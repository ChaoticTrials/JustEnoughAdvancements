package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EffectsChangedTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;

public class ChangeEffectsSerializer extends CriterionSerializer<EffectsChangedTrigger.Instance> {

    public ChangeEffectsSerializer() {
        super(EffectsChangedTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.CHANGE_EFFECTS);
    }

    @Override
    public void write(EffectsChangedTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeMobEffectsPredicate(instance.effects, buffer);
    }

    @Override
    public EffectsChangedTrigger.Instance read(PacketBuffer buffer) {
        return new EffectsChangedTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, PacketUtil.readMobEffectsPredicate(buffer));
    }
}
