package de.melanx.jea.ingredient;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.client.ClientAdvancementProgress;
import de.melanx.jea.client.AdvancementDisplayHelper;
import de.melanx.jea.AdvancementInfo;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.advancements.AdvancementState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AdvancementIngredientRenderer implements IIngredientRenderer<IAdvancementInfo> {
    
    @Override
    public void render(@Nonnull MatrixStack matrixStack, int x, int y, @Nullable IAdvancementInfo info) {
        if (info != null) {
            Minecraft mc = Minecraft.getInstance();
            IRenderTypeBuffer buffer = mc.getRenderTypeBuffers().getBufferSource();
            AdvancementState state = AdvancementState.UNOBTAINED;
            AdvancementProgress progress = ClientAdvancementProgress.getProgress(mc, info.getId());
            if (progress != null && progress.getPercent() >= 1) {
                state = AdvancementState.OBTAINED;
            }
            
            matrixStack.push();
            matrixStack.translate(x, y, 0);
            matrixStack.scale(16/24f, 16/24f, 1);
            matrixStack.translate(-1, -1, 0);

            AdvancementDisplayHelper.renderAdvancement(matrixStack, buffer, AdvancementInfo.get(info), state, 0, 0);
            
            matrixStack.pop();
        }
    }

    @Nonnull
    @Override
    public List<ITextComponent> getTooltip(@Nonnull IAdvancementInfo info, @Nonnull ITooltipFlag flag) {
        List<ITextComponent> list = new ArrayList<>();
        AdvancementDisplayHelper.addAdvancementTooltipToList(AdvancementInfo.get(info), list, flag);
        return list;
    }
}
