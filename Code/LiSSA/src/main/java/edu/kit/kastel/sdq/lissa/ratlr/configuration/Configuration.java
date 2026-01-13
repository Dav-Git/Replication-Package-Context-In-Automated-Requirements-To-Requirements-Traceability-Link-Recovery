/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Configuration {
    /**
     * Error message thrown when attempting to access arguments after finalization.
     */
    protected static final String ALREADY_FINALIZED_FOR_SERIALIZATION =
            "Configuration already finalized for serialization";

    /**
     * The name of the module.
     */
    @JsonProperty("name")
    protected final String name;

    /**
     * Flag indicating whether this configuration has been finalized for serialization.
     */
    @JsonIgnore
    protected boolean finalized = false;

    protected Configuration(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the module.
     *
     * @return The module name
     */
    public String name() {
        return name;
    }

    abstract void finalizeForSerialization();
}
