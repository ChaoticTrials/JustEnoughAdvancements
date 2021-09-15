package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.ConstructBeaconTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.FriendlyByteBuf;

public class ConstructBeaconSerializer extends CriterionSerializer<ConstructBeaconTrigger.TriggerInstance> {

    public ConstructBeaconSerializer() {
        super(ConstructBeaconTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.CONSTRUCT_BEACON);
    }

    @Override
    public void write(ConstructBeaconTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeIntRange(instance.level, buffer);
    }

    @Override
    public ConstructBeaconTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new ConstructBeaconTrigger.TriggerInstance(EntityPredicate.Composite.ANY, PacketUtil.readIntRange(buffer));
    }
}
