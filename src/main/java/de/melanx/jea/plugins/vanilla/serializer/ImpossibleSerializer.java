package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.network.PacketBuffer;

public class ImpossibleSerializer extends CriterionSerializer<ImpossibleTrigger.Instance> {

    public ImpossibleSerializer() {
        super(ImpossibleTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.IMPOSSIBLE);
    }

    @Override
    public void write(ImpossibleTrigger.Instance instance, PacketBuffer buffer) {
        //
    }

    @Override
    public ImpossibleTrigger.Instance read(PacketBuffer buffer) {
        return new ImpossibleTrigger.Instance();
    }
}
