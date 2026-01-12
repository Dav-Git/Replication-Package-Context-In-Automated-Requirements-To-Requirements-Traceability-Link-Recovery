/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.classifier;

import dev.langchain4j.model.chat.ChatModel;
import edu.kit.kastel.sdq.lissa.ratlr.cache.Cache;
import edu.kit.kastel.sdq.lissa.ratlr.cache.CacheManager;
import edu.kit.kastel.sdq.lissa.ratlr.cache.ClassifierCacheKey;
import edu.kit.kastel.sdq.lissa.ratlr.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;
import edu.kit.kastel.sdq.lissa.ratlr.elementstore.SourceElementStore;
import edu.kit.kastel.sdq.lissa.ratlr.elementstore.TargetElementStore;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.Element;
import edu.kit.kastel.sdq.lissa.ratlr.utils.Pair;

import java.util.List;
import java.util.Optional;

/**
 * A classifier that uses embeddings to find relevant context elements for classification.
 * The classifier retrieves similar elements from a specified ElementStore which is accessed
 * through its context_id. The retrieved elements are then used as context in a language model
 * prompt to determine if a traceability link exists between the source and target elements.
 * <p>
 * Example configuration:
 * <pre>
 * ...
 * "classifier" : {
 *  "name" : "embeddings-as-context_openai",
 *  "args" : {
 *    "model" : "gpt-4o-2024-08-06",
 *    "query_context": "source_requirements",
 *    "search_context" : "target_requirements",
 *    "search_mode" : "TARGET"
 *  }
 * },
 * ...
 * </pre>
 * </p>
 * The query_context is used to retrieve the embedding used to query the search_context for similar elements.
 * Both need to be ElementStores and may have the same or different context ids.
 *
 * @see Classifier
 */
public class EmbeddingsAsContextClassifier extends Classifier {
    public static final String EMBEDDINGS_AS_CONTEXT_CLASSIFIER_NAME = "embeddings-as-context";
    protected final Cache cache;
    /**
     * Provider for the language model used in classification.
     */
    protected final ChatLanguageModelProvider provider;
    /**
     * The language model instance used for classification.
     */
    protected final ChatModel llm;
    /**
     * The template used for classification requests.
     */
    protected String template;

    private final String DEFAULT_TEMPLATE =
            """
                    Below are artifacts from the same software system. Is there a traceability link between (a) and (b)?
                    Consider that the numbered {search_mode} {context_type} also exist in the same software system.
                    Give your reasoning, then answer with 'yes' or 'no' enclosed in <trace></trace>.

                    (a) {source_type}: ```{source_content}```
                    (b) {target_type}: ```{target_content}```

                    Context:
                    {context_content}
                    """;

    private final String PROMPT_TEMPLATE_KEY = "template";
    private final String searchContextId;
    private final SearchMode searchMode;
    private final String queryContextId;

    public EmbeddingsAsContextClassifier(ModuleConfiguration configuration, ContextStore contextStore) {
        super(ChatLanguageModelProvider.threads(configuration), contextStore);
        this.provider = new ChatLanguageModelProvider(configuration);
        this.llm = this.provider.createChatModel();
        this.cache = CacheManager.getDefaultInstance().getCache(this, provider.getCacheParameters());
        this.template = configuration.argumentAsString(PROMPT_TEMPLATE_KEY, DEFAULT_TEMPLATE);
        this.searchContextId = configuration.argumentAsString("search_context", "");
        if (searchContextId.isEmpty()) {
            throw new IllegalArgumentException("No search_context provided for EmbeddingsAsContextClassifier.");
        }
        this.searchMode = configuration.argumentAsEnum("search_mode", 0, SearchMode.values());
        this.queryContextId = configuration.argumentAsString("query_context", "");
        if (queryContextId.isEmpty()) {
            throw new IllegalArgumentException("No query_context provided for EmbeddingsAsContextClassifier.");
        }
    }

    /**
     * Creates a copy of this embedding as context classifier.
     * This method is used internally for parallel processing.
     */
    private EmbeddingsAsContextClassifier(
            int threads,
            Cache cache,
            ChatLanguageModelProvider provider,
            String template,
            ContextStore contextStore,
            String searchContextId,
            String queryContextId,
            SearchMode searchMode) {
        super(threads, contextStore);
        this.cache = cache;
        this.provider = provider;
        this.llm = this.provider.createChatModel();
        this.template = template;
        this.searchContextId = searchContextId;
        this.queryContextId = queryContextId;
        this.searchMode = searchMode;
    }

    @Override
    protected Optional<ClassificationResult> classify(Element source, Element target) {
        TargetElementStore searchContext = contextStore.getContext(searchContextId, TargetElementStore.class);
        if (searchContext == null) {
            throw new IllegalArgumentException("No context with ID " + searchContextId + " found in context store.");
        }
        var query = getElementPairForContextSearch(source, target);
        var contextElements = getContextElements(searchContext, query, source, target);
        String llmRequest = buildLlmRequest(source, target, contextElements, searchMode);
        String llmResponse = queryCachedLlm(llmRequest, source, target, contextElements);

        return evaluateLlmResponse(source, target, llmResponse);
    }

