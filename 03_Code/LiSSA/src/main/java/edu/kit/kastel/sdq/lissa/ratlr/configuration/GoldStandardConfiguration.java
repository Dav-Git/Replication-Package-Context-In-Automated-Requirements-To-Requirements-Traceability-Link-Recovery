/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Represents the configuration for gold standard evaluation in trace link analysis.
 * This record contains settings for loading and parsing gold standard data from a file.
 * The gold standard file is expected to be a CSV file containing trace links between
 * source and target elements.
 */
public final class GoldStandardConfiguration extends Configuration {

    /**
     * Path to the gold standard file.
     * The file should be a CSV file containing trace links.
     */
    @JsonProperty("path")
    private final String path;

    /**
     * Whether the gold standard file has a header row.
     * If true, the first row will be skipped during parsing.
     */
    @JsonProperty(defaultValue = "false")
    private final boolean hasHeader;

    /**
     * Whether to swap the source and target columns when reading the file.
     * This is useful when the gold standard file has columns in a different order
     * than expected by the system.
     */
    @JsonProperty(value = "swap_columns", defaultValue = "false")
    private final boolean swapColumns;

    /**
     *
     */
    public GoldStandardConfiguration(
            @JsonProperty("path") String path,
            @JsonProperty(value = "hasHeader", defaultValue = "false") boolean hasHeader,
            @JsonProperty(value = "swap_columns", defaultValue = "false") boolean swapColumns) {
        super("gold_standard");
        this.path = path;
        this.hasHeader = hasHeader;
        this.swapColumns = swapColumns;
    }

    /**
     * Loads a gold standard configuration from a JSON file.
     * If the file cannot be loaded or is null, returns null.
     *
     * @param evaluationConfig Path to the JSON configuration file
     * @return A new GoldStandardConfiguration instance, or null if loading fails
     */
    public static @Nullable GoldStandardConfiguration load(@Nullable Path evaluationConfig) {
        if (evaluationConfig == null) return null;

        try {
            return new ObjectMapper().readValue(evaluationConfig.toFile(), GoldStandardConfiguration.class);
        } catch (IOException e) {
            LoggerFactory.getLogger(GoldStandardConfiguration.class)
                    .error("Loading evaluation config threw an exception: {}", e.getMessage());
            return null;
        }
    }

    public String path() {
        return path;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public boolean swapColumns() {
        return swapColumns;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GoldStandardConfiguration) obj;
        return Objects.equals(this.path, that.path)
                && this.hasHeader == that.hasHeader
                && this.swapColumns == that.swapColumns;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, hasHeader, swapColumns);
    }

    @Override
    public String toString() {
        return "GoldStandardConfiguration[" + "path="
                + path + ", " + "hasHeader="
                + hasHeader + ", " + "swapColumns="
                + swapColumns + ']';
    }

    @Override
    void finalizeForSerialization() {
        throw new NotImplementedException("GoldStandardConfiguration does not support serialization.");
    }
}
