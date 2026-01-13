/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.elementstore;

import edu.kit.kastel.sdq.lissa.ratlr.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.elementstore.strategy.RetrievalStrategy;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.Element;
import edu.kit.kastel.sdq.lissa.ratlr.utils.Pair;

import java.util.List;

/**
 * A store for target elements and their embeddings in the LiSSA framework.
 * Providing functionality for similarity search and element retrieval
 * <b>Target Store</b> (similarityRetriever = true):
 * <ul>
 *      <li>Used to store target elements that will be searched for similarity in LiSSA's classification phase</li>
 *      <li>Cannot retrieve all elements at once</li>
 * </ul>
 */
public class TargetElementStore extends ElementStore {

    /**
     * Strategy to find similar elements.
     */
    private final RetrievalStrategy retrievalStrategy;

    public TargetElementStore(ModuleConfiguration moduleConfiguration) {
        super();
        this.retrievalStrategy = RetrievalStrategy.createStrategy(moduleConfiguration);
    }

    public TargetElementStore(List<Pair<Element, float[]>> content, RetrievalStrategy retrievalStrategy) {
        super(content);
        this.retrievalStrategy = retrievalStrategy;
    }

    public TargetElementStore(ModuleConfiguration moduleConfiguration, String contextId) {
        super(contextId);
        this.retrievalStrategy = RetrievalStrategy.createStrategy(moduleConfiguration);
    }

    /**
     * Retrieves the retrieval strategy used for finding similar elements.
     *
     * @return The retrieval strategy
     */
    public RetrievalStrategy getRetrievalStrategy() {
        return retrievalStrategy;
    }

    /**
     * Retrieves all elements in the store without their embeddings.
     * This method is primarily used for batch processing operations that only need element metadata,
     * such as reducing the target store scope.
     *
     * @return List of all elements (without embeddings)
     */
    public List<Element> getAllElements() {
        return getAllElementsIntern(false).stream().map(Pair::first).toList();
    }

    /**
     * Finds elements similar to the given query vector as part of LiSSA's similarity matching.
     *
     * @param query The element and vector to find similar elements for
     * @return List of similar elements, sorted by similarity
     */
    public final List<Element> findSimilar(Pair<Element, float[]> query) {
        return findSimilarWithDistances(query).stream().map(Pair::first).toList();
    }

    /**
     * Finds elements similar to the given query vector as part of LiSSA's similarity matching.
     * This variant allows excluding specific elements from the results.
     * Only available in target store mode.
     *
     * @param query            The element and vector to find similar elements for
     * @param excludedElements List of elements to exclude from the results
     * @return List of similar elements, sorted by similarity
     * @throws IllegalStateException If this is a source store (similarityRetriever = false)
     */
    public final List<Element> findSimilar(Pair<Element, float[]> query, List<Element> excludedElements) {
        return findSimilarWithDistances(query, excludedElements).stream()
                .map(Pair::first)
                .toList();
    }


    /**
     * Finds elements similar to the given query vector, including their similarity scores.
     * Used by LiSSA for similarity-based matching in the classification phase.
     *
     * @param query The element and vector to find similar elements for
     * @return List of pairs containing similar elements and their similarity scores
     */
    public List<Pair<Element, Float>> findSimilarWithDistances(Pair<Element, float[]> query) {
        return retrievalStrategy.findSimilarElements(query, getAllElementsIntern(true));
    }

    /**
     * Finds elements similar to the given query vector, including their similarity scores.
     * This variant allows excluding specific elements from the results.
     * Used by LiSSA for similarity-based matching in the classification phase.
     * Only available in target store mode.
     *
     * @param query            The element and vector to find similar elements for
     * @param excludedElements List of elements to exclude from the results
     * @return List of pairs containing similar elements and their similarity scores
     * @throws IllegalStateException If this is a source store (similarityRetriever = false)
     */
    public List<Pair<Element, Float>> findSimilarWithDistances(
            Pair<Element, float[]> query, List<Element> excludedElements) {
        return retrievalStrategy.findSimilarElements(query, getAllElementsIntern(true), excludedElements);
    }

}
