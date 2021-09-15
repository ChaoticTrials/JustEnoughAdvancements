package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;

public class InventoryChangeSerializer extends CriterionSerializer<InventoryChangeTrigger.TriggerInstance> {
    
    public InventoryChangeSerializer() {
        super(InventoryChangeTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.INVENTORY_CHANGE);
    }

    @Override
    public void write(InventoryChangeTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeIntRange(instance.slotsOccupied, buffer);
        PacketUtil.writeIntRange(instance.slotsFull, buffer);
        PacketUtil.writeIntRange(instance.slotsEmpty, buffer);
        buffer.writeVarInt(instance.predicates.length);
        for (ItemPredicate predicate : instance.predicates) {
            PacketUtil.writeItemPredicate(predicate, buffer);
        }
    }

    @Override
    public InventoryChangeTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        MinMaxBounds.Ints occupied = PacketUtil.readIntRange(buffer);
        MinMaxBounds.Ints full = PacketUtil.readIntRange(buffer);
        MinMaxBounds.Ints empty = PacketUtil.readIntRange(buffer);
        ItemPredicate[] items = new ItemPredicate[buffer.readVarInt()];
        for (int i = 0; i < items.length; i++) {
            items[i] = PacketUtil.readItemPredicate(buffer);
        }
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, occupied, full, empty, items);
    }
}
