package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class SummonEntitySerializer extends CriterionSerializer<SummonedEntityTrigger.TriggerInstance> {

    public SummonEntitySerializer() {
        super(SummonedEntityTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.SUMMON_ENTITY);
    }

    @Override
    public void write(SummonedEntityTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public SummonedEntityTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new SummonedEntityTrigger.TriggerInstance(EntityPredicate.Composite.ANY, LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer)));
    }
}
