package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SummonedEntityTrigger;
import net.minecraft.network.PacketBuffer;

public class SummonEntitySerializer extends CriterionSerializer<SummonedEntityTrigger.Instance> {

    public SummonEntitySerializer() {
        super(SummonedEntityTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.SUMMON_ENTITY);
    }

    @Override
    public void write(SummonedEntityTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public SummonedEntityTrigger.Instance read(PacketBuffer buffer) {
        return new SummonedEntityTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer)));
    }
}
