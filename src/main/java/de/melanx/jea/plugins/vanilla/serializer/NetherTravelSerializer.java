package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.NetherTravelTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class NetherTravelSerializer extends CriterionSerializer<NetherTravelTrigger.TriggerInstance> {

    public NetherTravelSerializer() {
        super(NetherTravelTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.NETHER_TRAVEL);
    }

    @Override
    public void write(NetherTravelTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeLocationPredicate(instance.entered, buffer);
        PacketUtil.writeLocationPredicate(instance.exited, buffer);
        PacketUtil.writeDistancePredicate(instance.distance, buffer);
    }

    @Override
    public NetherTravelTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        LocationPredicate entered = PacketUtil.readLocationPredicate(buffer);
        LocationPredicate exited = PacketUtil.readLocationPredicate(buffer);
        DistancePredicate distance = PacketUtil.readDistancePredicate(buffer);
        return new NetherTravelTrigger.TriggerInstance(EntityPredicate.Composite.ANY, entered, exited, distance);
    }
}
