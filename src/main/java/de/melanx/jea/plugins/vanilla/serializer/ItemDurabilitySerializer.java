package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemDurabilityTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.network.PacketBuffer;

public class ItemDurabilitySerializer extends CriterionSerializer<ItemDurabilityTrigger.Instance> {

    public ItemDurabilitySerializer() {
        super(ItemDurabilityTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.ITEM_DURABILITY);
    }

    @Override
    public void write(ItemDurabilityTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
        PacketUtil.writeIntRange(instance.durability, buffer);
        PacketUtil.writeIntRange(instance.delta, buffer);
    }

    @Override
    public ItemDurabilityTrigger.Instance read(PacketBuffer buffer) {
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        MinMaxBounds.IntBound durability = PacketUtil.readIntRange(buffer);
        MinMaxBounds.IntBound delta = PacketUtil.readIntRange(buffer);
        return new ItemDurabilityTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, item, durability, delta);
    }
}
