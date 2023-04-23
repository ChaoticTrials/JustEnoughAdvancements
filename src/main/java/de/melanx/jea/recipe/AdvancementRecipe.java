package de.melanx.jea.recipe;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.AdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.client.ClientAdvancementProgress;
import de.melanx.jea.client.ClientAdvancements;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.moddingx.libx.render.RenderHelper;

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

    public ResourceLocation id() {
        return this.info.id;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focus) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 62, 0)
                .addIngredient(Jea.ADVANCEMENT_TYPE, this.info)
                .setCustomRenderer(Jea.ADVANCEMENT_TYPE, Jea.ADVANCEMENT_RECIPE_RENDERER);
        
        if (this.parent != null) {
            builder.addSlot(RecipeIngredientRole.INPUT, 5, 5)
                    .addIngredient(Jea.ADVANCEMENT_TYPE, this.parent)
                    .setCustomRenderer(Jea.ADVANCEMENT_TYPE, Jea.ADVANCEMENT_RECIPE_RENDERER_TINY);
        }
    }

    public void draw(IRecipeSlotsView slots, PoseStack poseStack, double mouseX, double mouseY, IDrawableStatic complete, IDrawableStatic incomplete) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        int width = font.width(this.info.getFormattedDisplayName());
        font.draw(poseStack, this.info.getFormattedDisplayName(), 75 - (width / 2f), 29, 0xFFFFFF);
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

    public List<Component> getTooltip(IRecipeSlotsView slots, double mouseX, double mouseY) {
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
