package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.TargetBlockTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class TargetHitSerializer extends CriterionSerializer<TargetBlockTrigger.TriggerInstance> {

    public TargetHitSerializer() {
        super(TargetBlockTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.TARGET_HIT);
    }

    @Override
    public void write(TargetBlockTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeIntRange(instance.signalStrength, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.projectile), buffer);
    }

    @Override
    public TargetBlockTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        MinMaxBounds.Ints signalStrength = PacketUtil.readIntRange(buffer);
        EntityPredicate.Composite projectile = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new TargetBlockTrigger.TriggerInstance(EntityPredicate.Composite.ANY, signalStrength, projectile);
    }
}
