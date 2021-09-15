package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.FriendlyByteBuf;

public class ChangeEffectsSerializer extends CriterionSerializer<EffectsChangedTrigger.TriggerInstance> {

    public ChangeEffectsSerializer() {
        super(EffectsChangedTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.CHANGE_EFFECTS);
    }

    @Override
    public void write(EffectsChangedTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeMobEffectsPredicate(instance.effects, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.source), buffer);
    }

    @Override
    public EffectsChangedTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new EffectsChangedTrigger.TriggerInstance(EntityPredicate.Composite.ANY, PacketUtil.readMobEffectsPredicate(buffer), LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer)));
    }
}
