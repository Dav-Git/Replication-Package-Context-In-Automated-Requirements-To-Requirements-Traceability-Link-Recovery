/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.context.loaders;

import edu.kit.kastel.sdq.lissa.ratlr.configuration.ContextConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;

/**
 * Abstract base class for context loaders in the LiSSA framework.
 * Context loaders are responsible for loading and preparing context data
 * that can be used by other components in the traceability pipeline.
 */
public abstract class ContextLoader {

    protected final String contextId;
    protected final ContextStore contextStore;

    protected ContextLoader(String contextId, ContextStore contextStore) {
        this.contextId = contextId;
        this.contextStore = contextStore;
    }

    /**
     * Creates a ContextLoader based on the provided configuration.
     *
     * @param configuration The context configuration specifying the type of loader and its modules
     * @param contextStore  The context store to be used by the loader
     * @return An instance of ContextLoader as specified by the configuration
     */
    public static ContextLoader createContextLoader(ContextConfiguration configuration, ContextStore contextStore) {

        String contextId = configuration.getContextId();

        return switch (configuration.name()) {
            case "none" -> new NoContextLoader(contextId, contextStore);
            case "embeddings" -> new EmbeddingsContextLoader(contextId, contextStore, configuration);
            case "external_trace_links" -> new ExternalTraceLinksContextLoader(contextId, contextStore, configuration);
            default -> throw new IllegalArgumentException("Unknown context loader type: " + configuration.name());
        };
    }

    /**
     * Loads context into this loader`s context store.
     */
    public abstract void loadContext();
}
