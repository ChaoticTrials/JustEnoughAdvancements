package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.ConstructBeaconTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;

public class ConstructBeaconSerializer extends CriterionSerializer<ConstructBeaconTrigger.Instance> {

    public ConstructBeaconSerializer() {
        super(ConstructBeaconTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.CONSTRUCT_BEACON);
    }

    @Override
    public void write(ConstructBeaconTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeIntRange(instance.level, buffer);
    }

    @Override
    public ConstructBeaconTrigger.Instance read(PacketBuffer buffer) {
        return new ConstructBeaconTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, PacketUtil.readIntRange(buffer));
    }
}
