/* Licensed under MIT 2025-2026. */
package edu.kit.kastel.sdq.lissa.ratlr.classifier;

import dev.langchain4j.model.chat.ChatModel;
import edu.kit.kastel.sdq.lissa.ratlr.cache.Cache;
import edu.kit.kastel.sdq.lissa.ratlr.cache.CacheManager;
import edu.kit.kastel.sdq.lissa.ratlr.cache.ClassifierCacheKey;
import edu.kit.kastel.sdq.lissa.ratlr.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;
import edu.kit.kastel.sdq.lissa.ratlr.elementstore.TargetElementStore;
import edu.kit.kastel.sdq.lissa.ratlr.goldstandard.GoldStandard;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.Element;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.TraceLink;
import edu.kit.kastel.sdq.lissa.ratlr.utils.Pair;

import java.util.*;

public class ExternalTraceLinksAsContextClassifier extends Classifier {
    public static final String EXTERNAL_TRACE_LINKS_AS_CONTEXT_CLASSIFIER_NAME = "external-trace-links-as-context";
    public final String DEFAULT_EXAMPLE_TEMPLATE =
            """
                    ({i}) Source {source_type}: '''{source_content}'''
                    Target {target_type}: '''{target_content}'''
                    Link: {link}
                    """;
    /**
     * Cache for storing classification results.
     */
    protected final Cache cache;
    /**
     * Provider for the language model used in classification.
     */
    protected final ChatLanguageModelProvider provider;
    /**
     * The language model instance used for classification.
     */
    protected final ChatModel llm;

    private final String DEFAULT_TEMPLATE =
            """
                    Below are artifacts from multiple software systems.
                    Considering the examples, is there a traceability link between the source and target requirement ({i}) ?
            Answer with 'yes' or 'no'.

            {negative_examples}
            {positive_examples}
            ({i}) Source {source_type}: '''{source_content}'''
            Target {target_type}: '''{target_content}'''
                    Link:
            """;
    private final String CLASSIFICATION_PROMPT_KEY = "template";
    /**
     * The context ID for the internal source element store.
     */
    private final String internalSourceStoreContextId;
    /**
     * The context ID for the internal target element store.
     */
    private final String internalTargetStoreContextId;
    /**
     * The context ID for the external source element store.
     */
    private final String externalSourceStoreContextId;
    /**
     * The context ID for the external target element store.
     */
    private final String externalTargetStoreContextId;
    /**
     * The context ID for the external gold standard.
     */
    private final String externalGoldStandardContextId;
    /**
     * The number of examples to include in the prompt.
     */
    private final int numberOfExamples;
    /**
     * The template used for classification requests.
     */
    protected String template;
    /**
     * The template used for examples in classification requests.
     */
    protected String exampleTemplate;

    public ExternalTraceLinksAsContextClassifier(ModuleConfiguration configuration, ContextStore contextStore) {
        super(ChatLanguageModelProvider.threads(configuration), contextStore);
        this.provider = new ChatLanguageModelProvider(configuration);
        this.llm = provider.createChatModel();
        this.cache = CacheManager.getDefaultInstance().getCache(this, provider.getCacheParameters());
        this.template = configuration.argumentAsString(CLASSIFICATION_PROMPT_KEY, DEFAULT_TEMPLATE);
        this.exampleTemplate = configuration.argumentAsString("example_template", DEFAULT_EXAMPLE_TEMPLATE);
        this.internalSourceStoreContextId = configuration.argumentAsString("internal_source_context_id");
        this.internalTargetStoreContextId = configuration.argumentAsString("internal_target_context_id");
        this.externalSourceStoreContextId = configuration.argumentAsString("external_source_context_id");
        this.externalTargetStoreContextId = configuration.argumentAsString("external_target_context_id");
        this.externalGoldStandardContextId = configuration.argumentAsString("external_trace_links_context_id");
        this.numberOfExamples = configuration.argumentAsInt("number_of_examples", 4);
    }

