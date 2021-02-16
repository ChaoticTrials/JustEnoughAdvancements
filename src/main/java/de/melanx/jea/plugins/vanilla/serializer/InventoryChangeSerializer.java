package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.network.PacketBuffer;

public class InventoryChangeSerializer extends CriterionSerializer<InventoryChangeTrigger.Instance> {
    
    public InventoryChangeSerializer() {
        super(InventoryChangeTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.INVENTORY_CHANGE);
    }

    @Override
    public void write(InventoryChangeTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeIntRange(instance.occupied, buffer);
        PacketUtil.writeIntRange(instance.full, buffer);
        PacketUtil.writeIntRange(instance.empty, buffer);
        buffer.writeVarInt(instance.items.length);
        for (ItemPredicate predicate : instance.items) {
            PacketUtil.writeItemPredicate(predicate, buffer);
        }
    }

    @Override
    public InventoryChangeTrigger.Instance read(PacketBuffer buffer) {
        MinMaxBounds.IntBound occupied = PacketUtil.readIntRange(buffer);
        MinMaxBounds.IntBound full = PacketUtil.readIntRange(buffer);
        MinMaxBounds.IntBound empty = PacketUtil.readIntRange(buffer);
        ItemPredicate[] items = new ItemPredicate[buffer.readVarInt()];
        for (int i = 0; i < items.length; i++) {
            items[i] = PacketUtil.readItemPredicate(buffer);
        }
        return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, occupied, full, empty, items);
    }
}
