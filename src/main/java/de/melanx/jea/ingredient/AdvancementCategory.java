package de.melanx.jea.ingredient;

import de.melanx.jea.JustEnoughAdvancements;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.advancements.Advancement;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class AdvancementCategory implements IRecipeCategory<Advancement> {
    public static final ResourceLocation UID = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "advancement");

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends Advancement> getRecipeClass() {
        return Advancement.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return null;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return null;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(@Nonnull Advancement advancement, @Nonnull IIngredients iIngredients) {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout iRecipeLayout, @Nonnull Advancement advancement, @Nonnull IIngredients iIngredients) {

    }
}
