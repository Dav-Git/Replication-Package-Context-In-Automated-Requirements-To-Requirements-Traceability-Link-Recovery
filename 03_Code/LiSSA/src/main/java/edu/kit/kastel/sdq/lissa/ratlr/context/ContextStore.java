/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.context;

import edu.kit.kastel.sdq.lissa.ratlr.configuration.ContextConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.loaders.ContextLoader;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Stores and manages {@link Context} objects for the LiSSA pipeline.
 * The {@code ContextStore} acts as a registry for context objects that can be shared
 * across different pipeline components (e.g., artifact providers, preprocessors, classifiers).
 * Contexts are identified by unique IDs and can be retrieved by type.
 */
public class ContextStore {
    private final SortedMap<String, Context> contexts = new TreeMap<>();
    private final List<ContextLoader> contextLoaders = new ArrayList<>();

    public ContextStore(@Nullable List<ContextConfiguration> contextConfigurations) {
        if (contextConfigurations != null) {
            for (var contextConfiguration : contextConfigurations) {
                contextLoaders.add(ContextLoader.createContextLoader(contextConfiguration, this));
            }
        }
    }

    /**
     * Registers a new context in the store.
     *
     * @param context the context to add
     * @throws IllegalArgumentException if a context with the same ID already exists
     */
    public void createContext(Context context) {
        if (contexts.containsKey(context.getId())) {
            throw new IllegalArgumentException("Context already exists: %s".formatted(context.getId()));
        }
        contexts.put(context.getId(), context);
    }

    /**
     * Loads all contexts using their respective loaders.
     */
    public void loadContexts() {
        if (contextLoaders.isEmpty()) {
            return;
        }
        for (var contextLoader : contextLoaders) {
            contextLoader.loadContext();
        }
    }

    /**
     * Checks if a context with the given ID exists in the store.
     *
     * @param id the context ID
     * @return true if the context exists, false otherwise
     */
    public boolean hasContext(String id) {
        return contexts.containsKey(id);
    }

    /**
     * Retrieves a context by ID and type.
     *
     * @param id the context ID
     * @param contextType the expected type of the context
     * @return the context instance if found and of the correct type, or null if not found
     * @throws IllegalArgumentException if the context exists but is not of the requested type
     */
    @SuppressWarnings("unchecked")
    public <C> @Nullable C getContext(String id, Class<C> contextType) {
        if (!contexts.containsKey(id)) {
            return null;
        }
        Context context = contexts.get(id);
        if (contextType.isInstance(context)) {
            return (C) context;
        }
        throw new IllegalArgumentException(String.format("Context %s is not of type %s", id, contextType.getName()));
    }
}
