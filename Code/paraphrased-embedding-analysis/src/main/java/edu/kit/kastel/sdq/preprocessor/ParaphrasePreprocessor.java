package edu.kit.kastel.sdq.preprocessor;

import dev.langchain4j.model.chat.ChatModel;
import edu.kit.kastel.sdq.ChatLanguageModelProvider;
import edu.kit.kastel.sdq.cache.Cache;
import edu.kit.kastel.sdq.cache.CacheKey;
import edu.kit.kastel.sdq.cache.CacheManager;
import edu.kit.kastel.sdq.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.context.ContextStore;
import edu.kit.kastel.sdq.knowledge.Artifact;
import edu.kit.kastel.sdq.knowledge.Element;

import java.util.ArrayList;
import java.util.List;

public class ParaphrasePreprocessor extends Preprocessor {
    private static final String DEFAULT_TEMPLATE =
            """
            Here is the text of a software {artifact_type}:
            
            {content}
            
            Paraphrase the requirement, retaining its original meaning.
            Answer with the paraphrased text only, without any additional explanation or formatting.
            """;
    private static final int DEFAULT_PARAPHRASE_COUNT = 5;
    private final ChatLanguageModelProvider llmProvider;
    private final ChatModel llm;
    private final Cache cache;
    private final String template;
    private final int paraphraseCount;

    public ParaphrasePreprocessor(ModuleConfiguration configuration, ContextStore contextStore) {
        super(contextStore);
        this.llmProvider = new ChatLanguageModelProvider(configuration);
        this.llm = this.llmProvider.createChatModel();
        this.cache = CacheManager.getDefaultInstance().getCache(this.getClass().getSimpleName()+"_"+this.llmProvider.modelName()+"_"+this.llmProvider.seed());
        this.template = configuration.argumentAsString("template", DEFAULT_TEMPLATE);
        this.paraphraseCount = configuration.argumentAsInt("count", DEFAULT_PARAPHRASE_COUNT);
    }

    /**
     * @param artifacts The list of artifacts to preprocess 
     * @return The list of elements created from the artifacts.
     */
    @Override
    public List<Element> preprocess(List<Artifact> artifacts) {
        var elements = new ArrayList<Element>();
        for (Artifact artifact : artifacts) {// Create an element for the original artifact
            var element = new Element(artifact.getIdentifier(), artifact.getType(), artifact.getContent(), 0, null, false);
            elements.add(element);
            var selfcopy = new Element(artifact.getIdentifier() + SEPARATOR + "copy", artifact.getType(), artifact.getContent(), 1, element, false);
            elements.add(selfcopy); // So the classifier can access the original element
            System.out.println("Paraphrasing " + artifact.getIdentifier() + ":");
            System.out.println(artifact.getContent());

            for (int i = 0; i < paraphraseCount; i++) {
                System.out.print(i + " ");
                var paraphrase = new Element(artifact.getIdentifier() + SEPARATOR + "paraphrased" + SEPARATOR + i, artifact.getType(), paraphrase(artifact, i), 1, element, false);
                elements.add(paraphrase);
            }
            System.out.println();
        }
        return elements;
    }

    private String paraphrase(Artifact artifact,int i) {
        var llmRequest = this.template.replace("{content}", artifact.getContent())
                .replace("{artifact_type}", artifact.getType());
        var cacheKey = CacheKey.of(this.llmProvider.modelName(), this.llmProvider.seed(),CacheKey.Mode.CHAT, llmRequest+ i);
        var cachedResponse = this.cache.get(cacheKey, String.class);
        if (cachedResponse != null) {
            return cachedResponse;
        } else {
            var response = this.llm.chat(llmRequest);
            cache.put(cacheKey, response);
            return response;
        }
    }
}
