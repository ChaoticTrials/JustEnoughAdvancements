package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.RightClickBlockWithItemTrigger;
import net.minecraft.network.PacketBuffer;

public class RightClickBlockSerializer extends CriterionSerializer<RightClickBlockWithItemTrigger.Instance> {

    public RightClickBlockSerializer() {
        super(RightClickBlockWithItemTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.RIGHT_CLICK_BLOCK);
    }

    @Override
    public void write(RightClickBlockWithItemTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeLocationPredicate(instance.location, buffer);
        PacketUtil.writeItemPredicate(instance.stack, buffer);
    }

    @Override
    public RightClickBlockWithItemTrigger.Instance read(PacketBuffer buffer) {
        LocationPredicate location = PacketUtil.readLocationPredicate(buffer);
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        return new RightClickBlockWithItemTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, location, item);
    }
}
