package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EnchantedItemTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.network.PacketBuffer;

public class EnchantItemSerializer extends CriterionSerializer<EnchantedItemTrigger.Instance> {

    public EnchantItemSerializer() {
        super(EnchantedItemTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.ENCHANT_ITEM);
    }

    @Override
    public void write(EnchantedItemTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
        PacketUtil.writeIntRange(instance.levels, buffer);
    }

    @Override
    public EnchantedItemTrigger.Instance read(PacketBuffer buffer) {
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        MinMaxBounds.IntBound levels = PacketUtil.readIntRange(buffer);
        return new EnchantedItemTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, item, levels);
    }
}
