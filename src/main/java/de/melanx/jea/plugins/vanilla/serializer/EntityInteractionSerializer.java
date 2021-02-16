package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.PlayerEntityInteractionTrigger;
import net.minecraft.network.PacketBuffer;

public class EntityInteractionSerializer extends CriterionSerializer<PlayerEntityInteractionTrigger.Instance> {

    public EntityInteractionSerializer() {
        super(PlayerEntityInteractionTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.ENTITY_INTERACTION);
    }

    @Override
    public void write(PlayerEntityInteractionTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.stack, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public PlayerEntityInteractionTrigger.Instance read(PacketBuffer buffer) {
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        EntityPredicate.AndPredicate entity = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new PlayerEntityInteractionTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, item, entity);
    }
}
