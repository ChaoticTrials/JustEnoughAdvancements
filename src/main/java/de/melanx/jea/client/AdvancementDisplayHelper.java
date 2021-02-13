package de.melanx.jea.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.AdvancementInfo;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.advancements.AdvancementEntryGui;
import net.minecraft.client.gui.advancements.AdvancementState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class AdvancementDisplayHelper {

    public static void renderAdvancement(MatrixStack matrixStack, IRenderTypeBuffer buffer, AdvancementInfo info, AdvancementState state, int x, int y) {
        matrixStack.push();
        RenderHelper.resetColor();
        Minecraft.getInstance().getTextureManager().bindTexture(AdvancementEntryGui.WIDGETS);
        AbstractGui.blit(matrixStack, x, y, 0, info.getDisplay().getFrame().getIcon(), 128 + (state.getId() * 26), 26, 26, 256, 256);
        matrixStack.translate(0, 0, 20);
        //noinspection deprecation
        RenderSystem.pushMatrix();
        //noinspection deprecation
        RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(info.getDisplay().getIcon(), x + 5, y + 5);
        //noinspection deprecation
        RenderSystem.popMatrix();
        RenderHelper.resetColor();
        matrixStack.pop();
    }
    
    public static void addAdvancementTooltipToList(AdvancementInfo info, List<ITextComponent> list) {
        list.add(info.getFormattedDisplayName());
        list.add(info.getDisplay().getDescription());
        AdvancementProgress progress = ClientAdvancementProgress.getProgress(info.id);
        if (progress != null && progress.getPercent() >= 1) {
            list.add(info.getDisplay().getFrame().getTranslatedToast().deepCopy().mergeStyle(TextFormatting.YELLOW));
        } else if (progress != null && progress.getPercent() < 1 && progress.getPercent() > 0) {
            list.add(new TranslationTextComponent("jea.advancement.partial", Integer.toString(MathHelper.clamp(Math.round(progress.getPercent() * 100), 1, 99))).mergeStyle(TextFormatting.YELLOW));
        } else {
            list.add(new TranslationTextComponent("jea.advancement.incomplete").mergeStyle(TextFormatting.YELLOW));
        }
    }
}
