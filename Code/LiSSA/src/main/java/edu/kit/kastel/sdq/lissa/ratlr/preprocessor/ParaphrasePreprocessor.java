/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.preprocessor;

import dev.langchain4j.model.chat.ChatModel;
import edu.kit.kastel.sdq.lissa.ratlr.cache.Cache;
import edu.kit.kastel.sdq.lissa.ratlr.cache.CacheManager;
import edu.kit.kastel.sdq.lissa.ratlr.cache.ClassifierCacheKey;
import edu.kit.kastel.sdq.lissa.ratlr.classifier.ChatLanguageModelProvider;
import edu.kit.kastel.sdq.lissa.ratlr.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.Artifact;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.Element;

import java.util.*;

/**
 * A preprocessor that generates paraphrased versions of textual artifacts.
 *
 * <p>
 *     Example configuration:
 *     <pre>
 *"target_preprocessor" : {
 *  "name" : "paraphrase_openai",
 *  "args" : {
 *    "model" : "gpt-4o-2024-08-06",
 *    "superseed" : "1337420",
 *    "temperature" : "1",
 *    "count" : "3"
 *    }
 *},
 *     </pre>
 * </p>
 */
public class ParaphrasePreprocessor extends Preprocessor {
    private static final String DEFAULT_TEMPLATE =
            """
            Here is the text of a software {artifact_type}:

            {content}

                    Paraphrase the {artifact_type}, retaining its original meaning.
            Answer with the paraphrased text only, without any additional explanation or formatting.
            """;
    private static final int DEFAULT_PARAPHRASE_COUNT = 5;
    private final Map<ChatLanguageModelProvider, ChatModel> llmProvidersToModels;
    private final Map<ChatLanguageModelProvider, Cache> llmProvidersToCaches;
    private final String template;
    private final int paraphraseCount;
    /*
     * The superSeed value is used to pseudo-randomly generate seeds for the LLMs.
     */
    private final int superSeed;

    public ParaphrasePreprocessor(ModuleConfiguration configuration, ContextStore contextStore) {
        super(contextStore);
        this.paraphraseCount = configuration.argumentAsInt("count", DEFAULT_PARAPHRASE_COUNT);
        this.template = configuration.argumentAsString("template", DEFAULT_TEMPLATE);
        this.superSeed = configuration.argumentAsInt("superseed");
        if (this.superSeed <= 0) {
            throw new IllegalArgumentException("The superseed must be a positive integer.");
        }
        this.llmProvidersToModels = initializeModelProviders(configuration);
        this.llmProvidersToCaches = initializeCaches();
    }

    /**
     * Initializes multiple LLM providers and their corresponding chat models based on the configured superseed.
     *
     * @param configuration The base configuration for the LLM providers.
     * @return A map of LLM providers to their chat model instances.
     */
    private Map<ChatLanguageModelProvider, ChatModel> initializeModelProviders(ModuleConfiguration configuration) {
        var providersToModels = new IdentityHashMap<ChatLanguageModelProvider, ChatModel>();
        var randomNumberGenerator = new Random(this.superSeed);
        for (int i = 0; i < this.paraphraseCount; i++) {
            var providerInstance = new ChatLanguageModelProvider(
                    configuration.name(),
                    configuration.argumentAsString("model"),
                    randomNumberGenerator.nextInt(),
                    configuration.argumentAsDouble("temperature"));
            var modelInstance = providerInstance.createChatModel();
            providersToModels.put(providerInstance, modelInstance);
        }
        return providersToModels;
    }

    private Map<ChatLanguageModelProvider, Cache> initializeCaches() {
        var caches = new IdentityHashMap<ChatLanguageModelProvider, Cache>();
        for (var provider : this.llmProvidersToModels.keySet()) {
            var cache = CacheManager.getDefaultInstance().getCache(this, provider.getCacheParameters());
            caches.put(provider, cache);
        }
        return caches;
    }

    /**
     * @param artifacts The list of artifacts to preprocess
     * @return The list of elements created from the artifacts.
     * Each artifact results in one original element and multiple paraphrased elements.
     */
    @Override
    public List<Element> preprocess(List<Artifact> artifacts) {
        var elements = new ArrayList<Element>();
        for (Artifact artifact : artifacts) {
            // Create an element for the original artifact
            var element =
                    new Element(artifact.getIdentifier(), artifact.getType(), artifact.getContent(), 0, null, false);
            elements.add(element);
            var selfCopy = new Element(
                    artifact.getIdentifier() + SEPARATOR + "copy",
                    artifact.getType(),
                    artifact.getContent(),
                    1,
                    element,
                    true);
            elements.add(selfCopy); // So the classifier can access the original element
            for (var provider : this.llmProvidersToModels.keySet()) {
                var paraphrase = new Element(
                        artifact.getIdentifier() + SEPARATOR + "paraphrased" + SEPARATOR + provider.seed(),
                        artifact.getType(),
                        paraphrase(artifact, provider),
                        1,
                        element,
                        true);
                elements.add(paraphrase);
            }
        }
        return elements;
    }

    /**
     * Generates a paraphrased version of the given artifact using the specified LLM provider.
     *
     * @param artifact The artifact to paraphrase.
     * @param provider The LLM provider to use for paraphrasing.
     * @return The paraphrased text.
     */
    private String paraphrase(Artifact artifact, ChatLanguageModelProvider provider) {
        var llm = this.llmProvidersToModels.get(provider);
        var cache = this.llmProvidersToCaches.get(provider);
        var llmRequest = this.template
                .replace("{content}", artifact.getContent())
                .replace("{artifact_type}", artifact.getType());
        var cacheKey = ClassifierCacheKey.of(
                provider.modelName(), provider.seed(), provider.temperature(), llmRequest);
        var cachedResponse = cache.get(cacheKey, String.class);
        if (cachedResponse != null) {
            logger.info("Cache hit for {}", cacheKey);
            return cachedResponse;
        } else {
            logger.info("Paraphrasing {} (Seed:{})", artifact.getIdentifier(), provider.seed());
            var response = llm.chat(llmRequest);
            cache.put(cacheKey, response);
            return response;
        }
    }
}
