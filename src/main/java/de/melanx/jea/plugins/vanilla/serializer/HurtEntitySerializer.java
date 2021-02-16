package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.DamagePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
import net.minecraft.network.PacketBuffer;

public class HurtEntitySerializer extends CriterionSerializer<PlayerHurtEntityTrigger.Instance> {

    public HurtEntitySerializer() {
        super(PlayerHurtEntityTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.HURT_ENTITY);
    }

    @Override
    public void write(PlayerHurtEntityTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeDamagePredicate(instance.damage, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public PlayerHurtEntityTrigger.Instance read(PacketBuffer buffer) {
        DamagePredicate damage = PacketUtil.readDamagePredicate(buffer);
        EntityPredicate.AndPredicate entity = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new PlayerHurtEntityTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, damage, entity);
    }
}
