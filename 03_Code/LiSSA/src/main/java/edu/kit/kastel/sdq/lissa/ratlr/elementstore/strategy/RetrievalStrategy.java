/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.elementstore.strategy;

import edu.kit.kastel.sdq.lissa.ratlr.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.Element;
import edu.kit.kastel.sdq.lissa.ratlr.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface RetrievalStrategy {
    Logger logger = LoggerFactory.getLogger(RetrievalStrategy.class);

    /**
     * Find similar elements to the given query element in the provided list.
     * Amount of returned elements is determined by the strategy's configuration.
     * @param query Initial element and its embedding. Used as starting point for similarity search.
     * @param allElementsInStore List of all elements and their embeddings available for similarity search.
     * @return List of similar elements along with their similarity scores.
     */
    default List<Pair<Element, Float>> findSimilarElements(
            Pair<Element, float[]> query, List<Pair<Element, float[]>> allElementsInStore) {
        // Returns exactly configured maxResults results
        return findSimilarElements(query, allElementsInStore, List.of());
    }

    /**
     * Find similar elements to the given query element in the provided list.
     * @param query Initial element and its embedding. Used as starting point for similarity search.
     * @param allElementsInStore List of all elements and their embeddings available for similarity search.
     * @param excludedElements List of elements to exclude from the results.
     * @return List of similar elements along with their similarity scores.
     */
    List<Pair<Element, Float>> findSimilarElements(
            Pair<Element, float[]> query,
            List<Pair<Element, float[]>> allElementsInStore,
            List<Element> excludedElements);

    default void removeExcludedElements(List<Pair<Element, Float>> similarElements, List<Element> excludedElements) {
        similarElements.removeIf(elementPair -> excludedElements.stream().anyMatch(excluded -> excluded.getIdentifier()
                .equals(elementPair.first().getIdentifier())));
    }

    static RetrievalStrategy createStrategy(ModuleConfiguration configuration) {
        return switch (configuration.name()) {
            case "paraphrased_to_artifacts" -> new ParaphrasedElementsToArtifactsRetrievalStrategy(configuration);
            case "cosine_similarity" -> new CosineSimilarity(configuration);
            case "custom" -> {
                logger.warn("For backwards compatibility: Using cosine similarity as default retrieval strategy.");
                yield new CosineSimilarity(configuration);
            }
            default -> throw new IllegalStateException("Unknown strategy name: " + configuration.name());
        };
    }
}
