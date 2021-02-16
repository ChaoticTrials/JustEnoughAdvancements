package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.ThrownItemPickedUpByEntityTrigger;
import net.minecraft.network.PacketBuffer;

public class EntityPickupItemSerializer extends CriterionSerializer<ThrownItemPickedUpByEntityTrigger.Instance> {

    public EntityPickupItemSerializer() {
        super(ThrownItemPickedUpByEntityTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.ENTITY_PICKUP_ITEM);
    }

    @Override
    public void write(ThrownItemPickedUpByEntityTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.stack, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public ThrownItemPickedUpByEntityTrigger.Instance read(PacketBuffer buffer) {
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        EntityPredicate.AndPredicate entity = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new ThrownItemPickedUpByEntityTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, item, entity);
    }
}
