package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.FriendlyByteBuf;

public class CureZombieVillagerSerializer extends CriterionSerializer<CuredZombieVillagerTrigger.TriggerInstance> {

    public CureZombieVillagerSerializer() {
        super(CuredZombieVillagerTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.CURE_ZOMBIE_VILLAGER);
    }

    @Override
    public void write(CuredZombieVillagerTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.zombie), buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.villager), buffer);
    }

    @Override
    public CuredZombieVillagerTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        EntityPredicate.Composite zombie = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        EntityPredicate.Composite villager = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        return new CuredZombieVillagerTrigger.TriggerInstance(EntityPredicate.Composite.ANY, zombie, villager);
    }
}
