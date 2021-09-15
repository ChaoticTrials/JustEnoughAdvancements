package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class HurtEntitySerializer extends CriterionSerializer<PlayerHurtEntityTrigger.TriggerInstance> {

    public HurtEntitySerializer() {
        super(PlayerHurtEntityTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.HURT_ENTITY);
    }

    @Override
    public void write(PlayerHurtEntityTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeDamagePredicate(instance.damage, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public PlayerHurtEntityTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        DamagePredicate damage = PacketUtil.readDamagePredicate(buffer);
        EntityPredicate.Composite entity = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new PlayerHurtEntityTrigger.TriggerInstance(EntityPredicate.Composite.ANY, damage, entity);
    }
}
