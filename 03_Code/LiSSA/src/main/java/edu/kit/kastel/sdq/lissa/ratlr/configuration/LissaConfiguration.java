/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.kit.kastel.sdq.lissa.ratlr.classifier.Classifier;
import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;
import edu.kit.kastel.sdq.lissa.ratlr.utils.KeyGenerator;
import org.jspecify.annotations.Nullable;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;

/**
 * Represents the complete configuration for a trace link analysis run.
 * This record contains all necessary configurations for artifact providers,
 * preprocessors, embedding creators, stores, classifiers, and postprocessors.
 * It supports both single-classifier and multi-stage classifier configurations.
 * <p>
 * The configuration is used to instantiate pipeline components, each of which can access shared context
 * via a {@link ContextStore} passed to their factory methods.
 * </p>
 */
public final class LissaConfiguration extends Configuration {
    /**
     * Directory for caching intermediate results.
     */
    @JsonProperty("cache_dir")
    private final String cacheDir;

    /**
     * Configuration for gold standard evaluation.
     */
    @JsonProperty("gold_standard_configuration")
    private final GoldStandardConfiguration goldStandardConfiguration;

    /**
     * Configuration for the contexts (ContextLoaders) used in the pipeline.
     *
     * @see edu.kit.kastel.sdq.lissa.ratlr.context.loaders.ContextLoader
     */
    @JsonProperty("context_configurations")
    private final @Nullable List<ContextConfiguration> contextConfigurations;

    /**
     * Configuration for the source artifact provider.
     */
    @JsonProperty("source_artifact_provider")
    private final ModuleConfiguration sourceArtifactProvider;

    /**
     * Configuration for the target artifact provider.
     */
    @JsonProperty("target_artifact_provider")
    private final ModuleConfiguration targetArtifactProvider;

    /**
     * Configuration for the source artifact preprocessor.
     */
    @JsonProperty("source_preprocessor")
    private final ModuleConfiguration sourcePreprocessor;

    /**
     * Configuration for the target artifact preprocessor.
     */
    @JsonProperty("target_preprocessor")
    private final ModuleConfiguration targetPreprocessor;

    /**
     * Configuration for the embedding creator.
     */
    @JsonProperty("embedding_creator")
    private final ModuleConfiguration embeddingCreator;

    /**
     * Configuration for the source element store.
     */
    @JsonProperty("source_store")
    private final ModuleConfiguration sourceStore;

    /**
     * Configuration for the target element store.
     */
    @JsonProperty("target_store")
    private final ModuleConfiguration targetStore;

    /**
     * Configuration for a single classifier.
     * Either this or {@link #classifiers} must be set, but not both.
     */
    @JsonProperty("classifier")
    private final @Nullable ModuleConfiguration classifier;

    /**
     * Configuration for a multi-stage classifier pipeline.
     * Either this or {@link #classifier} must be set, but not both.
     */
    @JsonProperty("classifiers")
    private final @Nullable List<List<ModuleConfiguration>> classifiers;

    /**
     * Configuration for the result aggregator.
     */
    @JsonProperty("result_aggregator")
    private final ModuleConfiguration resultAggregator;

    /**
     * Configuration for the trace link ID postprocessor.
     */
    @JsonProperty("tracelinkid_postprocessor")
    private final @Nullable ModuleConfiguration traceLinkIdPostprocessor;

    /**
     *
     */
    public LissaConfiguration(
            @JsonProperty("cache_dir") String cacheDir,
            @JsonProperty("gold_standard_configuration") GoldStandardConfiguration goldStandardConfiguration,
            @JsonProperty("context_configurations") @Nullable List<ContextConfiguration> contextConfigurations,
            @JsonProperty("source_artifact_provider") ModuleConfiguration sourceArtifactProvider,
            @JsonProperty("target_artifact_provider") ModuleConfiguration targetArtifactProvider,
            @JsonProperty("source_preprocessor") ModuleConfiguration sourcePreprocessor,
            @JsonProperty("target_preprocessor") ModuleConfiguration targetPreprocessor,
            @JsonProperty("embedding_creator") ModuleConfiguration embeddingCreator,
            @JsonProperty("source_store") ModuleConfiguration sourceStore,
            @JsonProperty("target_store") ModuleConfiguration targetStore,
            @JsonProperty("classifier") @Nullable ModuleConfiguration classifier,
            @JsonProperty("classifiers") @Nullable List<List<ModuleConfiguration>> classifiers,
            @JsonProperty("result_aggregator") ModuleConfiguration resultAggregator,
            @JsonProperty("tracelinkid_postprocessor") @Nullable ModuleConfiguration traceLinkIdPostprocessor) {
        super("LiSSA");
        this.cacheDir = cacheDir;
        this.goldStandardConfiguration = goldStandardConfiguration;
        this.contextConfigurations = contextConfigurations;
        this.sourceArtifactProvider = sourceArtifactProvider;
        this.targetArtifactProvider = targetArtifactProvider;
        this.sourcePreprocessor = sourcePreprocessor;
        this.targetPreprocessor = targetPreprocessor;
        this.embeddingCreator = embeddingCreator;
        this.sourceStore = sourceStore;
        this.targetStore = targetStore;
        this.classifier = classifier;
        this.classifiers = classifiers;
        this.resultAggregator = resultAggregator;
        this.traceLinkIdPostprocessor = traceLinkIdPostprocessor;
    }

