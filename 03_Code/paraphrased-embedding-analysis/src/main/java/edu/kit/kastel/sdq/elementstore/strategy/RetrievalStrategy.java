/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.elementstore.strategy;

import edu.kit.kastel.sdq.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.knowledge.Element;
import edu.kit.kastel.sdq.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface RetrievalStrategy {
    Logger logger = LoggerFactory.getLogger(RetrievalStrategy.class);

    List<Pair<Element, Float>> findSimilarElements(
            Pair<Element, float[]> query, List<Pair<Element, float[]>> allElementsInStore);

    static RetrievalStrategy createStrategy(ModuleConfiguration configuration) {
        return switch (configuration.name()) {
            case "cosine_similarity" -> new CosineSimilarity(configuration);
            case "custom" -> {
                logger.warn("For backwards compatibility: Using cosine similarity as default retrieval strategy.");
                yield new CosineSimilarity(configuration);
            }
            default -> throw new IllegalStateException("Unknown strategy name: " + configuration.name());
        };
    }
}
