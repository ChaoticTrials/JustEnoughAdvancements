package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;

public class ItemDurabilitySerializer extends CriterionSerializer<ItemDurabilityTrigger.TriggerInstance> {

    public ItemDurabilitySerializer() {
        super(ItemDurabilityTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.ITEM_DURABILITY);
    }

    @Override
    public void write(ItemDurabilityTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
        PacketUtil.writeIntRange(instance.durability, buffer);
        PacketUtil.writeIntRange(instance.delta, buffer);
    }

    @Override
    public ItemDurabilityTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        MinMaxBounds.Ints durability = PacketUtil.readIntRange(buffer);
        MinMaxBounds.Ints delta = PacketUtil.readIntRange(buffer);
        return new ItemDurabilityTrigger.TriggerInstance(EntityPredicate.Composite.ANY, item, durability, delta);
    }
}
