/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.goldstandard;

import edu.kit.kastel.sdq.lissa.ratlr.configuration.GoldStandardConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.configuration.ModuleConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.Context;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.Element;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.TraceLink;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Set;
import java.util.stream.Collectors;

public class GoldStandard implements Context {

    private final String path;
    private final boolean hasHeader;
    private final boolean swapColumns;

    @Nullable
    private final String id;

    @Nullable
    private Set<TraceLink> traceLinks;

    public GoldStandard(GoldStandardConfiguration configuration) {
        this.path = configuration.path();
        this.hasHeader = configuration.hasHeader();
        this.swapColumns = configuration.swapColumns();
        this.id = null;
    }

    /**
     * Constructor to use GoldStandard as a Context.
     *
     * @param configuration Configuration of this gold standard
     * @param id ContextId of this gold standard
     */
    public GoldStandard(ModuleConfiguration configuration, @NonNull String id) {
        this.path = configuration.argumentAsString("path");
        this.hasHeader = configuration.argumentAsBoolean("hasHeader", false);
        this.swapColumns = configuration.argumentAsBoolean("swap_columns", false);
        this.id = id;
    }

    /**
     * Loads trace links from a gold standard file.
     * This method:
     * <ol>
     *     <li>Reads the gold standard file</li>
     *     <li>Skips header if configured</li>
     *     <li>Parses each line into a trace link</li>
     *     <li>Handles column swapping if configured</li>
     * </ol>
     *
     * @throws UncheckedIOException If there are issues reading the gold standard file
     */
    public void load() {
        File goldStandardFile = new File(path);
        Set<TraceLink> validTraceLinks;
        try {
            validTraceLinks = Files.readAllLines(goldStandardFile.toPath()).stream()
                    .skip(hasHeader ? 1 : 0)
                    .map(l -> l.split(","))
                    .map(it -> swapColumns ? new TraceLink(it[1], it[0]) : new TraceLink(it[0], it[1]))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        this.traceLinks = validTraceLinks;
    }

    /**
     * Retrieves the loaded trace links from this gold standard.
     *
     * @return Set of trace links
     * @throws IllegalStateException If trace links have not been loaded yet.
     */
    public Set<TraceLink> getTraceLinks() {
        if (traceLinks == null) {
            throw new IllegalStateException("Gold standard trace links have not been loaded yet. Call load() first.");
        }
        return traceLinks;
    }

    /**
     * Retrieves the trace links for a specific source element.
     *
     * @param sourceElement The source element to filter trace links
     * @return Set of trace links associated with the source element
     * @throws IllegalStateException If trace links have not been loaded yet.
     */
    public Set<TraceLink> getTraceLinks(Element sourceElement) {
        if (traceLinks == null) {
            throw new IllegalStateException("Gold standard trace links have not been loaded yet. Call load() first.");
        }
        return traceLinks.stream()
                .filter(tl -> tl.sourceId().equals(sourceElement.getIdentifier()))
                .collect(Collectors.toSet());
    }

    public boolean contains(TraceLink traceLink) {
        if (traceLinks == null) {
            return false;
        }
        return traceLinks.contains(traceLink);
    }

    @Override
    public String getId() {
        if (id == null) {
            throw new IllegalStateException("GoldStandard was not configured as context.");
        }
        return id;
    }
}