    /**
     * Creates a copy of this embedding as context classifier.
     * This method is used internally for parallel processing.
     */
    public ExternalTraceLinksAsContextClassifier(
            int threads,
            ContextStore contextStore,
            Cache cache,
            ChatLanguageModelProvider provider,
            String template,
            String exampleTemplate,
            String internalSourceStoreContextId,
            String internalTargetStoreContextId,
            String externalSourceStoreContextId,
            String externalTargetStoreContextId,
            String externalGoldStandardContextId,
            int numberOfExamples) {
        super(threads, contextStore);
        this.cache = cache;
        this.provider = provider;
        this.llm = provider.createChatModel();
        this.template = template;
        this.exampleTemplate = exampleTemplate;
        this.internalSourceStoreContextId = internalSourceStoreContextId;
        this.internalTargetStoreContextId = internalTargetStoreContextId;
        this.externalSourceStoreContextId = externalSourceStoreContextId;
        this.externalTargetStoreContextId = externalTargetStoreContextId;
        this.externalGoldStandardContextId = externalGoldStandardContextId;
        this.numberOfExamples = numberOfExamples;
    }

    @Override
    protected Optional<ClassificationResult> classify(Element source, Element target) {
        // We have to retrieve the necessary contexts here because they are not yet loaded when the classifier is
        // created in the main thread.
        final GoldStandard externalGoldStandard =
                contextStore.getContext(externalGoldStandardContextId, GoldStandard.class);
        if (externalGoldStandard == null) {
            throw new IllegalStateException(
                    "External GoldStandard not found in context store with ID: " + externalGoldStandardContextId);
        }
        final TargetElementStore internalSourceStore =
                contextStore.getContext(internalSourceStoreContextId, TargetElementStore.class);
        if (internalSourceStore == null) {
            throw new IllegalStateException(
                    "Internal SourceStore not found in context store with ID: " + internalSourceStoreContextId);
        }
        final TargetElementStore internalTargetStore =
                contextStore.getContext(internalTargetStoreContextId, TargetElementStore.class);
        if (internalTargetStore == null) {
            throw new IllegalStateException(
                    "Internal TargetStore not found in context store with ID: " + internalTargetStoreContextId);
        }
        final TargetElementStore externalSourceStore =
                contextStore.getContext(externalSourceStoreContextId, TargetElementStore.class);
        if (externalSourceStore == null) {
            throw new IllegalStateException(
                    "External SourceStore not found in context store with ID: " + externalSourceStoreContextId);
        }
        final TargetElementStore externalTargetStore =
                contextStore.getContext(externalTargetStoreContextId, TargetElementStore.class);
        if (externalTargetStore == null) {
            throw new IllegalStateException(
                    "External TargetStore not found in context store with ID: " + externalTargetStoreContextId);
        }

        // Get info from contexts
        Pair<Element, float[]> sourceElementWithEmbedding = internalSourceStore.getById(source.getIdentifier());
        if (sourceElementWithEmbedding == null) {
            throw new IllegalStateException("Internal Source " + source.getIdentifier()
                    + " not found in context store with ID: " + internalSourceStoreContextId);
        }
        List<TraceLink> positiveExternalTraceLinks =
                getPositiveExamples(target, externalSourceStore, sourceElementWithEmbedding, externalGoldStandard);
        positiveExternalTraceLinks = sortTraceLinksByTargetSimilarity(
                target, internalTargetStore, externalTargetStore, positiveExternalTraceLinks);
        // Only keep the top-n "numberOfExamples" examples
        positiveExternalTraceLinks =
                positiveExternalTraceLinks.subList(0, Math.min(numberOfExamples, positiveExternalTraceLinks.size()));
        if (positiveExternalTraceLinks.size() < numberOfExamples) {
            logger.warn(
                    "Using only {} positive external trace links for classification of {} and {}.",
                    positiveExternalTraceLinks.size(),
                    source.getIdentifier(),
                    target.getIdentifier());
        }
        List<TraceLink> negativeExternalTraceLinks = generateNegativeExamples(
                target, positiveExternalTraceLinks, externalSourceStore, externalTargetStore, externalGoldStandard);

        // Build prompt and classify
        String prompt = buildPrompt(
                source,
                target,
                positiveExternalTraceLinks,
                negativeExternalTraceLinks,
                externalSourceStore,
                externalTargetStore);
        String response =
                queryCachedLlm(prompt, source, target, positiveExternalTraceLinks, negativeExternalTraceLinks);
        boolean isRelated = response.toLowerCase().contains("yes");
        if (isRelated) {
            return Optional.of(ClassificationResult.of(source, target));
        } else {
            return Optional.empty();
        }
    }

