package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class KilledSerializer extends CriterionSerializer<KilledTrigger.Instance> {

    public KilledSerializer() {
        super(KilledTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.KILLED);
    }

    @Override
    protected boolean checkValidity(ICriterionInstance instance) {
        return instance instanceof KilledTrigger.Instance && ((KilledTrigger.Instance) instance).criterion != null;
    }

    @Override
    public void write(KilledTrigger.Instance instance, PacketBuffer buffer) {
        buffer.writeResourceLocation(instance.criterion);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
        PacketUtil.writeDamageSourcePredicate(instance.killingBlow, buffer);
    }

    @Override
    public KilledTrigger.Instance read(PacketBuffer buffer) {
        ResourceLocation criterionId = buffer.readResourceLocation();
        EntityPredicate.AndPredicate entity = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        DamageSourcePredicate damage = PacketUtil.readDamageSourcePredicate(buffer);
        return new KilledTrigger.Instance(criterionId, EntityPredicate.AndPredicate.ANY_AND, entity, damage);
    }
}
