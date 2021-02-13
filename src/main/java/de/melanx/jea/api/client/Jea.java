package de.melanx.jea.api.client;

import de.melanx.jea.api.client.criterion.ICriterionInfo;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Client Only class for JEA.
 * 
 * JEA adds all advancements to JEI, also rom other mods. To get recipe pages for them,
 * you don't need to do anything as long as you only use vanilla criteria. Whenever you
 * register your own criterion, you should register a {@link de.melanx.jea.api.CriterionSerializer}
 * via the forge registry and an {@link ICriterionInfo} in clientSetup. The criterion info must be
 * registered with the same id as the serializer.
 */
public class Jea {

    public static final IIngredientType<IAdvancementInfo> ADVANCEMENT_TYPE = () -> IAdvancementInfo.class;
    
    private static final Map<ResourceLocation, ICriterionInfo<?>> criteria = new HashMap<>();

    /**
     * Registers a new {@link ICriterionInfo}.
     */
    public static void register(ResourceLocation id, ICriterionInfo<?> criterion) {
        if (criteria.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id for JEA criterion registry");
        }
        criteria.put(id, criterion);
    }

    /**
     * Gets all criteria info.
     */
    public static Map<ResourceLocation, ICriterionInfo<?>> getCriteria() {
        return Collections.unmodifiableMap(criteria);
    }
}
