package de.melanx.jea.api.client.criterion;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * A criterion info is used to draw data for one criterion on a recipe.
 */
public interface ICriterionInfo<T extends ICriterionInstance> {

    // TODO a way for implementations to easily mrk thing selected on hover likes slots.
    
    /**
     * The total width you have to draw your recipe.
     */
    int RECIPE_WIDTH = 150;

    /**
     * The total height you have to draw your recipe. The tortal height of the recipe is taller
     * as JEA draws something above as well.
     * 
     * <b>Make sure to leave a space of {@code SPACE_TOP} at the top of the recipe. This space is
     * used by JEA.</b>
     */
    int RECIPE_HEIGHT = 90;

    /**
     * The space you should leave empty at the top of the recipe. Space is used by JEA.
     */
    int SPACE_TOP = 36;

    /**
     * Gets the criterion instance class this is used for.
     */
    Class<T> criterionClass();

    /**
     * Sets the ingredients just like in a JEI recipe category.
     * 
     * <b>YOU MAY NOT SET ANY ADVANCEMENT OUTPUT INGREDIENT HERE!</b>
     */
    void setIngredients(IAdvancementInfo advancement, String criterionKey, T instance, IIngredients ii);

    /**
     * Just like in a recipe category.
     */
    void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, T instance, IIngredients ii);

    /**
     * Just like in a recipe category.
     */
    void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, T instance, double mouseX, double mouseY);

    /**
     * Adds the tooltip text for a mouse location.
     */
    void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, T instance, double mouseX, double mouseY);
}
