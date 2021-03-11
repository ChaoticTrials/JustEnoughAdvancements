package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class KilledByEntitySerializer extends CriterionSerializer<KilledTrigger.Instance> {

    public KilledByEntitySerializer() {
        super(KilledTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.KILLED_BY_ENTITY);
    }

    @Override
    public void write(KilledTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
        PacketUtil.writeDamageSourcePredicate(instance.killingBlow, buffer);
    }

    @Override
    public KilledTrigger.Instance read(PacketBuffer buffer) {
        EntityPredicate.AndPredicate entity = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        DamageSourcePredicate damage = PacketUtil.readDamageSourcePredicate(buffer);
        return new KilledTrigger.Instance(VanillaCriteriaIds.KILLED_BY_ENTITY, EntityPredicate.AndPredicate.ANY_AND, entity, damage);
    }
}
