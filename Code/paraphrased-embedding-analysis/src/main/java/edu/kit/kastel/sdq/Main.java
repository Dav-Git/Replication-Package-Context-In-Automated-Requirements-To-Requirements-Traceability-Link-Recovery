package edu.kit.kastel.sdq;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.kastel.sdq.artifactprovider.ArtifactProvider;
import edu.kit.kastel.sdq.cache.CacheManager;
import edu.kit.kastel.sdq.configuration.Configuration;
import edu.kit.kastel.sdq.context.ContextStore;
import edu.kit.kastel.sdq.elementstore.strategy.CosineSimilarity;
import edu.kit.kastel.sdq.embeddingcreator.EmbeddingCreator;
import edu.kit.kastel.sdq.knowledge.Artifact;
import edu.kit.kastel.sdq.knowledge.Element;
import edu.kit.kastel.sdq.preprocessor.Preprocessor;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        var configFilePath = args.length == 0 ? "config.json" : args[0];
        var configFile = new File(configFilePath);
        Configuration configuration = new ObjectMapper().readValue(configFile, Configuration.class);
        CacheManager.setCacheDir(configuration.cacheDir());

        ContextStore contextStore = new ContextStore();

        ArtifactProvider artifactProvider = ArtifactProvider.createArtifactProvider(configuration.sourceArtifactProvider(), contextStore);

        Preprocessor preprocessor = Preprocessor.createPreprocessor(configuration.sourcePreprocessor(), contextStore);

        EmbeddingCreator embeddingCreator = EmbeddingCreator.createEmbeddingCreator(configuration.embeddingCreator(), contextStore);

        System.out.println("Loading Artifacts");
        List<Artifact> artifacts = artifactProvider.getArtifacts();
        System.out.println("Preprocessing");
        List<Element> elements = preprocessor.preprocess(artifacts);
        System.out.println("Creating embeddings");
        List<Element> embeddings = embeddingCreator.calculateEmbeddings(elements);


        int c = 0;
        var similarities = new HashMap<Set<Element>, Double>();
        for (Element element1 : embeddings) {
            if (configuration.sourcePreprocessor().name().contains("paraphrase")){
                if (element1.getGranularity() == 0) continue;
            }
            for (Element element2 : embeddings) {
                if (configuration.sourcePreprocessor().name().contains("paraphrase")){
                    if (element2.getGranularity() == 0) continue;
                }
                if (element1.getIdentifier().equals(element2.getIdentifier())) continue;
                Set<Element> comparisonSet = Set.of(element1, element2);
                if (similarities.containsKey(comparisonSet)) continue;
                var vec1 = element1.getEmbedding();
                var vec2 = element2.getEmbedding();
                var sim = CosineSimilarity.cosineSimilarity(vec1, vec2);
                if (sim == 1.0) {
                    System.out.println(element1.getIdentifier() + " " + element2.getIdentifier());
                    System.out.println("Vectors are identical.");
                    try {
                        System.out.println(element1.getParent().getContent());
                    } catch (NullPointerException _) {}
                    System.out.println(element1.getContent());
                    System.out.println(element2.getContent());
                }
                if (c < 200) { // Just to give some indication of progress.
                    System.out.print(".");
                    c++;
                } else {
                    System.out.println(".");
                    c = 0;
                }
                similarities.put(comparisonSet, sim);
            }
        }

        System.out.println("\nCalculating statistics");
        Double max = similarities.values().stream().max(Double::compareTo).orElse(Double.NaN);
        Long identicalVectors = similarities.values().stream().filter(d -> d == 1.0).count();
        Double min = similarities.values().stream().min(Double::compareTo).orElse(Double.NaN);
        Double avg = similarities.values().stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

        writeResults(configuration,similarities,max,identicalVectors,min,avg);
    }

    private static void writeResults(Configuration configuration, HashMap<Set<Element>,Double> similarities, Double max, Long identicalVectors,Double min, Double avg) throws IOException {
        // Generate a histogram of similarity values
        int bins = 100;
        double[] similarityArray = similarities.values().stream()
                .filter(d -> !d.isNaN())
                .mapToDouble(Double::doubleValue)
                .toArray();

        var modelName = configuration.sourcePreprocessor().argumentAsString("model","");
        var n = configuration.sourcePreprocessor().argumentAsInt("count");
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Similarities", similarityArray, bins, min, max);
        JFreeChart histogramChart = ChartFactory.createHistogram(
                "model=%s, n=%s".formatted(modelName,n),
                "Similarity",
                "Amount",
                dataset
        );
        Font titleFont = new Font("SansSerif", Font.BOLD, 42);
        Font restFont = new Font("SansSerif", Font.PLAIN, 32);
        histogramChart.getTitle().setFont(titleFont);
        XYPlot plot = histogramChart.getXYPlot();
        plot.getDomainAxis().setLabelFont(titleFont);
        plot.getDomainAxis().setTickLabelFont(titleFont);
        plot.getRangeAxis().setLabelFont(titleFont);
        plot.getRangeAxis().setTickLabelFont(titleFont);
        histogramChart.removeLegend();

        var result = new StringBuilder();
        System.out.println("Writing");
        result.append("## Config:\n```json\n");
        result.append(configuration.serializeAndDestroyConfiguration());
        result.append("\n```\n");
        result.append("## Similarities:\n");
        result.append("Max similarity: ").append(max).append(" \\\n");
        result.append("Identical vectors: ").append(identicalVectors).append(" \\\n");
        result.append("Min similarity: ").append(min).append(" \\\n");
        result.append("Range: ").append(max - min).append(" \\\n");
        result.append("Average similarity: ").append(avg).append(" \n");

        // Determine result filenames
        var cacheDir = configuration.cacheDir().split("/");
        var cacheName = cacheDir[cacheDir.length - 1];
        // Write Histogram
        File pngFile = new File("analysis-" + cacheName + modelName+"-similarities"+n+".png");
        ChartUtils.saveChartAsPNG(pngFile, histogramChart, 1900, 1200);
        result.append("![Histogram](").append(pngFile.getPath()).append(")");

        result.append("\n## Identical vectors:\n");
        similarities.entrySet().stream()
                .filter(entry -> entry.getValue() == 1.0)
                .forEach(entry -> {
                    Element e1 = entry.getKey().stream().findFirst().orElseThrow();
                    Element e2 = entry.getKey().stream().skip(1).findFirst().orElseThrow();
                    result.append("- ");
                    System.out.println(e1.getIdentifier() + " " + e2.getIdentifier());
                    if (e1.getIdentifier().split("\\.")[0].equals(e2.getIdentifier().split("\\.")[0])) {
                        result.append("! ");
                    }
                    result.append(e1.getIdentifier().replace("$paraphrased$","p").replace("$copy","c"));
                    result.append(" and ").append(e2.getIdentifier().replace("$paraphrased$", "p").replace("$copy", "c"));
                    result.append("\n");
                });

        var paraphrasedEquivalence = StringUtils.countMatches(result.toString(),"!");
        //                                      -1 because one ! is used to embed the histogram
        result.append("### Of which ").append(paraphrasedEquivalence-1).append(" are equivalent paraphrases.\n");
        // Write md results
        var resultfile = new File("analysis-" + cacheName + modelName+ "-similarities"+n+".md");
        try {
            Files.writeString(resultfile.toPath(), result.toString(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        System.out.println("Results written to " + resultfile.getAbsolutePath());
    }
}