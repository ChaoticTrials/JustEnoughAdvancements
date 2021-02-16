package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.NetherTravelTrigger;
import net.minecraft.network.PacketBuffer;

public class NetherTravelSerializer extends CriterionSerializer<NetherTravelTrigger.Instance> {

    public NetherTravelSerializer() {
        super(NetherTravelTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.NETHER_TRAVEL);
    }

    @Override
    public void write(NetherTravelTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeLocationPredicate(instance.entered, buffer);
        PacketUtil.writeLocationPredicate(instance.exited, buffer);
        PacketUtil.writeDistancePredicate(instance.distance, buffer);
    }

    @Override
    public NetherTravelTrigger.Instance read(PacketBuffer buffer) {
        LocationPredicate entered = PacketUtil.readLocationPredicate(buffer);
        LocationPredicate exited = PacketUtil.readLocationPredicate(buffer);
        DistancePredicate distance = PacketUtil.readDistancePredicate(buffer);
        return new NetherTravelTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, entered, exited, distance);
    }
}
