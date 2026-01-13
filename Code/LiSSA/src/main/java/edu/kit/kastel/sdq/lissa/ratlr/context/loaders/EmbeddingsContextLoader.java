/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.context.loaders;

import edu.kit.kastel.sdq.lissa.ratlr.artifactprovider.ArtifactProvider;
import edu.kit.kastel.sdq.lissa.ratlr.configuration.ContextConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;
import edu.kit.kastel.sdq.lissa.ratlr.elementstore.ElementStore;
import edu.kit.kastel.sdq.lissa.ratlr.elementstore.TargetElementStore;
import edu.kit.kastel.sdq.lissa.ratlr.embeddingcreator.EmbeddingCreator;
import edu.kit.kastel.sdq.lissa.ratlr.preprocessor.Preprocessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A context loader that builds a context using artifact embeddings.
 * It preprocesses artifacts, calculates embeddings, and sets up an element store.
 *
 * <p>
 *     Example configuration:
 *    <pre>
 *"context_configurations" : [
 *    ...,
 *    {
 *      "name" : "embeddings",
 *      "context_id" : "target_requirements",
 *      "modules" : [{
 *          "ARTIFACT_PROVIDER" : {
 *              "name" : "text",
 *              "args" : {
 *                  "artifact_type" : "requirement",
 *                  "path" : "./datasets/dronology/low"
 *              }
 *          },
 *          "PREPROCESSOR" : {
 *              "name" : "artifact",
 *              "args" : { }
 *          },
 *          "EMBEDDING_CREATOR" : {
 *              "name" : "openai",
 *              "args" : {
 *              "model" : "text-embedding-3-large"
 *              }
 *          },
 *          "ELEMENT_STORE" : {
 *              "name" : "custom",
 *              "args" : {
 *                  "max_results" : "5"
 *              }
 *          }
 *      }]
 *   },
 *   ...
 *]
 *    </pre>
 * </p>
 */
public class EmbeddingsContextLoader extends ContextLoader {
    private final Logger logger;
    private final ArtifactProvider artifactProvider;
    private final Preprocessor preprocessor;
    private final EmbeddingCreator embeddingCreator;
    private final ElementStore elementStore;

    protected EmbeddingsContextLoader(String contextId, ContextStore contextStore, ContextConfiguration configuration) {
        super(contextId, contextStore);
        logger = LoggerFactory.getLogger(EmbeddingsContextLoader.class.getName() + "[" + contextId + "]");
        this.artifactProvider = ArtifactProvider.createArtifactProvider(
                configuration.getModuleConfiguration(AllowableContextModuleTypes.ARTIFACT_PROVIDER, 0), contextStore);
        this.preprocessor = Preprocessor.createPreprocessor(
                configuration.getModuleConfiguration(AllowableContextModuleTypes.PREPROCESSOR, 0), contextStore);
        this.embeddingCreator = EmbeddingCreator.createEmbeddingCreator(
                configuration.getModuleConfiguration(AllowableContextModuleTypes.EMBEDDING_CREATOR, 0), contextStore);
        this.elementStore = new TargetElementStore(
                configuration.getModuleConfiguration(AllowableContextModuleTypes.ELEMENT_STORE, 0), contextId);
    }

    @Override
    public void loadContext() {
        logger.info("Loading artifacts");
        var artifacts = artifactProvider.getArtifacts();

        logger.info("Preprocessing artifacts");
        var elements = preprocessor.preprocess(artifacts);

        logger.info("Calculating embeddings");
        var embeddings = embeddingCreator.calculateEmbeddings(elements);

        logger.info("Building element store");
        elementStore.setup(elements, embeddings);

        contextStore.createContext(elementStore);
    }
}