    private List<TraceLink> getPositiveExamples(
            Element target,
            TargetElementStore externalSourceStore,
            Pair<Element, float[]> sourceElementWithEmbedding,
            GoldStandard externalGoldStandard) {
        List<Element> similarExternalSourceElements = externalSourceStore.findSimilar(sourceElementWithEmbedding);
        List<TraceLink> positiveExternalTraceLinks = new ArrayList<>();
        for (Element similarExternalSourceElement : similarExternalSourceElements) {
            if (positiveExternalTraceLinks.size() >= numberOfExamples) {
                // We only want to get TraceLinks that are similar to the source element until we have enough examples
                break;
            }
            Set<TraceLink> traceLinksFromExternalSourceElement =
                    externalGoldStandard.getTraceLinks(similarExternalSourceElement);
            positiveExternalTraceLinks.addAll(traceLinksFromExternalSourceElement);
            positiveExternalTraceLinks = new ArrayList<>(positiveExternalTraceLinks.stream()
                    .filter(tl -> !(tl.targetId().equals(normalizeElementId(target.getIdentifier()))))
                    .toList());
        }
        return positiveExternalTraceLinks;
    }

    /**
     * Sort the provided trace links based on the similarity of the trace link's target element to the given target element (of the classification).
     *
     * @param target              The target element of the current classification. Similarity is computed against this element.
     * @param internalTargetStore ElementStore containing the internal target elements.
     * @param externalTargetStore ElementStore containing the external target elements.
     * @param externalTraceLinks  The TraceLinks to sort
     * @return The provided trace links sorted by target-to-target similarity.
     */
    private List<TraceLink> sortTraceLinksByTargetSimilarity(
            Element target,
            TargetElementStore internalTargetStore,
            TargetElementStore externalTargetStore,
            List<TraceLink> externalTraceLinks) {
        var targetElementWithEmbedding = internalTargetStore.getById(target.getIdentifier());
        if (targetElementWithEmbedding == null) {
            throw new IllegalStateException("Target " + target.getIdentifier()
                    + " not found in Internal Target Store context with ID: " + internalTargetStoreContextId);
        }
        List<Element> externalTargetsSimilarToTarget = externalTargetStore.findSimilar(targetElementWithEmbedding);
        var externalTargetIdsToTraceLinks = new HashMap<String, List<TraceLink>>();
        // Construct a mapping from external target IDs to TraceLinks for lookup
        for (TraceLink tl : externalTraceLinks) {
            var traceLinks = externalTargetIdsToTraceLinks.get(tl.targetId());
            if (traceLinks == null) {
                traceLinks = new ArrayList<>();
            }
            traceLinks.add(tl);
            externalTargetIdsToTraceLinks.put(tl.targetId(), traceLinks);
        }

        List<TraceLink> externalTraceLinksSortedByTargetSimilarity = new ArrayList<>();
        // externalTargetsSimilarToTarget is already sorted by similarity to the internal target
        // So we can just iterate over it and add the corresponding trace links in order
        for (Element externalTarget : externalTargetsSimilarToTarget) {
            if (externalTargetIdsToTraceLinks.containsKey(externalTarget.getIdentifier())) {
                externalTraceLinksSortedByTargetSimilarity.addAll(
                        externalTargetIdsToTraceLinks.get(externalTarget.getIdentifier()));
            }
        }
        return externalTraceLinksSortedByTargetSimilarity;
    }

