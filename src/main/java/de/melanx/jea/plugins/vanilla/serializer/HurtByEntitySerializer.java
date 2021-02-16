package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityHurtPlayerTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;

public class HurtByEntitySerializer extends CriterionSerializer<EntityHurtPlayerTrigger.Instance> {

    public HurtByEntitySerializer() {
        super(EntityHurtPlayerTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.HURT_BY_ENTITY);
    }

    @Override
    public void write(EntityHurtPlayerTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeDamagePredicate(instance.damage, buffer);
    }

    @Override
    public EntityHurtPlayerTrigger.Instance read(PacketBuffer buffer) {
        return new EntityHurtPlayerTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, PacketUtil.readDamagePredicate(buffer));
    }
}
