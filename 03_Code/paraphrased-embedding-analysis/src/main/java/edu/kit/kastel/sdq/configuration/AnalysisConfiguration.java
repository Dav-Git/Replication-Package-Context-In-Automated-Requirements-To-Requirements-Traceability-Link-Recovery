/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.kit.kastel.sdq.utils.KeyGenerator;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;

@RecordBuilder()
public record AnalysisConfiguration(
        /*
         * Directory for caching intermediate results.
         */
        @JsonProperty("cache_dir") String cacheDir,

        /*
         * Name of the cache to use for vector space analysis.
         */
        @JsonProperty("cache_name") String cacheName
) implements ConfigurationBuilder.With {
    public static final String CONFIG_NAME_SEPARATOR = "_";
    /**
     * Serializes this configuration to JSON and finalizes all module configurations.
     * This method should be called before saving the configuration to ensure all
     * module configurations are properly finalized.
     *
     * @return A JSON string representation of this configuration
     * @throws UncheckedIOException If the configuration cannot be serialized
     */
    public String serializeAndDestroyConfiguration() throws UncheckedIOException {
        try {
            return new ObjectMapper()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Returns a string representation of this configuration.
     * The string includes all module configurations except the cache directory
     * and gold standard configuration.
     *
     * @return A string representation of this configuration
     */
    @Override
    public String toString() {
        return "Configuration{" + cacheName +"}";
    }

    /**
     * Generates a unique identifier for this configuration.
     * The identifier is created by combining the given prefix with a hash of
     * the configuration's string representation.
     *
     * @param prefix The prefix to use for the identifier
     * @return A unique identifier for this configuration
     * @throws NullPointerException If prefix is null
     */
    public String getConfigurationIdentifierForFile(String prefix) {
        return Objects.requireNonNull(prefix) + "_" + KeyGenerator.generateKey(this.toString());
    }

    /**
     * @return 
     */
    @Override
    public GoldStandardConfiguration goldStandardConfiguration() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration sourceArtifactProvider() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration targetArtifactProvider() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration sourcePreprocessor() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration targetPreprocessor() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration embeddingCreator() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration sourceStore() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration targetStore() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration classifier() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public List<List<ModuleConfiguration>> classifiers() {
        return List.of();
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration resultAggregator() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public ModuleConfiguration traceLinkIdPostprocessor() {
        return null;
    }
}
