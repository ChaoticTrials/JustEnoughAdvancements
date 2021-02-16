package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class PositionSerializer extends CriterionSerializer<PositionTrigger.Instance> {

    public PositionSerializer() {
        super(PositionTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.POSITION);
    }

    @Override
    protected boolean checkValidity(ICriterionInstance instance) {
        return instance instanceof PositionTrigger.Instance && ((PositionTrigger.Instance) instance).criterion != null;
    }

    @Override
    public void write(PositionTrigger.Instance instance, PacketBuffer buffer) {
        buffer.writeResourceLocation(instance.criterion);
        PacketUtil.writeLocationPredicate(instance.location, buffer);
    }

    @Override
    public PositionTrigger.Instance read(PacketBuffer buffer) {
        ResourceLocation criterionId = buffer.readResourceLocation();
        LocationPredicate location = PacketUtil.readLocationPredicate(buffer);
        return new PositionTrigger.Instance(criterionId, EntityPredicate.AndPredicate.ANY_AND, location);
    }
}
