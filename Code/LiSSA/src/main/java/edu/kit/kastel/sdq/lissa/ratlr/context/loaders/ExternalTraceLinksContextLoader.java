/* Licensed under MIT 2025. */
package edu.kit.kastel.sdq.lissa.ratlr.context.loaders;

import edu.kit.kastel.sdq.lissa.ratlr.configuration.ContextConfiguration;
import edu.kit.kastel.sdq.lissa.ratlr.context.ContextStore;
import edu.kit.kastel.sdq.lissa.ratlr.goldstandard.GoldStandard;

public class ExternalTraceLinksContextLoader extends ContextLoader {

    private final GoldStandard goldStandard;

    ExternalTraceLinksContextLoader(String contextId, ContextStore contextStore, ContextConfiguration configuration) {
        super(contextId, contextStore);
        goldStandard = new GoldStandard(
                configuration.getModuleConfiguration(AllowableContextModuleTypes.GOLD_STANDARD, 0), contextId);
    }

    @Override
    public void loadContext() {
        goldStandard.load();
        contextStore.createContext(goldStandard);
    }
}
