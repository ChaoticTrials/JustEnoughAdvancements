package de.melanx.jea.recipe;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.AdvancementInfo;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.client.AdvancementDisplayHelper;
import de.melanx.jea.client.ClientAdvancements;
import de.melanx.jea.ingredient.AdvancementIngredientRenderer;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Rectangle2d;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class CriterionRecipe {
    
    private static Class<?> guiIngredientClass;
    private static Field rectangleField;
    private static Field ingredientRenderField;
    
    private final AdvancementInfo info;
    private final String criterionKey;
    private final ICriterionInstance criterion;
    private final ICriterionInfo<?> criterionInfo;
    @Nullable
    private final AdvancementInfo parent;

    public CriterionRecipe(AdvancementInfo info, String criterionKey, ICriterionInfo<?> criterionInfo) {
        this.info = info;
        this.criterionKey = criterionKey;
        this.criterionInfo = criterionInfo;
        this.criterion = info.getCriteria().get(criterionKey).getCriterionInstance();
        this.parent = ClientAdvancements.getInfo(this.info.getParent());
    }

    public void setIngredients(IIngredients ii) {
        //noinspection unchecked
        ((ICriterionInfo<ICriterionInstance>) this.criterionInfo).setIngredients(this.info, this.criterionKey, this.criterion, ii);
        ii.setOutputs(Jea.ADVANCEMENT_TYPE, ImmutableList.of(this.info));
        if (this.parent != null) {
            ii.setInputs(Jea.ADVANCEMENT_TYPE, ImmutableList.of(this.parent));
        }
    }

    public void setRecipe(IRecipeLayout layout, IIngredients ii) {
        IGuiIngredientGroup<IAdvancementInfo> group = layout.getIngredientsGroup(Jea.ADVANCEMENT_TYPE);
        group.addTooltipCallback((slot, type, info, list) -> {
            if (list.isEmpty()) {
                AdvancementDisplayHelper.addAdvancementTooltipToList(AdvancementInfo.get(info), list);
            }
        });
        //noinspection unchecked
        ((ICriterionInfo<ICriterionInstance>) this.criterionInfo).setRecipe(layout, this.info, this.criterionKey, this.criterion, ii);
        group.init(group.getGuiIngredients().size(), false, (ICriterionInfo.RECIPE_WIDTH / 2) - (26 / 2), 0);
        if (this.parent != null) {
            group.init(group.getGuiIngredients().size(), true, JustEnoughAdvancementsJEIPlugin.ADVANCEMENT_RECIPE_RENDERER_TINY, 5, 5, 16, 16, 0, 0);
        }
        group.set(ii);
        for (IGuiIngredient<IAdvancementInfo> ingredient : group.getGuiIngredients().values()) {
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
                    if (render == null || render instanceof AdvancementIngredientRenderer) {
                        ingredientRenderField.set(ingredient, JustEnoughAdvancementsJEIPlugin.ADVANCEMENT_RECIPE_RENDERER);
                        Rectangle2d rect = (Rectangle2d) rectangleField.get(ingredient);
                        rectangleField.set(ingredient, new Rectangle2d(rect.getX(), rect.getY(), 26, 26));
                    }
                }
            } catch (ReflectiveOperationException | NoClassDefFoundError | ClassCastException e) {
                guiIngredientClass = null;
                rectangleField = null;
            }
        }
    }

    public void draw(MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer font = mc.fontRenderer;
        int width = font.getStringPropertyWidth(this.info.getFormattedDisplayName());
        font.func_243246_a(matrixStack, this.info.getFormattedDisplayName(), (ICriterionInfo.RECIPE_WIDTH / 2f) - (width / 2f), 27, 0xFFFFFF);
        //noinspection unchecked
        ((ICriterionInfo<ICriterionInstance>) this.criterionInfo).draw(matrixStack, mc, this.info, this.criterionKey, this.criterion, mouseX, mouseY);
    }
}
