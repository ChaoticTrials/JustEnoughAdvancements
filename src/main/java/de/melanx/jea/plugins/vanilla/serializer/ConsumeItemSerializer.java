package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;

public class ConsumeItemSerializer extends CriterionSerializer<ConsumeItemTrigger.Instance> {

    public ConsumeItemSerializer() {
        super(ConsumeItemTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.CONSUME_ITEM);
    }

    @Override
    public void write(ConsumeItemTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public ConsumeItemTrigger.Instance read(PacketBuffer buffer) {
        return new ConsumeItemTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, PacketUtil.readItemPredicate(buffer));
    }
}
