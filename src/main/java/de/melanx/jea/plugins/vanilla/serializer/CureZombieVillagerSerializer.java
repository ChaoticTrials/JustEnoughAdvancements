package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.CuredZombieVillagerTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;

public class CureZombieVillagerSerializer extends CriterionSerializer<CuredZombieVillagerTrigger.Instance> {

    public CureZombieVillagerSerializer() {
        super(CuredZombieVillagerTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.CURE_ZOMBIE_VILLAGER);
    }

    @Override
    public void write(CuredZombieVillagerTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.zombie), buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.villager), buffer);
    }

    @Override
    public CuredZombieVillagerTrigger.Instance read(PacketBuffer buffer) {
        EntityPredicate.AndPredicate zombie = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        EntityPredicate.AndPredicate villager = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new CuredZombieVillagerTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, zombie, villager);
    }
}