    private Pair<Element, float[]> getElementPairForContextSearch(Element source, Element target) {
        SourceElementStore queryContext = contextStore.getContext(this.queryContextId, SourceElementStore.class);
        if (queryContext == null) {
            throw new IllegalArgumentException("No context with ID " + queryContextId + " found in context store.");
        }
        // We have to look for the source or target element in the query context to get its embedding.
        // Depending on the chosen query store, only one of the two elements will be present.
        Pair<Element, float[]> elementWithEmbedding = queryContext.getById(source.getIdentifier());
        if (elementWithEmbedding == null) {
            elementWithEmbedding = queryContext.getById(target.getIdentifier());
        }
        if (elementWithEmbedding == null) {
            throw new IllegalStateException("Could not find element for similarity search in query store. Element IDs: "
                    + source.getIdentifier() + ", " + target.getIdentifier());
        }
        return elementWithEmbedding;
    }

    /**
     * Retrieves context elements similar to the query from the search context.
     * Retrieved elements are never the same element as the source or target element.
     *
     * @param searchContext The context in which to search for similar elements.
     * @param query         The element and its embedding used for the similarity search.
     * @param source        The source element for the current classification task.
     * @param target        The target element for the current classification task.
     * @return A list of context elements similar to the query.
     */
    private List<Element> getContextElements(
            TargetElementStore searchContext, Pair<Element, float[]> query, Element source, Element target) {
        return searchContext.findSimilar(query, List.of(source, target));
    }

    private String buildLlmRequest(
            Element source, Element target, List<Element> contextElements, SearchMode searchMode) {
        StringBuilder contextContent = new StringBuilder();
        for (int i = 1; i <= contextElements.size(); i++) {
            contextContent
                    .append("(")
                    .append(i)
                    .append("): ```")
                    .append(contextElements.get(i - 1).getContent())
                    .append("```\n");
        }

        return template.replace("{source_type}", source.getType())
                .replace("{search_mode}", searchMode.toString())
                .replace("{source_content}", source.getContent())
                .replace("{target_type}", target.getType())
                .replace("{target_content}", target.getContent())
                .replace("{context_type}", contextElements.getFirst().getType())
                .replace("{context_content}", contextContent.toString());
    }

    private String queryCachedLlm(String request, Element source, Element target, List<Element> contextElements) {
        ClassifierCacheKey cacheKey =
                ClassifierCacheKey.of(provider.modelName(), provider.seed(), provider.temperature(), request);
        String cachedResponse = cache.get(cacheKey, String.class);
        if (cachedResponse != null) {
            logger.info(
                    "Cache hit for ({}): {} and {} with context {}",
                    provider.modelName(),
                    source.getIdentifier(),
                    target.getIdentifier(),
                    contextElements.stream().map(Element::getIdentifier).toList());
            return cachedResponse;
        } else {
            logger.info(
                    "Classifying ({}): {} and {} with context {}",
                    provider.modelName(),
                    source.getIdentifier(),
                    target.getIdentifier(),
                    contextElements.stream().map(Element::getIdentifier).toList());
            String response = llm.chat(request);
            cache.put(cacheKey, response);
            return response;
        }
    }

    private Optional<ClassificationResult> evaluateLlmResponse(Element source, Element target, String llmResponse) {
        String thinkEnd = "</think>";
        if (llmResponse.startsWith("<think>") && llmResponse.contains(thinkEnd)) {
            // Omit the thinking of models like deepseek-r1
            llmResponse = llmResponse
                    .substring(llmResponse.indexOf(thinkEnd) + thinkEnd.length())
                    .strip();
        }
        boolean isRelated;
        if (llmResponse.contains("<trace>")) {
            int beginTrace = llmResponse.indexOf("<trace>");
            int endTrace = llmResponse.indexOf("</trace>");
            isRelated = llmResponse.substring(beginTrace, endTrace).contains("yes");
        } else {
            isRelated = llmResponse.toLowerCase().contains("yes");
        }
        if (isRelated) {
            return Optional.of(ClassificationResult.of(source, target));
        }
        return Optional.empty();
    }

    @Override
    protected Classifier copyOf() {
        return new EmbeddingsAsContextClassifier(
                threads, cache, provider, template, contextStore, searchContextId, queryContextId, searchMode);
    }

    @Override
    public void setClassificationPrompt(String prompt) {
        this.template = prompt;
    }

    @Override
    public String getClassificationPromptKey() {
        return PROMPT_TEMPLATE_KEY;
    }

    /**
     * Enum representing the possible search modes for context retrieval.
     * SOURCE: The context elements are source elements.
     * TARGET: The context elements are target elements.
     * UNKNOWN: The category of the context elements is not known.
     */
    private enum SearchMode {
        UNKNOWN,
        SOURCE,
        TARGET
    }
}
