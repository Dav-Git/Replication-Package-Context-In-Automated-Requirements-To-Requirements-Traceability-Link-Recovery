/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.context.loaders;

import edu.kit.kastel.sdq.lissa.ratlr.configuration.ContextConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;
import edu.kit.kastel.sdq.lissa.ratlr.elementstore.ElementStore;
import edu.kit.kastel.sdq.lissa.ratlr.elementstore.TargetElementStore;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmbeddingsContextLoaderTest {

    private static ContextConfiguration createMinimalEmbeddingsContextConfig(String contextId, Path artifactsPath) {
        // NOTE: configuration objects are intentionally *not* finalized for serialization;
        // production code reads module configs via getters which require non-finalized state.
        ModuleConfiguration artifactProvider = new ModuleConfiguration(
                "text",
                Map.of(
                        "artifact_type", "requirement",
                        "path", artifactsPath.toString()));

        ModuleConfiguration preprocessor = new ModuleConfiguration("artifact", Map.of());

        // Deterministic, offline, fast.
        ModuleConfiguration embeddingCreator = new ModuleConfiguration("mock", Map.of());

        // TargetElementStore expects a RetrievalStrategy config. "custom" maps to cosine similarity.
        ModuleConfiguration elementStore = new ModuleConfiguration("custom", Map.of("max_results", "5"));

        return new ContextConfiguration(
                "embeddings",
                contextId,
                List.of(Map.of(
                        AllowableContextModuleTypes.ARTIFACT_PROVIDER, artifactProvider,
                        AllowableContextModuleTypes.PREPROCESSOR, preprocessor,
                        AllowableContextModuleTypes.EMBEDDING_CREATOR, embeddingCreator,
                        AllowableContextModuleTypes.ELEMENT_STORE, elementStore)));
    }

    @Test
    void loadContextRegistersTargetElementStoreWithElements() {
        Path requirementsDir = Path.of("src/test/resources/warc/low");
        assertTrue(requirementsDir.toFile().exists(), "Expected test dataset to exist: " + requirementsDir);

        String contextId = "target_requirements";
        ContextStore contextStore = new ContextStore(List.of(createMinimalEmbeddingsContextConfig(contextId, requirementsDir)));

        contextStore.loadContexts();

        assertTrue(contextStore.hasContext(contextId));
        ElementStore store = contextStore.getContext(contextId, ElementStore.class);
        assertNotNull(store);
        assertInstanceOf(TargetElementStore.class, store);

        // Sanity-check a couple of known files from the test resources.
        assertNotNull(store.getById("SRS01.txt"));
        assertNotNull(store.getById("SRS02.txt"));

        // MockEmbeddingCreator returns a vector of length 1.
        assertEquals(1, store.getById("SRS01.txt").second().length);
    }

    @Test
    void loadContextThrowsIfContextAlreadyExists() {
        Path requirementsDir = Path.of("src/test/resources/warc/low");
        assertTrue(requirementsDir.toFile().exists(), "Expected test dataset to exist: " + requirementsDir);

        String contextId = "target_requirements";
        ContextStore contextStore = new ContextStore(null);

        // Pre-create context with same id
        contextStore.createContext(new TargetElementStore(new ModuleConfiguration("custom", Map.of("max_results", "5")), contextId));

        ContextLoader loader = ContextLoader.createContextLoader(
                createMinimalEmbeddingsContextConfig(contextId, requirementsDir),
                contextStore);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, loader::loadContext);
        assertTrue(ex.getMessage().contains("Context already exists"));
    }
}

