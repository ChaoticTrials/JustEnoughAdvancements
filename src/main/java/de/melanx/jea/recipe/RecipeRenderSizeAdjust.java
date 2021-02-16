package de.melanx.jea.recipe;

import de.melanx.jea.api.client.Jea;
import de.melanx.jea.ingredient.AdvancementIngredientRenderer;
import de.melanx.jea.render.LargeBlockIngredientRender;
import de.melanx.jea.render.LargeItemIngredientRender;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.renderer.Rectangle2d;

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
                    Rectangle2d rect = (Rectangle2d) rectangleField.get(ingredient);
                    Rectangle2d newRect = newSize(render, rect);
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
    
    private static Rectangle2d newSize(IIngredientRenderer<?> render, Rectangle2d size) {
        if (render == Jea.ADVANCEMENT_RECIPE_RENDERER) {
            return new Rectangle2d(size.getX(), size.getY(), 26, 26);
        } else if (render instanceof LargeBlockIngredientRender) {
            return new Rectangle2d(size.getX(), size.getY(), LargeBlockIngredientRender.SIZE, LargeBlockIngredientRender.SIZE);
        } else if (render instanceof LargeItemIngredientRender) {
            return new Rectangle2d(size.getX(), size.getY(), ((LargeItemIngredientRender) render).size, ((LargeItemIngredientRender) render).size);

        } else {
            return null;
        }
    }
}
