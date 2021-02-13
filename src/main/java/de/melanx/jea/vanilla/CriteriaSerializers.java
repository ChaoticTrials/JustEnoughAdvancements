package de.melanx.jea.vanilla;

import de.melanx.jea.api.CriterionSerializer;
import net.minecraftforge.event.RegistryEvent;

public class CriteriaSerializers {
    
    public static void init(RegistryEvent.Register<CriterionSerializer<?>> event) {
        event.getRegistry().registerAll(
                new ItemCriterionSerializer()
        );
    }
}
