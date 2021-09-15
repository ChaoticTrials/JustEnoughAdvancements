package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class ImpossibleSerializer extends CriterionSerializer<ImpossibleTrigger.TriggerInstance> {

    public ImpossibleSerializer() {
        super(ImpossibleTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.IMPOSSIBLE);
    }

    @Override
    public void write(ImpossibleTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        //
    }

    @Override
    public ImpossibleTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new ImpossibleTrigger.TriggerInstance();
    }
}
