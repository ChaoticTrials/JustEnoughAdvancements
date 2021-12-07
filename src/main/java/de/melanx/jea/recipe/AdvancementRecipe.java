package de.melanx.jea.recipe;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.AdvancementInfo;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AdvancementRecipe {

    private final AdvancementInfo info;
    @Nullable
    private final AdvancementInfo parent;

    public AdvancementRecipe(AdvancementInfo info) {
        this.info = info;
        this.parent = ClientAdvancements.getInfo(this.info.getParent());
    }

    public void setIngredients(IIngredients ii) {
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
        group.init(group.getGuiIngredients().size(), false, 62, 0);
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
        font.draw(poseStack, this.info.getFormattedDisplayName(), 75 - (width / 2f), 27, 0xFFFFFF);
        AdvancementCompletion advancementCompletion = this.getCriterionCompletion();
        poseStack.pushPose();
        poseStack.translate(129, 5, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (mouseX >= 129 && mouseX <= 145 && mouseY >= 5 && mouseY <= 5 + 16) {
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderColor(1, 1, 1, 0.5f);
            RenderSystem.setShaderTexture(0, RenderHelper.TEXTURE_WHITE);
            GuiComponent.blit(poseStack, 0, 0, 0, 0, 16, 16, 256, 256);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
        poseStack.translate(0, 0, 10);
        poseStack.scale(16 / 17f, 16 / 17f, 0);
        poseStack.translate(1, 1, 0);
        advancementCompletion.draw(poseStack, complete, incomplete);
        RenderSystem.disableBlend();
        poseStack.popPose();
        if (this.info.getDisplay() != null) {
            Component description = this.info.getDisplay().getDescription();
            List<FormattedCharSequence> lines = ComponentRenderUtils.wrapComponents(description, 130, font);
            for (int i = 0; i < lines.size(); i++) {
                font.drawShadow(poseStack, lines.get(i), 10, 50 + ((font.lineHeight + 2) * i), 0xFFFFFF);
            }
        }
        mc.renderBuffers().bufferSource().endBatch();
    }

    public List<Component> getTooltip(double mouseX, double mouseY) {
        ArrayList<Component> tooltip = new ArrayList<>();
        if (mouseX >= 129 && mouseX <= 145 && mouseY >= 5 && mouseY <= 5 + 16) {
            this.getCriterionCompletion().addTooltip(tooltip, this.info);
        }
        return tooltip;
    }
    
    private AdvancementCompletion getCriterionCompletion() {
        AdvancementProgress progress = ClientAdvancementProgress.getProgress(this.info.getId());
        if (progress != null) {
            if (progress.isDone()) {
                return AdvancementCompletion.COMPLETE;
            } else if (progress.hasProgress()) {
                return AdvancementCompletion.PARTIALLY_COMPLETE;
            } else {
                return AdvancementCompletion.INCOMPLETE;
            }
        } else {
            return AdvancementCompletion.INCOMPLETE;
        }
    }
}
