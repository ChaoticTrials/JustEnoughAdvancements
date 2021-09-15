package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.UsingItemTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class UsingItemSerializer extends CriterionSerializer<UsingItemTrigger.TriggerInstance> {

    public UsingItemSerializer() {
        super(UsingItemTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.USING_ITEM);
    }

    @Override
    public void write(UsingItemTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public UsingItemTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new UsingItemTrigger.TriggerInstance(EntityPredicate.Composite.ANY, PacketUtil.readItemPredicate(buffer));
    }
}
