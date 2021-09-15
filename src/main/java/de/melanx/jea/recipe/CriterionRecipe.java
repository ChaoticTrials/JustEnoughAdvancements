package de.melanx.jea.recipe;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CriterionRecipe {

    private final AdvancementInfo info;
    private final String criterionKey;
    private final CriterionTriggerInstance criterion;
    private final ICriterionInfo<?> criterionInfo;
    @Nullable
    private final AdvancementInfo parent;
    private final List<String> criterionGroup;

    public CriterionRecipe(AdvancementInfo info, String criterionKey, ICriterionInfo<?> criterionInfo, List<String> criterionGroup) {
        this.info = info;
        this.criterionKey = criterionKey;
        this.criterionInfo = criterionInfo;
        this.criterion = info.getCriteria().get(criterionKey).getTrigger();
        this.parent = ClientAdvancements.getInfo(this.info.getParent());
        this.criterionGroup = ImmutableList.copyOf(criterionGroup);
    }

    public void setIngredients(IIngredients ii) {
        //noinspection unchecked
        ((ICriterionInfo<CriterionTriggerInstance>) this.criterionInfo).setIngredients(this.info, this.criterionKey, this.criterion, ii);
        ii.setOutputs(Jea.ADVANCEMENT_TYPE, List.of(this.info));
        if (this.parent != null) {
            ii.setInputs(Jea.ADVANCEMENT_TYPE, List.of(this.parent));
        }
    }

    public void setRecipe(IRecipeLayout layout, IIngredients ii) {
        IGuiIngredientGroup<IAdvancementInfo> group = layout.getIngredientsGroup(Jea.ADVANCEMENT_TYPE);
        group.addTooltipCallback((slot, type, info, list) -> {
            if (list.isEmpty()) {
                AdvancementDisplayHelper.addAdvancementTooltipToList(AdvancementInfo.get(info), list, TooltipFlag.Default.NORMAL);
            }
        });
        //noinspection unchecked
        ((ICriterionInfo<CriterionTriggerInstance>) this.criterionInfo).setRecipe(layout, this.info, this.criterionKey, this.criterion, ii);
        group.init(group.getGuiIngredients().size(), false, (ICriterionInfo.RECIPE_WIDTH / 2) - (26 / 2), 0);
        if (this.parent != null) {
            group.init(group.getGuiIngredients().size(), true, Jea.ADVANCEMENT_RECIPE_RENDERER_TINY, 5, 5, 16, 16, 0, 0);
        }
        group.set(ii);
        RecipeRenderSizeAdjust.changeRecipeSizes(layout, VanillaTypes.ITEM);
        RecipeRenderSizeAdjust.changeRecipeSizes(layout, Jea.ADVANCEMENT_TYPE);
        
    }

    public void draw(PoseStack poseStack, double mouseX, double mouseY, IDrawableStatic complete, IDrawableStatic incomplete) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        int width = font.width(this.info.getFormattedDisplayName());
        font.draw(poseStack, this.info.getFormattedDisplayName(), (ICriterionInfo.RECIPE_WIDTH / 2f) - (width / 2f), 27, 0xFFFFFF);
        CriterionCompletion criterionCompletion = this.getCriterionCompletion();
        poseStack.pushPose();
        poseStack.translate(ICriterionInfo.RECIPE_WIDTH - 16 - 5, 5, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (mouseX >= ICriterionInfo.RECIPE_WIDTH - 16 - 5 && mouseX <= ICriterionInfo.RECIPE_WIDTH - 5
                && mouseY >= 5 && mouseY <= 5 + 16) {
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderColor(1, 1, 1, 0.5f);
            RenderSystem.setShaderTexture(0, RenderHelper.TEXTURE_WHITE);
            GuiComponent.blit(poseStack, 0, 0, 0, 0, 16, 16, 256, 256);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
        poseStack.translate(0, 0, 10);
        poseStack.scale(16 / 17f, 16 / 17f, 0);
        poseStack.translate(1, 1, 0);
        criterionCompletion.draw(poseStack, complete, incomplete);
        RenderSystem.disableBlend();
        poseStack.popPose();
        //noinspection unchecked
        ((ICriterionInfo<CriterionTriggerInstance>) this.criterionInfo).draw(poseStack, mc.renderBuffers().bufferSource(), mc, this.info, this.criterionKey, this.criterion, mouseX, mouseY);
        mc.renderBuffers().bufferSource().endBatch();
    }

    public List<Component> getTooltip(double mouseX, double mouseY) {
        ArrayList<Component> tooltip = new ArrayList<>();
        if (mouseX >= ICriterionInfo.RECIPE_WIDTH - 16 - 5 && mouseX <= ICriterionInfo.RECIPE_WIDTH - 5
                && mouseY >= 5 && mouseY <= 5 + 16) {
            this.getCriterionCompletion().addTooltip(tooltip);
        } else if (mouseY > ICriterionInfo.SPACE_TOP) {
            // Don't let criterion infos add their tooltip into the header.
            //noinspection unchecked
            ((ICriterionInfo<CriterionTriggerInstance>) this.criterionInfo).addTooltip(tooltip, this.info, this.criterionKey, this.criterion, mouseX, mouseY);
        }
        return tooltip;
    }
    
    private CriterionCompletion getCriterionCompletion() {
        AdvancementProgress progress = ClientAdvancementProgress.getProgress(this.info.getId());
        CriterionCompletion completion = CriterionCompletion.INCOMPLETE;
        if (progress != null) {
            CriterionProgress criterionProgress = progress.getCriterion(this.criterionKey);
            if (criterionProgress != null && criterionProgress.isDone()) {
                completion = CriterionCompletion.COMPLETE;
            } else if (this.criterionGroup.stream()
                    .map(progress::getCriterion)
                    .anyMatch(x -> x != null && x.isDone())) {
                completion = CriterionCompletion.PARTIALLY_COMPLETE;
            }
        }
        return completion;
    }
}