    @Override
    void finalizeForSerialization() {
        if (this.finalized) {
            return;
        }
        if (contextConfigurations != null) {
            for (var contextConfiguration : contextConfigurations) {
                contextConfiguration.finalizeForSerialization();
            }
        }
        sourceArtifactProvider.finalizeForSerialization();
        targetArtifactProvider.finalizeForSerialization();
        sourcePreprocessor.finalizeForSerialization();
        targetPreprocessor.finalizeForSerialization();
        embeddingCreator.finalizeForSerialization();
        sourceStore.finalizeForSerialization();
        targetStore.finalizeForSerialization();
        if (classifier != null) {
            classifier.finalizeForSerialization();
        }
        if (classifiers != null) {
            for (var group : classifiers) {
                for (var classifier : group) {
                    classifier.finalizeForSerialization();
                }
            }
        }
        resultAggregator.finalizeForSerialization();
        if (traceLinkIdPostprocessor != null) {
            traceLinkIdPostprocessor.finalizeForSerialization();
        }
        this.finalized = true;
    }

    /**
     * Serializes this configuration to JSON and finalizes all module configurations.
     * This method should be called before saving the configuration to ensure all
     * module configurations are properly finalized.
     *
     * @return A JSON string representation of this configuration
     * @throws UncheckedIOException If the configuration cannot be serialized
     */
    public String serializeAndDestroyConfiguration() throws UncheckedIOException {
        finalizeForSerialization();
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
     * and gold standard configuration. Used for naming the output files.
     *
     * @return A string representation of this configuration
     */
    @Override
    public String toString() {
        return "LissaConfiguration{" + "contextConfigurations="
                + contextConfigurations + ", sourceArtifactProvider="
                + sourceArtifactProvider + ", targetArtifactProvider="
                + targetArtifactProvider + ", sourcePreprocessor="
                + sourcePreprocessor + ", targetPreprocessor="
                + targetPreprocessor + ", embeddingCreator="
                + embeddingCreator + ", sourceStore="
                + sourceStore + ", targetStore="
                + targetStore + ", classifier="
                + classifier + ", classifiers="
                + classifiers + ", resultAggregator="
                + resultAggregator + ", traceLinkIdPostprocessor="
                + traceLinkIdPostprocessor + '}';
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
     * Creates a classifier instance based on this configuration.
     * Either a single classifier or a multi-stage classifier pipeline is created,
     * depending on which configuration is set. The shared {@link ContextStore} is passed to all classifiers.
     *
     * @param contextStore The shared context store for pipeline components
     * @return A classifier instance
     * @throws IllegalStateException If neither or both classifier configurations are set
     */
    public Classifier createClassifier(ContextStore contextStore) {
        if ((classifier == null) == (classifiers == null)) {
            throw new IllegalStateException("Either 'classifier' or 'classifiers' must be set, but not both.");
        }

        return classifier != null
                ? Classifier.createClassifier(classifier, contextStore)
                : Classifier.createMultiStageClassifier(classifiers, contextStore);
    }

    public String cacheDir() {
        return cacheDir;
    }

    public GoldStandardConfiguration goldStandardConfiguration() {
        return goldStandardConfiguration;
    }

    @Nullable
    public List<ContextConfiguration> contextConfigurations() {
        return contextConfigurations;
    }

    public ModuleConfiguration sourceArtifactProvider() {
        return sourceArtifactProvider;
    }

    public ModuleConfiguration targetArtifactProvider() {
        return targetArtifactProvider;
    }

    public ModuleConfiguration sourcePreprocessor() {
        return sourcePreprocessor;
    }

    public ModuleConfiguration targetPreprocessor() {
        return targetPreprocessor;
    }

    public ModuleConfiguration embeddingCreator() {
        return embeddingCreator;
    }

    public ModuleConfiguration sourceStore() {
        return sourceStore;
    }

    public ModuleConfiguration targetStore() {
        return targetStore;
    }

    public @Nullable ModuleConfiguration classifier() {
        return classifier;
    }

    public @Nullable List<List<ModuleConfiguration>> classifiers() {
        return classifiers;
    }

    public ModuleConfiguration resultAggregator() {
        return resultAggregator;
    }

    public @Nullable ModuleConfiguration traceLinkIdPostprocessor() {
        return traceLinkIdPostprocessor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LissaConfiguration) obj;
        return Objects.equals(this.cacheDir, that.cacheDir)
                && Objects.equals(this.goldStandardConfiguration, that.goldStandardConfiguration)
                && Objects.equals(this.contextConfigurations, that.contextConfigurations)
                && Objects.equals(this.sourceArtifactProvider, that.sourceArtifactProvider)
                && Objects.equals(this.targetArtifactProvider, that.targetArtifactProvider)
                && Objects.equals(this.sourcePreprocessor, that.sourcePreprocessor)
                && Objects.equals(this.targetPreprocessor, that.targetPreprocessor)
                && Objects.equals(this.embeddingCreator, that.embeddingCreator)
                && Objects.equals(this.sourceStore, that.sourceStore)
                && Objects.equals(this.targetStore, that.targetStore)
                && Objects.equals(this.classifier, that.classifier)
                && Objects.equals(this.classifiers, that.classifiers)
                && Objects.equals(this.resultAggregator, that.resultAggregator)
                && Objects.equals(this.traceLinkIdPostprocessor, that.traceLinkIdPostprocessor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                cacheDir,
                goldStandardConfiguration,
                contextConfigurations,
                sourceArtifactProvider,
                targetArtifactProvider,
                sourcePreprocessor,
                targetPreprocessor,
                embeddingCreator,
                sourceStore,
                targetStore,
                classifier,
                classifiers,
                resultAggregator,
                traceLinkIdPostprocessor);
    }
}