    private List<TraceLink> generateNegativeExamples(
            Element target,
            List<TraceLink> positiveExternalTraceLinksSortedByTargetSimilarity,
            TargetElementStore externalSourceStore,
            TargetElementStore externalTargetStore,
            GoldStandard externalGoldStandard) {
        List<TraceLink> negativeExamples = new ArrayList<>();
        var externalTraceLinkSources = positiveExternalTraceLinksSortedByTargetSimilarity.stream()
                .map(traceLink -> {
                    var source = externalSourceStore.getById(traceLink.sourceId());
                    if (source == null) {
                        throw new IllegalStateException("Source " + traceLink.sourceId() + "for TraceLink " + traceLink
                                + " not found in External Source Store.");
                    }
                    return source;
                })
                .toList();
        // For each source of a positive trace link, find a similar element that is not linked to the target
        for (Pair<Element, float[]> positiveTraceLinkSource : externalTraceLinkSources) {
            // Iterate over all potential targets similar to the positive trace link source
            for (Element similarToPositiveTraceLinkSource : externalTargetStore.findSimilar(positiveTraceLinkSource)) {
                String positiveTraceLinkSourceId =
                        positiveTraceLinkSource.first().getIdentifier();
                TraceLink negativeTraceLinkCandidate =
                        new TraceLink(positiveTraceLinkSourceId, similarToPositiveTraceLinkSource.getIdentifier());
                if (!externalGoldStandard.contains(negativeTraceLinkCandidate)
                        && !negativeExamples.contains(negativeTraceLinkCandidate)
                        && !(negativeTraceLinkCandidate
                        .targetId()
                        .equals(normalizeElementId(target.getIdentifier())))) {
                    negativeExamples.add(negativeTraceLinkCandidate);
                    // If we manage to construct negative example, we can break the inner loop and continue with the
                    // next source
                    break;
                }
            }
        }
        return negativeExamples;
    }

    /**
     * Builds the string used for examples in the classification prompt.
     *
     * @param traceLinks          The trace links to use as examples.
     * @param externalSourceStore The element store containing the external source elements used in the trace links.
     * @param externalTargetStore The element store containing the external target elements used in the trace links.
     * @param exampleType         The type of examples (positive or negative).
     * @param counter             Running counter for example numbering. Should be shared for positive and negative examples.
     * @return A pair containing the built examples string and the current value of the counter.
     */
    private Pair<String, Integer> buildExamplesString(
            List<TraceLink> traceLinks,
            TargetElementStore externalSourceStore,
            TargetElementStore externalTargetStore,
            ExampleType exampleType,
            int counter) {
        StringBuilder examples = new StringBuilder();
        var isLink = exampleType == ExampleType.POSITIVE ? "yes" : "no";
        for (TraceLink traceLink : traceLinks) {
            Element source = externalSourceStore.getById(traceLink.sourceId()).first();
            Element target = externalTargetStore.getById(traceLink.targetId()).first();
            examples.append(exampleTemplate
                    .replace("{i}", Integer.toString(counter))
                    .replace("{source_type}", source.getType())
                    .replace("{source_content}", source.getContent())
                    .replace("{target_type}", target.getType())
                    .replace("{target_content}", target.getContent())
                    .replace("{link}", isLink));
            counter++;
        }
        return new Pair<>(examples.toString(), counter);
    }

