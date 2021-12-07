package de.melanx.jea.recipe;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.JustEnoughAdvancements;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class AdvancementCategory implements IRecipeCategory<AdvancementRecipe> {
    
    public static final ResourceLocation UID = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "advancement");
    
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic complete;
    private final IDrawableStatic incomplete;
    
    public AdvancementCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 126);
        this.icon = guiHelper.createDrawable(new ResourceLocation("minecraft", "textures/gui/toasts.png"), 236, 2, 16, 16);
        this.complete = guiHelper.createDrawable(new ResourceLocation("minecraft", "textures/gui/container/beacon.png"), 91, 222, 15, 15);
        this.incomplete = guiHelper.createDrawable(new ResourceLocation("minecraft", "textures/gui/container/beacon.png"), 113, 222, 15, 15);
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends AdvancementRecipe> getRecipeClass() {
        return AdvancementRecipe.class;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return new TranslatableComponent("jea.category.advancement");
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
    public void setIngredients(@Nonnull AdvancementRecipe recipe, @Nonnull IIngredients ii) {
        recipe.setIngredients(ii);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull AdvancementRecipe recipe, @Nonnull IIngredients ii) {
        recipe.setRecipe(layout, ii);
    }

    @Override
    public void draw(AdvancementRecipe recipe, @Nonnull PoseStack poseStack, double mouseX, double mouseY) {
        recipe.draw(poseStack, mouseX, mouseY, this.complete, this.incomplete);
    }

    @Nonnull
    @Override
    public List<Component> getTooltipStrings(@Nonnull AdvancementRecipe recipe, double mouseX, double mouseY) {
        return recipe.getTooltip(mouseX, mouseY);
    }
}
