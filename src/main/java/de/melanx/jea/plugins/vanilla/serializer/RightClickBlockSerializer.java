package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnBlockTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.network.FriendlyByteBuf;

public class RightClickBlockSerializer extends CriterionSerializer<ItemUsedOnBlockTrigger.TriggerInstance> {

    public RightClickBlockSerializer() {
        super(ItemUsedOnBlockTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.RIGHT_CLICK_BLOCK);
    }

    @Override
    public void write(ItemUsedOnBlockTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeLocationPredicate(instance.location, buffer);
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public ItemUsedOnBlockTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        LocationPredicate location = PacketUtil.readLocationPredicate(buffer);
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        return new ItemUsedOnBlockTrigger.TriggerInstance(EntityPredicate.Composite.ANY, location, item);
    }
}
