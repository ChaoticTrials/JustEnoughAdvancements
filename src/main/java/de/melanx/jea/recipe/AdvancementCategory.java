package de.melanx.jea.recipe;

import de.melanx.jea.JustEnoughAdvancements;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AdvancementCategory implements IRecipeCategory<AdvancementRecipe> {
    
    public static final RecipeType<AdvancementRecipe> TYPE = RecipeType.create(JustEnoughAdvancements.getInstance().modid, "advancement", AdvancementRecipe.class);

    private final IDrawable icon;
    private final IDrawableStatic complete;
    private final IDrawableStatic incomplete;
    
    public AdvancementCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.drawableBuilder(ResourceLocation.fromNamespaceAndPath("jea", "textures/gui/category_icon.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
        this.complete = guiHelper.drawableBuilder(ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/sprites/container/beacon/confirm.png"), 1,4, 14, 12).setTextureSize(18, 18).build();
        this.incomplete = guiHelper.drawableBuilder(ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/sprites/container/beacon/cancel.png"), 2,3, 13, 13).setTextureSize(18, 18).build();
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

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 126;
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
    public void draw(@Nonnull AdvancementRecipe recipe, @Nonnull IRecipeSlotsView slots, @Nonnull GuiGraphics graphics, double mouseX, double mouseY) {
        recipe.draw(slots, graphics, mouseX, mouseY, this.complete, this.incomplete);
    }

    @Override
    public void getTooltip(@Nonnull ITooltipBuilder tooltip, @Nonnull AdvancementRecipe recipe, @Nonnull IRecipeSlotsView slots, double mouseX, double mouseY) {
        recipe.getTooltip(slots, mouseX, mouseY).forEach(tooltip::add);
    }
}
