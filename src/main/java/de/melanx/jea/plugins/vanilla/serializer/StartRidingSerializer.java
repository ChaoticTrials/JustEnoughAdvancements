package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.StartRidingTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class StartRidingSerializer extends CriterionSerializer<StartRidingTrigger.TriggerInstance> {

    public StartRidingSerializer() {
        super(StartRidingTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.START_RIDING);
    }

    @Override
    public void write(StartRidingTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        
    }

    @Override
    public StartRidingTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new StartRidingTrigger.TriggerInstance(EntityPredicate.Composite.ANY);
    }
}
