package de.melanx.jea.ingredient;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.JEAPlugin;
import de.melanx.jea.JustEnoughAdvancements;
import de.melanx.jea.client.data.AdvancementInfo;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.gui.textures.Textures;
import mezz.jei.util.Translator;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static mezz.jei.plugins.jei.info.IngredientInfoRecipeCategory.recipeHeight;
import static mezz.jei.plugins.jei.info.IngredientInfoRecipeCategory.recipeWidth;

public class AdvancementCategory implements IRecipeCategory<AdvancementInfo> {
    public static final ResourceLocation UID = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "advancement");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotBackground;
    private final String localizedName;

    public AdvancementCategory(IGuiHelper guiHelper, Textures textures) {
        this.background = guiHelper.createBlankDrawable(recipeWidth, recipeHeight);
        this.icon = textures.getInfoIcon();
        this.slotBackground = guiHelper.getSlotDrawable();
        this.localizedName = Translator.translateToLocal("gui.jea.category.advancements");
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends AdvancementInfo> getRecipeClass() {
        return AdvancementInfo.class;
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
    public void setIngredients(@Nonnull AdvancementInfo info, @Nonnull IIngredients ingredients) {
        ingredients.setInput(JEAPlugin.ADVANCEMENT_TYPE, info);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull AdvancementInfo info, @Nonnull IIngredients ingredients) {

    }

    @Override
    public void draw(@Nonnull AdvancementInfo recipe, @Nonnull MatrixStack ms, double mouseX, double mouseY) {

    }
}
