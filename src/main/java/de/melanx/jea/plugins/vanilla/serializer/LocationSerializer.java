package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.PositionTrigger;
import net.minecraft.network.PacketBuffer;

public class LocationSerializer extends CriterionSerializer<PositionTrigger.Instance> {

    public LocationSerializer() {
        super(PositionTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.LOCATION);
    }

    @Override
    public void write(PositionTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeLocationPredicate(instance.location, buffer);
    }

    @Override
    public PositionTrigger.Instance read(PacketBuffer buffer) {
        LocationPredicate location = PacketUtil.readLocationPredicate(buffer);
        return new PositionTrigger.Instance(VanillaCriteriaIds.LOCATION, EntityPredicate.AndPredicate.ANY_AND, location);
    }
}
