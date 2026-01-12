/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.context.loaders;

import edu.kit.kastel.sdq.lissa.ratlr.configuration.ContextConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;
import edu.kit.kastel.sdq.lissa.ratlr.goldstandard.GoldStandard;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.TraceLink;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExternalTraceLinksContextLoaderTest {

    private static ContextConfiguration createExternalTraceLinksContextConfig(String contextId, Path goldStandardCsv) {
        ModuleConfiguration goldStandardModule = new ModuleConfiguration(
                "gold_standard",
                Map.of(
                        "path", goldStandardCsv.toString(),
                        "hasHeader", "true"));

        return new ContextConfiguration(
                "external_trace_links",
                contextId,
                List.of(Map.of(AllowableContextModuleTypes.GOLD_STANDARD, goldStandardModule)));
    }

    @Test
    void loadContextRegistersGoldStandardAndLoadsTraceLinks() {
        Path goldStandard = Path.of("src/test/resources/warc/answer.csv");
        assertTrue(goldStandard.toFile().exists(), "Expected test gold standard to exist: " + goldStandard);

        String contextId = "external_trace_links";
        ContextStore contextStore = new ContextStore(List.of(createExternalTraceLinksContextConfig(contextId, goldStandard)));

        contextStore.loadContexts();

        assertTrue(contextStore.hasContext(contextId));
        GoldStandard gs = contextStore.getContext(contextId, GoldStandard.class);
        assertNotNull(gs);

        // Ensure it actually loaded something and parsing behavior matches known CSV content.
        assertFalse(gs.getTraceLinks().isEmpty());
        assertTrue(gs.contains(new TraceLink("FR01", "SRS01")));
        assertTrue(gs.contains(new TraceLink("FR02", "SRS68")));
    }

    @Test
    void loadContextThrowsIfContextAlreadyExists() {
        Path goldStandard = Path.of("src/test/resources/warc/answer.csv");
        assertTrue(goldStandard.toFile().exists(), "Expected test gold standard to exist: " + goldStandard);

        String contextId = "external_trace_links";
        ContextStore contextStore = new ContextStore(null);

        // Pre-create a context with the same ID.
        contextStore.createContext(new GoldStandard(new ModuleConfiguration(
                "gold_standard",
                Map.of(
                        "path", goldStandard.toString(),
                        "hasHeader", "true")), contextId));

        ContextLoader loader = ContextLoader.createContextLoader(
                createExternalTraceLinksContextConfig(contextId, goldStandard),
                contextStore);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, loader::loadContext);
        assertTrue(ex.getMessage().contains("Context already exists"));
    }
}

