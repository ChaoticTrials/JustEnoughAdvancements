package de.melanx.jea.recipe;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.JustEnoughAdvancements;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class AdvancementCategory implements IRecipeCategory<CriterionRecipe> {
    
    public static final ResourceLocation UID = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "advancement");
    
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable complete;
    private final IDrawable incomplete;
    private final String localizedName;
    
    public AdvancementCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(ICriterionInfo.RECIPE_WIDTH, ICriterionInfo.RECIPE_HEIGHT + ICriterionInfo.SPACE_TOP);
        this.icon = guiHelper.createDrawable(new ResourceLocation("minecraft", "textures/gui/toasts.png"), 236, 2, 16, 16);
        this.complete = guiHelper.createDrawable(new ResourceLocation("minecraft", "textures/gui/container/beacon.png"), 91, 222, 15, 15);
        this.incomplete = guiHelper.createDrawable(new ResourceLocation("minecraft", "textures/gui/container/beacon.png"), 113, 222, 15, 15);
        this.localizedName = I18n.format("jea.category.advancement");
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends CriterionRecipe> getRecipeClass() {
        return CriterionRecipe.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return this.localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(@Nonnull CriterionRecipe recipe, @Nonnull IIngredients ii) {
        recipe.setIngredients(ii);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull CriterionRecipe recipe, @Nonnull IIngredients ii) {
        recipe.setRecipe(layout, ii);
    }

    @Override
    public void draw(CriterionRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
        recipe.draw(matrixStack, mouseX, mouseY, this.complete, this.incomplete);
    }

    @Nonnull
    @Override
    public List<ITextComponent> getTooltipStrings(@Nonnull CriterionRecipe recipe, double mouseX, double mouseY) {
        return recipe.getTooltip(mouseX, mouseY);
    }
}
