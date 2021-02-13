package de.melanx.jea.vanilla;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class ItemCriterionSerializer extends CriterionSerializer<InventoryChangeTrigger.Instance> {
    
    public ItemCriterionSerializer() {
        super(InventoryChangeTrigger.Instance.class);
        this.setRegistryName(new ResourceLocation("inventory_changed"));
    }

    @Override
    public void write(InventoryChangeTrigger.Instance instance, PacketBuffer buffer) {
        buffer.writeVarInt(instance.items.length);
        for (ItemPredicate predicate : instance.items) {
            PacketUtil.writeJSON(buffer, predicate.serialize());
        }
    }

    @Override
    public InventoryChangeTrigger.Instance read(PacketBuffer buffer) {
        ItemPredicate[] items = new ItemPredicate[buffer.readVarInt()];
        for (int i = 0; i < items.length; i++) {
            items[i] = ItemPredicate.deserialize(PacketUtil.readJSON(buffer));
        }
        return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, items);
    }
}
