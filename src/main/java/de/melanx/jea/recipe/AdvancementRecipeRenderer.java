package de.melanx.jea.recipe;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.AdvancementInfo;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.client.AdvancementDisplayHelper;
import de.melanx.jea.client.ClientAdvancementProgress;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AdvancementRecipeRenderer implements IIngredientRenderer<IAdvancementInfo> {

    @Override
    public void render(@Nonnull GuiGraphics graphics, @Nullable IAdvancementInfo info) {
        if (info != null) {
            Minecraft mc = Minecraft.getInstance();
            AdvancementWidgetType state = AdvancementWidgetType.UNOBTAINED;
            AdvancementProgress progress = ClientAdvancementProgress.getProgress(mc, info.getId());
            if (progress != null && progress.getPercent() >= 1) {
                state = AdvancementWidgetType.OBTAINED;
            }
            
            graphics.pose().pushPose();
            this.transform(graphics.pose());
            AdvancementDisplayHelper.renderAdvancement(graphics, AdvancementInfo.get(info), state);
            graphics.pose().popPose();
        }
    }

    @Nonnull
    @Override
    public List<Component> getTooltip(@Nonnull IAdvancementInfo info, @Nonnull TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        AdvancementDisplayHelper.addAdvancementTooltipToList(AdvancementInfo.get(info), list, flag);
        return list;
    }
    
    protected void transform(PoseStack poseStack) {
        
    }

    @Override
    public int getWidth() {
        return 26;
    }

    @Override
    public int getHeight() {
        return 26;
    }
}
