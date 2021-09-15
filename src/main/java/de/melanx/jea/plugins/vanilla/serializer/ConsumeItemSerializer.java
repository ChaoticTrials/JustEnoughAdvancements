package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.FriendlyByteBuf;

public class ConsumeItemSerializer extends CriterionSerializer<ConsumeItemTrigger.TriggerInstance> {

    public ConsumeItemSerializer() {
        super(ConsumeItemTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.CONSUME_ITEM);
    }

    @Override
    public void write(ConsumeItemTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public ConsumeItemTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new ConsumeItemTrigger.TriggerInstance(EntityPredicate.Composite.ANY, PacketUtil.readItemPredicate(buffer));
    }
}
