package de.melanx.jea.recipe;

import de.melanx.jea.api.client.Jea;
import de.melanx.jea.ingredient.AdvancementIngredientRenderer;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.renderer.Rect2i;

import java.lang.reflect.Field;

public class RecipeRenderSizeAdjust {
    
    private static Class<?> guiIngredientClass;
    private static Field rectangleField;
    private static Field ingredientRenderField;
    
    public static void changeRecipeSizes(IRecipeLayout layout, IIngredientType<?> type) {
        for (IGuiIngredient<?> ingredient : layout.getIngredientsGroup(type).getGuiIngredients().values()) {
            try {
                if (guiIngredientClass == null || rectangleField == null || ingredientRenderField == null) {
                    guiIngredientClass = Class.forName("mezz.jei.gui.ingredients.GuiIngredient");
                    rectangleField = guiIngredientClass.getDeclaredField("rect");
                    rectangleField.setAccessible(true);
                    ingredientRenderField = guiIngredientClass.getDeclaredField("ingredientRenderer");
                    ingredientRenderField.setAccessible(true);
                }
                if (guiIngredientClass.isAssignableFrom(ingredient.getClass())) {
                    IIngredientRenderer<?> render = (IIngredientRenderer<?>) ingredientRenderField.get(ingredient);
                    if (type == Jea.ADVANCEMENT_TYPE && (render == null || render instanceof AdvancementIngredientRenderer)) {
                        ingredientRenderField.set(ingredient, Jea.ADVANCEMENT_RECIPE_RENDERER);
                        render = Jea.ADVANCEMENT_RECIPE_RENDERER;
                    }
                    Rect2i rect = (Rect2i) rectangleField.get(ingredient);
                    Rect2i newRect = newSize(render, rect);
                    if (newRect != null) {
                        rectangleField.set(ingredient, newRect);
                    }
                }
            } catch (ReflectiveOperationException | NoClassDefFoundError | ClassCastException e) {
                guiIngredientClass = null;
                rectangleField = null;
            }
        }
    }
    
    private static Rect2i newSize(IIngredientRenderer<?> render, Rect2i size) {
        if (render == Jea.ADVANCEMENT_RECIPE_RENDERER) {
            return new Rect2i(size.getX(), size.getY(), 26, 26);
        } else {
            return null;
        }
    }
}
