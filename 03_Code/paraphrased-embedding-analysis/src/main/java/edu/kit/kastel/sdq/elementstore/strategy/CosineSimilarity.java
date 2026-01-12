/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.elementstore.strategy;

import edu.kit.kastel.sdq.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.knowledge.Element;
import edu.kit.kastel.sdq.utils.Pair;

import java.util.List;

/**
 * A retrieval strategy that computes the cosine similarity between a query vector and
 * vectors of elements in the store. This strategy is used for finding similar elements
 * based on their vector embeddings.
 * It supports a configurable maximum number of results to return.
 */
public class CosineSimilarity implements RetrievalStrategy {
    /**
     * Special value for the maximum number of results that indicates no limit.
     * Only applicable for target stores (similarityRetriever = true) in LiSSA's similarity search.
     */
    public static final String MAX_RESULTS_INFINITY_ARGUMENT = "infinity";

    private final int maxResults;

    public CosineSimilarity(ModuleConfiguration configuration) {
        final String maxResultsKey = "max_results";
        boolean isInfinity = configuration.hasArgument(maxResultsKey)
                && configuration.argumentAsString(maxResultsKey).equalsIgnoreCase(MAX_RESULTS_INFINITY_ARGUMENT);

        if (isInfinity) {
            this.maxResults = Integer.MAX_VALUE;
        } else {
            this.maxResults = configuration.argumentAsInt(maxResultsKey, 10);
            if (maxResults < 1) {
                throw new IllegalArgumentException("The maximum number of results must be greater than 0.");
            }
        }
    }
    

    public static double cosineSimilarity(float[] queryVector, float[] elementVector) {
        if (queryVector.length != elementVector.length) {
            throw new IllegalArgumentException("The length of the query vector and the element vector must be equal.");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < queryVector.length; i++) {
            dotProduct += queryVector[i] * elementVector[i];
            normA += Math.pow(queryVector[i], 2);
            normB += Math.pow(elementVector[i], 2);
        }
        return (float) (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
    }

    /**
     * @param query 
     * @param allElementsInStore
     * @return
     */
    @Override
    public List<Pair<Element, Float>> findSimilarElements(Pair<Element, float[]> query, List<Pair<Element, float[]>> allElementsInStore) {
        return List.of();
    }
}
