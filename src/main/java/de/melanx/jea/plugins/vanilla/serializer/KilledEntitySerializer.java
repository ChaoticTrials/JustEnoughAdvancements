package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class KilledEntitySerializer extends CriterionSerializer<KilledTrigger.Instance> {

    public KilledEntitySerializer() {
        super(KilledTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.KILLED_ENTITY);
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
        return new KilledTrigger.Instance(VanillaCriteriaIds.KILLED_ENTITY, EntityPredicate.AndPredicate.ANY_AND, entity, damage);
    }
}
