package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.TargetHitTrigger;
import net.minecraft.network.PacketBuffer;

public class TargetHitSerializer extends CriterionSerializer<TargetHitTrigger.Instance> {

    public TargetHitSerializer() {
        super(TargetHitTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.TARGET_HIT);
    }

    @Override
    public void write(TargetHitTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeIntRange(instance.signalStrength, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.projectile), buffer);
    }

    @Override
    public TargetHitTrigger.Instance read(PacketBuffer buffer) {
        MinMaxBounds.IntBound signalStrength = PacketUtil.readIntRange(buffer);
        EntityPredicate.AndPredicate projectile = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new TargetHitTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, signalStrength, projectile);
    }
}
