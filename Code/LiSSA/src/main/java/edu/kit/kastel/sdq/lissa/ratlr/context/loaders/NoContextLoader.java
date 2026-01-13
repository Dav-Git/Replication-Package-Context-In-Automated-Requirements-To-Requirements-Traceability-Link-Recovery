/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.context.loaders;

import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;

/**
 * A dummy context loader that does not load any context.
 */
public class NoContextLoader extends ContextLoader {

    public NoContextLoader(String contextId, ContextStore contextStore) {
        super(contextId, contextStore);
    }

    @Override
    public void loadContext() {
        // No context to load
    }
}
