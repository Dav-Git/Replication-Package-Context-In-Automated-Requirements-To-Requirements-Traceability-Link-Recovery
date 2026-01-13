/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.elementstore.strategy;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.store.embedding.CosineSimilarity;
import edu.kit.kastel.sdq.lissa.ratlr.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.Element;
import edu.kit.kastel.sdq.lissa.ratlr.utils.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A retrieval strategy that computes the cosine similarity between a query vector and
 * vectors of elements in the store. This strategy is used for finding similar elements
 * based on their vector embeddings.
 * It supports a configurable maximum number of results to return.
 */
public class ParaphrasedElementsToArtifactsRetrievalStrategy implements RetrievalStrategy {
    /**
     * Special value for the maximum number of results that indicates no limit.
     * Only applicable for target stores (similarityRetriever = true) in LiSSA's similarity search.
     */
    public static final String MAX_RESULTS_INFINITY_ARGUMENT = "infinity";

    private final int maxResults;

    public ParaphrasedElementsToArtifactsRetrievalStrategy(ModuleConfiguration configuration) {
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

    @Override
    public List<Pair<Element, Float>> findSimilarElements(
            Pair<Element, float[]> query,
            List<Pair<Element, float[]>> allElementsInStore,
            List<Element> excludedElements) {
        // First calculate similarity for all elements and sort starting with highest similarity
        List<Pair<Element, Float>> similarElements = calculateSimilarity(query, allElementsInStore);
        similarElements.sort((a, b) -> Float.compare(b.second(), a.second()));

        // Map paraphrased elements to their corresponding artifact elements
        var similarArtifactElements = similarElements.stream()
                .map(elementPair -> new Pair<>(Element.getArtifactElement(elementPair.first()), elementPair.second()))
                .toList();

        // The list now contains each ArtifactElement multiple times (because multiple Elements (paraphrases) map to the
        // same artifact)
        // So we need to deduplicate them, keeping only the first occurrence of each ArtifactElement(which has the
        // highest similarity score)
        var similarArtifactElementsWithoutDuplicates = deduplicateElementsFromPairs(similarArtifactElements);

        removeExcludedElements(similarArtifactElementsWithoutDuplicates, excludedElements);
        return similarArtifactElementsWithoutDuplicates.subList(
                0, Math.min(maxResults, similarArtifactElementsWithoutDuplicates.size()));
    }

    /**
     * Deduplicates elements from a list of pairs, keeping only the first occurrence
     * of each element based on its id.
     *
     * @param similarElementPairs List of pairs containing elements and their similarity scores.
     * @return A list of pairs with unique elements preserving the input order.
     */
    private List<Pair<Element, Float>> deduplicateElementsFromPairs(List<Pair<Element, Float>> similarElementPairs) {
        var elementToSimilarity = new LinkedHashMap<Element, Float>();
        for (var elementPair : similarElementPairs) {
            if (elementToSimilarity.containsKey(elementPair.first())) {
                continue; // Only consider the first occurrence of each artifact
            }
            elementToSimilarity.put(elementPair.first(), elementPair.second());
        }
        var uniqueElementPairs = new ArrayList<Pair<Element, Float>>();
        for (var artifact : elementToSimilarity.sequencedKeySet()) {
            uniqueElementPairs.add(new Pair<>(artifact, elementToSimilarity.get(artifact)));
        }
        return uniqueElementPairs;
    }

    /**
     * Calculates the cosine similarity between a query vector and a list of target element vectors.
     *
     * @param query   The query element and its vector representation.
     * @param targets The list of target elements and their vector representations.
     * @return A list of pairs containing elements and their similarity scores to the query.
     */
    private List<Pair<Element, Float>> calculateSimilarity(
            Pair<Element, float[]> query, List<Pair<Element, float[]>> targets) {
        var similarElements = new ArrayList<Pair<Element, Float>>();
        for (var element : targets) {
            float[] elementVector = element.second();
            float similarity =
                    (float) CosineSimilarity.between(new Embedding(query.second()), new Embedding(elementVector));
            similarElements.add(new Pair<>(element.first(), similarity));
        }
        return similarElements;
    }
}
