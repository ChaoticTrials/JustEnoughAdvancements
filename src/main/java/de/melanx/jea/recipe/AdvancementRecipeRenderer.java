package de.melanx.jea.recipe;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.AdvancementInfo;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.client.AdvancementDisplayHelper;
import de.melanx.jea.client.ClientAdvancementProgress;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AdvancementRecipeRenderer implements IIngredientRenderer<IAdvancementInfo> {
    
    @Override
    public void render(@Nonnull PoseStack poseStack, int x, int y, @Nullable IAdvancementInfo info) {
        if (info != null) {
            Minecraft mc = Minecraft.getInstance();
            MultiBufferSource buffer = mc.renderBuffers().bufferSource();
            AdvancementWidgetType state = AdvancementWidgetType.UNOBTAINED;
            AdvancementProgress progress = ClientAdvancementProgress.getProgress(mc, info.getId());
            if (progress != null && progress.getPercent() >= 1) {
                state = AdvancementWidgetType.OBTAINED;
            }
            
            AdvancementDisplayHelper.renderAdvancement(poseStack, buffer, AdvancementInfo.get(info), state, x, y);
        }
    }

    @Nonnull
    @Override
    public List<Component> getTooltip(@Nonnull IAdvancementInfo info, @Nonnull TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        AdvancementDisplayHelper.addAdvancementTooltipToList(AdvancementInfo.get(info), list, flag);
        return list;
    }
}
