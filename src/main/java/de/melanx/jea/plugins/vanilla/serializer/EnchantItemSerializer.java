package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;

public class EnchantItemSerializer extends CriterionSerializer<EnchantedItemTrigger.TriggerInstance> {

    public EnchantItemSerializer() {
        super(EnchantedItemTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.ENCHANT_ITEM);
    }

    @Override
    public void write(EnchantedItemTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
        PacketUtil.writeIntRange(instance.levels, buffer);
    }

    @Override
    public EnchantedItemTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        MinMaxBounds.Ints levels = PacketUtil.readIntRange(buffer);
        return new EnchantedItemTrigger.TriggerInstance(EntityPredicate.Composite.ANY, item, levels);
    }
}
