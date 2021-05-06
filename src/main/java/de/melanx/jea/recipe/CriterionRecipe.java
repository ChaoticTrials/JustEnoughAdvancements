package de.melanx.jea.recipe;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.AdvancementInfo;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.client.AdvancementDisplayHelper;
import de.melanx.jea.client.ClientAdvancementProgress;
import de.melanx.jea.client.ClientAdvancements;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CriterionRecipe {

    private final AdvancementInfo info;
    private final String criterionKey;
    private final ICriterionInstance criterion;
    private final ICriterionInfo<?> criterionInfo;
    @Nullable
    private final AdvancementInfo parent;
    private final List<String> criterionGroup;

    public CriterionRecipe(AdvancementInfo info, String criterionKey, ICriterionInfo<?> criterionInfo, List<String> criterionGroup) {
        this.info = info;
        this.criterionKey = criterionKey;
        this.criterionInfo = criterionInfo;
        this.criterion = info.getCriteria().get(criterionKey).getCriterionInstance();
        this.parent = ClientAdvancements.getInfo(this.info.getParent());
        this.criterionGroup = ImmutableList.copyOf(criterionGroup);
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
                AdvancementDisplayHelper.addAdvancementTooltipToList(AdvancementInfo.get(info), list, ITooltipFlag.TooltipFlags.NORMAL);
            }
        });
        //noinspection unchecked
        ((ICriterionInfo<ICriterionInstance>) this.criterionInfo).setRecipe(layout, this.info, this.criterionKey, this.criterion, ii);
        group.init(group.getGuiIngredients().size(), false, (ICriterionInfo.RECIPE_WIDTH / 2) - (26 / 2), 0);
        if (this.parent != null) {
            group.init(group.getGuiIngredients().size(), true, Jea.ADVANCEMENT_RECIPE_RENDERER_TINY, 5, 5, 16, 16, 0, 0);
        }
        group.set(ii);
        RecipeRenderSizeAdjust.changeRecipeSizes(layout, VanillaTypes.ITEM);
        RecipeRenderSizeAdjust.changeRecipeSizes(layout, Jea.ADVANCEMENT_TYPE);
        
    }

    public void draw(MatrixStack matrixStack, double mouseX, double mouseY, IDrawableStatic complete, IDrawableStatic incomplete) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer font = mc.fontRenderer;
        int width = font.getStringPropertyWidth(this.info.getFormattedDisplayName());
        font.func_243246_a(matrixStack, this.info.getFormattedDisplayName(), (ICriterionInfo.RECIPE_WIDTH / 2f) - (width / 2f), 27, 0xFFFFFF);
        CriterionCompletion criterionCompletion = this.getCriterionCompletion();
        matrixStack.push();
        matrixStack.translate(ICriterionInfo.RECIPE_WIDTH - 16 - 5, 5, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        //noinspection deprecation
        RenderSystem.color4f(1, 1, 1, 1);
        if (mouseX >= ICriterionInfo.RECIPE_WIDTH - 16 - 5 && mouseX <= ICriterionInfo.RECIPE_WIDTH - 5
                && mouseY >= 5 && mouseY <= 5 + 16) {
            //noinspection deprecation
            RenderSystem.disableLighting();
            RenderSystem.disableDepthTest();
            //noinspection deprecation
            RenderSystem.color4f(1, 1, 1, 0.5f);
            mc.getTextureManager().bindTexture(RenderHelper.TEXTURE_WHITE);
            AbstractGui.blit(matrixStack, 0, 0, 0, 0, 16, 16, 256, 256);
            //noinspection deprecation
            RenderSystem.color4f(1, 1, 1, 1);
        }
        matrixStack.translate(0, 0, 10);
        matrixStack.scale(16 / 17f, 16 / 17f, 0);
        matrixStack.translate(1, 1, 0);
        criterionCompletion.draw(matrixStack, complete, incomplete);
        RenderSystem.disableBlend();
        matrixStack.pop();
        //noinspection unchecked
        ((ICriterionInfo<ICriterionInstance>) this.criterionInfo).draw(matrixStack, mc.getRenderTypeBuffers().getBufferSource(), mc, this.info, this.criterionKey, this.criterion, mouseX, mouseY);
        mc.getRenderTypeBuffers().getBufferSource().finish();
    }

    public List<ITextComponent> getTooltip(double mouseX, double mouseY) {
        ArrayList<ITextComponent> tooltip = new ArrayList<>();
        if (mouseX >= ICriterionInfo.RECIPE_WIDTH - 16 - 5 && mouseX <= ICriterionInfo.RECIPE_WIDTH - 5
                && mouseY >= 5 && mouseY <= 5 + 16) {
            this.getCriterionCompletion().addTooltip(tooltip);
        } else if (mouseY > ICriterionInfo.SPACE_TOP) {
            // Don't let criterion infos add their tooltip into the header.
            //noinspection unchecked
            ((ICriterionInfo<ICriterionInstance>) this.criterionInfo).addTooltip(tooltip, this.info, this.criterionKey, this.criterion, mouseX, mouseY);
        }
        return tooltip;
    }
    
    private CriterionCompletion getCriterionCompletion() {
        AdvancementProgress progress = ClientAdvancementProgress.getProgress(this.info.getId());
        CriterionCompletion completion = CriterionCompletion.INCOMPLETE;
        if (progress != null) {
            CriterionProgress criterionProgress = progress.getCriterionProgress(this.criterionKey);
            if (criterionProgress != null && criterionProgress.isObtained()) {
                completion = CriterionCompletion.COMPLETE;
            } else if (this.criterionGroup.stream()
                    .map(progress::getCriterionProgress)
                    .anyMatch(x -> x != null && x.isObtained())) {
                completion = CriterionCompletion.PARTIALLY_COMPLETE;
            }
        }
        return completion;
    }
}
