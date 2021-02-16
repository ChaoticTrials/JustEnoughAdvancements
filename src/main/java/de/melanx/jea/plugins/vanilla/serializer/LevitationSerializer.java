package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.LevitationTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.network.PacketBuffer;

public class LevitationSerializer extends CriterionSerializer<LevitationTrigger.Instance> {

    public LevitationSerializer() {
        super(LevitationTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.LEVITATION);
    }

    @Override
    public void write(LevitationTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeDistancePredicate(instance.distance, buffer);
        PacketUtil.writeIntRange(instance.duration, buffer);
    }

    @Override
    public LevitationTrigger.Instance read(PacketBuffer buffer) {
        DistancePredicate distance = PacketUtil.readDistancePredicate(buffer);
        MinMaxBounds.IntBound duration = PacketUtil.readIntRange(buffer);
        return new LevitationTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, distance, duration);
    }
}
