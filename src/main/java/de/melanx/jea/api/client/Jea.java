package de.melanx.jea.api.client;

import de.melanx.jea.client.ClientAdvancements;
import de.melanx.jea.recipe.AdvancementRecipeRenderer;
import de.melanx.jea.recipe.AdvancementRecipeRendererTiny;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Client Only class for JEA.
 */
public class Jea {

    public static final IIngredientType<IAdvancementInfo> ADVANCEMENT_TYPE = () -> IAdvancementInfo.class;
    
    public static final IIngredientRenderer<IAdvancementInfo> ADVANCEMENT_RECIPE_RENDERER = new AdvancementRecipeRenderer();
    public static final IIngredientRenderer<IAdvancementInfo> ADVANCEMENT_RECIPE_RENDERER_TINY = new AdvancementRecipeRendererTiny();

    /**
     * Gets the advancement for a resource location or null if not found.
     */
    @Nullable
    public static IAdvancementInfo getAdvancement(ResourceLocation location) {
        return ClientAdvancements.getInfo(location);
    }
}
