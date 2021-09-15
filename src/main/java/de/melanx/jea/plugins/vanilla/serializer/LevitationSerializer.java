package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LevitationTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;

public class LevitationSerializer extends CriterionSerializer<LevitationTrigger.TriggerInstance> {

    public LevitationSerializer() {
        super(LevitationTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.LEVITATION);
    }

    @Override
    public void write(LevitationTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeDistancePredicate(instance.distance, buffer);
        PacketUtil.writeIntRange(instance.duration, buffer);
    }

    @Override
    public LevitationTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        DistancePredicate distance = PacketUtil.readDistancePredicate(buffer);
        MinMaxBounds.Ints duration = PacketUtil.readIntRange(buffer);
        return new LevitationTrigger.TriggerInstance(EntityPredicate.Composite.ANY, distance, duration);
    }
}
