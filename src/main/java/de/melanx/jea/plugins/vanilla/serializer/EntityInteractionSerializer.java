package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class EntityInteractionSerializer extends CriterionSerializer<PlayerInteractTrigger.TriggerInstance> {

    public EntityInteractionSerializer() {
        super(PlayerInteractTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.ENTITY_INTERACTION);
    }

    @Override
    public void write(PlayerInteractTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public PlayerInteractTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        EntityPredicate.Composite entity = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new PlayerInteractTrigger.TriggerInstance(EntityPredicate.Composite.ANY, item, entity);
    }
}
