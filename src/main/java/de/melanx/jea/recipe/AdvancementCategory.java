package de.melanx.jea.recipe;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.JustEnoughAdvancements;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AdvancementCategory implements IRecipeCategory<AdvancementRecipe> {
    
    public static final RecipeType<AdvancementRecipe> TYPE = RecipeType.create(JustEnoughAdvancements.getInstance().modid, "advancement", AdvancementRecipe.class);
    
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
    public RecipeType<AdvancementRecipe> getRecipeType() {
        return TYPE;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName(@Nonnull AdvancementRecipe recipe) {
        return recipe.id();
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("jea.category.advancement");
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull AdvancementRecipe recipe, @Nonnull IFocusGroup focus) {
        recipe.setRecipe(builder, focus);
    }

    @Override
    public void draw(@Nonnull AdvancementRecipe recipe, @Nonnull IRecipeSlotsView slots, @Nonnull PoseStack poseStack, double mouseX, double mouseY) {
        recipe.draw(slots, poseStack, mouseX, mouseY, this.complete, this.incomplete);
    }

    @Nonnull
    @Override
    public List<Component> getTooltipStrings(@Nonnull AdvancementRecipe recipe, @Nonnull IRecipeSlotsView slots, double mouseX, double mouseY) {
        return recipe.getTooltip(slots, mouseX, mouseY);
    }
}
