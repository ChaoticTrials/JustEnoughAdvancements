package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.FriendlyByteBuf;

public class HurtByEntitySerializer extends CriterionSerializer<EntityHurtPlayerTrigger.TriggerInstance> {

    public HurtByEntitySerializer() {
        super(EntityHurtPlayerTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.HURT_BY_ENTITY);
    }

    @Override
    public void write(EntityHurtPlayerTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeDamagePredicate(instance.damage, buffer);
    }

    @Override
    public EntityHurtPlayerTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new EntityHurtPlayerTrigger.TriggerInstance(EntityPredicate.Composite.ANY, PacketUtil.readDamagePredicate(buffer));
    }
}
