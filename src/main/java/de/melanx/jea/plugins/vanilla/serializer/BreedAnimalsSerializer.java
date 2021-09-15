package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.FriendlyByteBuf;

public class BreedAnimalsSerializer extends CriterionSerializer<BredAnimalsTrigger.TriggerInstance> {

    public BreedAnimalsSerializer() {
        super(BredAnimalsTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.BREED_ANIMALS);
    }

    @Override
    public void write(BredAnimalsTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        byte mask = 0;
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.parent), buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.partner), buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.child), buffer);
    }

    @Override
    public BredAnimalsTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        EntityPredicate.Composite parent = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        EntityPredicate.Composite partner = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        EntityPredicate.Composite child = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new BredAnimalsTrigger.TriggerInstance(EntityPredicate.Composite.ANY, parent, partner, child);
    }
}
