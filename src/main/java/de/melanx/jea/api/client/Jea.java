package de.melanx.jea.api.client;

import de.melanx.jea.JustEnoughAdvancements;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.client.ClientAdvancements;
import de.melanx.jea.recipe.AdvancementRecipeRenderer;
import de.melanx.jea.recipe.AdvancementRecipeRendererTiny;
import de.melanx.jea.render.LargeBlockAppearingIngredientRender;
import de.melanx.jea.render.LargeBlockBreakingIngredientRender;
import de.melanx.jea.render.LargeBlockIngredientRender;
import de.melanx.jea.render.LargeItemIngredientRender;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
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
    
    public static final IIngredientRenderer<IAdvancementInfo> ADVANCEMENT_RECIPE_RENDERER = new AdvancementRecipeRenderer();
    public static final IIngredientRenderer<IAdvancementInfo> ADVANCEMENT_RECIPE_RENDERER_TINY = new AdvancementRecipeRendererTiny();
    
    public static final IIngredientRenderer<ItemStack> SLIGHTLY_LARGE_ITEM = new LargeItemIngredientRender(24, 0);
    public static final IIngredientRenderer<ItemStack> LARGE_ITEM = new LargeItemIngredientRender(48, 0);
    public static final IIngredientRenderer<ItemStack> LARGE_ITEM_FACING_RIGHT = new LargeItemIngredientRender(48, 135);
    public static final IIngredientRenderer<ItemStack> LARGE_BLOCK = new LargeBlockIngredientRender();
    public static final IIngredientRenderer<ItemStack> LARGE_BLOCK_BREAK_SLOW = new LargeBlockBreakingIngredientRender(12);
    public static final IIngredientRenderer<ItemStack> LARGE_BLOCK_BREAK_FAST = new LargeBlockBreakingIngredientRender(3);
    public static final IIngredientRenderer<ItemStack> LARGE_BLOCK_APPEARING = new LargeBlockAppearingIngredientRender(20, 20);


    private static final Map<ResourceLocation, ICriterionInfo<?>> criteria = new HashMap<>();
    private static final Map<Pair<ResourceLocation, String>, ICriterionInfo<?>> advancements = new HashMap<>();

    /**
     * Registers a new {@link ICriterionInfo}.
     */
    public static void register(ResourceLocation id, ICriterionInfo<?> criterion) {
        JustEnoughAdvancements.getInstance().checkRegistryResourceLocation(id, "criterion info registration");
        if (criteria.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id for JEA criterion registry");
        }
        criteria.put(id, criterion);
    }
    
    /**
     * Registers a new {@link ICriterionInfo} but only for one specific advancement criterion. Those are
     * checked first. Allows you to create a special render for an advancement. If multiple calls
     * to this are made with the same advancement, the last one replaces all other calls.
     * You can pass a null {@link ICriterionInfo} in which case the recipe for that criterion is suppressed.
     * This should only be used if really required.
     */
    public static void registerSpecial(ResourceLocation advancement, String criterionKey, ICriterionInfo<?> criterion) {
        // No `checkRegistryResourceLocation` as this may also be used for advancements from advancement packs and such
        if (criterion == null) {
            JustEnoughAdvancements.logger.warn("Advancement criterion recipe was suppressed: " + advancement.toString() + "#" + criterionKey);
        }
        advancements.put(Pair.of(advancement, criterionKey), criterion);
    }

    /**
     * Gets all criteria info.
     */
    public static Map<ResourceLocation, ICriterionInfo<?>> getCriteria() {
        return Collections.unmodifiableMap(criteria);
    }
    
    /**
     * Gets all special advancement info.
     */
    public static Map<Pair<ResourceLocation, String>, ICriterionInfo<?>> getSpecialAdvancements() {
        return Collections.unmodifiableMap(advancements);
    }

    /**
     * Gets the {@link ICriterionInfo} that should be used for an {@link IAdvancementInfo}
     * that was serialised with a given serializer
     */
     @Nullable
    public static ICriterionInfo<?> getCriterionInfo(IAdvancementInfo info, String criterionKey, ResourceLocation serializerId) {
        Pair<ResourceLocation, String> key = Pair.of(info.getId(), criterionKey);
         if (advancements.containsKey(key)) {
             return advancements.get(key);
         } else {
             return criteria.getOrDefault(serializerId, null);
         }
    }

    /**
     * Gets the advancement for a resource location or null if not found.
     */
    @Nullable
    public static IAdvancementInfo getAdvancement(ResourceLocation location) {
        return ClientAdvancements.getInfo(location);
    }
}
