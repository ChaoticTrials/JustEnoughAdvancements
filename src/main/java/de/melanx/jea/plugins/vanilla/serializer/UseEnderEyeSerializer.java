package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.UsedEnderEyeTrigger;
import net.minecraft.network.PacketBuffer;

public class UseEnderEyeSerializer extends CriterionSerializer<UsedEnderEyeTrigger.Instance> {

    public UseEnderEyeSerializer() {
        super(UsedEnderEyeTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.USE_ENDER_EYE);
    }

    @Override
    public void write(UsedEnderEyeTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeFloatRange(instance.distance, buffer);
    }

    @Override
    public UsedEnderEyeTrigger.Instance read(PacketBuffer buffer) {
        return new UsedEnderEyeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, PacketUtil.readFloatRange(buffer));
    }
}
