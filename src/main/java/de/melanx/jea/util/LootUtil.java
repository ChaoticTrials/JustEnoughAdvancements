package de.melanx.jea.util;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class LootUtil {

    public static EntityPredicate asEntity(EntityPredicate.Composite predicate) {
        if (predicate == EntityPredicate.Composite.ANY) return EntityPredicate.ANY;
        for (LootItemCondition loot : predicate.conditions) {
            if (loot instanceof LootItemEntityPropertyCondition condition && condition.entityTarget == LootContext.EntityTarget.THIS) {
                return condition.predicate;
            }
        }
        return EntityPredicate.ANY;
    }

    public static EntityPredicate.Composite asLootPredicate(EntityPredicate predicate) {
        if (predicate == EntityPredicate.ANY) return EntityPredicate.Composite.ANY;
        return new EntityPredicate.Composite(new LootItemCondition[]{
                new LootItemEntityPropertyCondition(predicate, LootContext.EntityTarget.THIS)
        });
    }
}
