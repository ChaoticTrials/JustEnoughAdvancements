package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.TameAnimalTrigger;
import net.minecraft.network.PacketBuffer;

public class TameAnimalSerializer extends CriterionSerializer<TameAnimalTrigger.Instance> {

    public TameAnimalSerializer() {
        super(TameAnimalTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.TAME_ANIMAL);
    }

    @Override
    public void write(TameAnimalTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public TameAnimalTrigger.Instance read(PacketBuffer buffer) {
        return new TameAnimalTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer)));
    }
}
