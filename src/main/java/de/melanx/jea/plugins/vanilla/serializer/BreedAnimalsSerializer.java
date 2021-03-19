package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.util.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.BredAnimalsTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;

public class BreedAnimalsSerializer extends CriterionSerializer<BredAnimalsTrigger.Instance> {

    public BreedAnimalsSerializer() {
        super(BredAnimalsTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.BREED_ANIMALS);
    }

    @Override
    public void write(BredAnimalsTrigger.Instance instance, PacketBuffer buffer) {
        byte mask = 0;
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.parent), buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.partner), buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.child), buffer);
    }

    @Override
    public BredAnimalsTrigger.Instance read(PacketBuffer buffer) {
        EntityPredicate.AndPredicate parent = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        EntityPredicate.AndPredicate partner = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        EntityPredicate.AndPredicate child = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new BredAnimalsTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, parent, partner, child);
    }
}
