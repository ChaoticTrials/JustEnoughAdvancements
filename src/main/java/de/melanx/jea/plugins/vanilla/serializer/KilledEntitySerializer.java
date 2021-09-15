package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class KilledEntitySerializer extends CriterionSerializer<KilledTrigger.TriggerInstance> {

    public KilledEntitySerializer() {
        super(KilledTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.KILLED_ENTITY);
    }

    @Override
    public void write(KilledTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entityPredicate), buffer);
        PacketUtil.writeDamageSourcePredicate(instance.killingBlow, buffer);
    }

    @Override
    public KilledTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        EntityPredicate.Composite entity = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        DamageSourcePredicate damage = PacketUtil.readDamageSourcePredicate(buffer);
        return new KilledTrigger.TriggerInstance(VanillaCriteriaIds.KILLED_ENTITY, EntityPredicate.Composite.ANY, entity, damage);
    }
}
