package de.melanx.jea;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.ILootCondition;

public class LootUtil {

    public static EntityPredicate asEntity(EntityPredicate.AndPredicate predicate) {
        for (ILootCondition loot : predicate.lootConditions) {
            if (loot instanceof EntityHasProperty && ((EntityHasProperty) loot).target == LootContext.EntityTarget.THIS) {
                return ((EntityHasProperty) loot).predicate;
            }
        }
        return EntityPredicate.ANY;
    }

    public static EntityPredicate.AndPredicate asLootPredicate(EntityPredicate predicate) {
        return new EntityPredicate.AndPredicate(new ILootCondition[]{
                new EntityHasProperty(predicate, LootContext.EntityTarget.THIS)
        });
    }
}
