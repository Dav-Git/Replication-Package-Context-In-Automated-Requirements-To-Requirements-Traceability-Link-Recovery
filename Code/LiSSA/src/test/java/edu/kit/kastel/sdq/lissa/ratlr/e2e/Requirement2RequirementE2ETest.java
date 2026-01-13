/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.e2e;

import edu.kit.kastel.mcse.ardoco.metrics.ClassificationMetricsCalculator;
import edu.kit.kastel.sdq.lissa.ratlr.Evaluation;
import edu.kit.kastel.sdq.lissa.ratlr.goldstandard.GoldStandard;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.TraceLink;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

class Requirement2RequirementE2ETest {

    @BeforeEach
    void setUp() throws IOException {
        File envFile = new File(".env");
        if (!envFile.exists() && System.getenv("CI") != null) {
            Files.writeString(
                    envFile.toPath(),
                    """
OLLAMA_EMBEDDING_HOST=http://localhost:11434
OLLAMA_HOST=http://localhost:11434
OPENAI_ORGANIZATION_ID=DUMMY
OPENAI_API_KEY=sk-DUMMY
""");
        }
    }

    @Test
    void testEnd2End() throws Exception {
        File config = new File("src/test/resources/warc/config.json");
        Assertions.assertTrue(config.exists());

        Evaluation evaluation = new Evaluation(config.toPath());
        var traceLinks = evaluation.run();

        GoldStandard referenceGoldStandard =
                new GoldStandard(evaluation.getConfiguration().goldStandardConfiguration());
        referenceGoldStandard.load();
        Set<TraceLink> validTraceLinks = referenceGoldStandard.getTraceLinks();

        ClassificationMetricsCalculator cmc = ClassificationMetricsCalculator.getInstance();
        var classification = cmc.calculateMetrics(traceLinks, validTraceLinks, null);
        classification.prettyPrint();
        Assertions.assertEquals(0.38, classification.getPrecision(), 1E-8);
        Assertions.assertEquals(0.6985294117647058, classification.getRecall(), 1E-8);
        Assertions.assertEquals(0.49222797927461137, classification.getF1(), 1E-8);
    }
}
