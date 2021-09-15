package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.LocationTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class WinRaidSerializer extends CriterionSerializer<LocationTrigger.TriggerInstance> {

    public WinRaidSerializer() {
        super(LocationTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.WIN_RAID);
    }

    @Override
    public void write(LocationTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeLocationPredicate(instance.location, buffer);
    }

    @Override
    public LocationTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        LocationPredicate location = PacketUtil.readLocationPredicate(buffer);
        return new LocationTrigger.TriggerInstance(VanillaCriteriaIds.WIN_RAID, EntityPredicate.Composite.ANY, location);
    }
}
