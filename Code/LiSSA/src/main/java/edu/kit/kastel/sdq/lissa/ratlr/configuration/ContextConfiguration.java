/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.kit.kastel.sdq.lissa.ratlr.context.loaders.AllowableContextModuleTypes;

import java.util.List;
import java.util.Map;

public final class ContextConfiguration extends Configuration {

    @JsonProperty("context_id")
    private final String contextId;

    @JsonProperty("modules")
    private final List<Map<AllowableContextModuleTypes, ModuleConfiguration>> contextModuleConfigurations;

    @JsonCreator
    public ContextConfiguration(
            @JsonProperty("name") String name,
            @JsonProperty("context_id") String contextId,
            @JsonProperty("modules")
            List<Map<AllowableContextModuleTypes, ModuleConfiguration>> contextModuleConfigurations) {
        super(name);
        this.contextId = contextId;
        this.contextModuleConfigurations = contextModuleConfigurations;
    }

    public String getContextId() {
        return contextId;
    }

    public ModuleConfiguration getModuleConfiguration(AllowableContextModuleTypes moduleType, int index) {
        checkFinalized();
        var config = this.contextModuleConfigurations.get(index).get(moduleType);
        if (config != null) {
            return config;
        } else {
            throw new IllegalArgumentException("No module configuration found for modules of type: " + moduleType);
        }
    }

    @Override
    void finalizeForSerialization() {
        if (this.finalized) {
            return;
        }
        for (var contextModuleConfiguration : this.contextModuleConfigurations) {
            for (var module : contextModuleConfiguration.values()) {
                module.finalizeForSerialization();
            }
        }
        this.finalized = true;
    }

    private void checkFinalized() {
        if (finalized) {
            throw new IllegalStateException(ALREADY_FINALIZED_FOR_SERIALIZATION);
        }
    }

    @Override
    public String toString() {
        return "ContextConfiguration{" + "name='"
                + name + ", contextId='"
                + contextId + ", contextModuleConfigurationsByType="
                + contextModuleConfigurations + '}';
    }
}
