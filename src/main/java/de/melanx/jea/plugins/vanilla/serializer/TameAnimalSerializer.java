package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class TameAnimalSerializer extends CriterionSerializer<TameAnimalTrigger.TriggerInstance> {

    public TameAnimalSerializer() {
        super(TameAnimalTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.TAME_ANIMAL);
    }

    @Override
    public void write(TameAnimalTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public TameAnimalTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new TameAnimalTrigger.TriggerInstance(EntityPredicate.Composite.ANY, LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer)));
    }
}