    private String buildPrompt(
            Element source,
            Element target,
            List<TraceLink> positiveExternalTraceLinksSortedByTargetSimilarity,
            List<TraceLink> negativeExternalTraceLinks,
            TargetElementStore externalSourceStore,
            TargetElementStore externalTargetStore) {
        int counter = 1;
        String positiveExamples;
        String negativeExamples;
        // Examples can be numbered from 1 to X
        // To keep numbering consistent, we build either positive or negative examples first depending on their position
        // in the template
        if (template.indexOf("{positive_examples}") < template.indexOf("{negative_examples}")) {
            Pair<String, Integer> positiveExamplesStringAndCounter = buildExamplesString(
                    positiveExternalTraceLinksSortedByTargetSimilarity,
                    externalSourceStore,
                    externalTargetStore,
                    ExampleType.POSITIVE,
                    counter);
            positiveExamples = positiveExamplesStringAndCounter.first();
            counter = positiveExamplesStringAndCounter.second();
            Pair<String, Integer> negativeExamplesStringAndCounter = buildExamplesString(
                    negativeExternalTraceLinks,
                    externalSourceStore,
                    externalTargetStore,
                    ExampleType.NEGATIVE,
                    counter);
            negativeExamples = negativeExamplesStringAndCounter.first();
            counter = negativeExamplesStringAndCounter.second();
        } else {
            Pair<String, Integer> negativeExamplesStringAndCounter = buildExamplesString(
                    negativeExternalTraceLinks,
                    externalSourceStore,
                    externalTargetStore,
                    ExampleType.NEGATIVE,
                    counter);
            negativeExamples = negativeExamplesStringAndCounter.first();
            counter = negativeExamplesStringAndCounter.second();
            Pair<String, Integer> positiveExamplesStringAndCounter = buildExamplesString(
                    positiveExternalTraceLinksSortedByTargetSimilarity,
                    externalSourceStore,
                    externalTargetStore,
                    ExampleType.POSITIVE,
                    counter);
            positiveExamples = positiveExamplesStringAndCounter.first();
            counter = positiveExamplesStringAndCounter.second();
        }

        return template.replace("{i}", Integer.toString(counter))
                .replace("{positive_examples}", positiveExamples)
                .replace("{negative_examples}", negativeExamples)
                .replace("{source_type}", source.getType())
                .replace("{source_content}", source.getContent())
                .replace("{target_type}", target.getType())
                .replace("{target_content}", target.getContent());
    }

    /**
     * Return the response from the LLM to the provided prompt from the cache if available, otherwise query the LLM and store the result in the cache.
     *
     * @param prompt                     The prompt to send to the LLM.
     * @param source                     The source element of the current classification.
     * @param target                     The target element of the current classification.
     * @param positiveExternalTraceLinks The positive external trace links used in the prompt.
     * @param negativeExternalTraceLinks The negative external trace links used in the prompt.
     * @return The response from the LLM.
     */
    private String queryCachedLlm(
            String prompt,
            Element source,
            Element target,
            List<TraceLink> positiveExternalTraceLinks,
            List<TraceLink> negativeExternalTraceLinks) {
        ClassifierCacheKey cacheKey =
                ClassifierCacheKey.of(provider.modelName(), provider.seed(), provider.temperature(), prompt);
        String cachedResponse = cache.get(cacheKey, String.class);
        if (cachedResponse != null) {
            logger.info(
                    "Cache hit for {} and {} with positive {} and negative {}.",
                    source.getIdentifier(),
                    target.getIdentifier(),
                    positiveExternalTraceLinks,
                    negativeExternalTraceLinks);
            return cachedResponse;
        } else {
            logger.info(
                    "Classifying {} and {} with positive {} and negative {}.",
                    source.getIdentifier(),
                    target.getIdentifier(),
                    positiveExternalTraceLinks,
                    negativeExternalTraceLinks);
            String response = llm.chat(prompt);
            cache.put(cacheKey, response);
            return response;
        }
    }

    @Override
    protected Classifier copyOf() {
        return new ExternalTraceLinksAsContextClassifier(
                threads,
                contextStore,
                cache,
                provider,
                template,
                exampleTemplate,
                internalSourceStoreContextId,
                internalTargetStoreContextId,
                externalSourceStoreContextId,
                externalTargetStoreContextId,
                externalGoldStandardContextId,
                numberOfExamples);
    }

    @Override
    public void setClassificationPrompt(String prompt) {
        this.template = prompt;
    }

    @Override
    public String getClassificationPromptKey() {
        return CLASSIFICATION_PROMPT_KEY;
    }

    private String normalizeElementId(String id) {
        return id.replace(".txt", "");
    }

    private enum ExampleType {
        POSITIVE,
        NEGATIVE
    }
}
