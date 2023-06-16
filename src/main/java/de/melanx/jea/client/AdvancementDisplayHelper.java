package de.melanx.jea.client;

import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.AdvancementInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.TooltipFlag;
import org.moddingx.libx.render.RenderHelper;

import java.util.List;

public class AdvancementDisplayHelper {

    public static void renderAdvancement(GuiGraphics graphics, AdvancementInfo info, AdvancementWidgetType state) {
        graphics.pose().pushPose();
        RenderHelper.resetColor();
        graphics.blit(AdvancementWidget.WIDGETS_LOCATION, 0, 0, 0, info.getDisplay().getFrame().getTexture(), 128 + (state.getIndex() * 26), 26, 26, 256, 256);
        graphics.pose().translate(0, 0, 20);
        graphics.renderFakeItem(info.getDisplay().getIcon(), 5, 5);
        RenderHelper.resetColor();
        graphics.renderItemDecorations(Minecraft.getInstance().font, info.getDisplay().getIcon(), 5, 5);
        graphics.pose().popPose();
        RenderSystem.applyModelViewMatrix();
    }
    
    public static void addAdvancementTooltipToList(AdvancementInfo info, List<Component> list, TooltipFlag flag) {
        list.add(info.getFormattedDisplayName());
        list.add(info.getDisplay().getDescription());
        AdvancementProgress progress = ClientAdvancementProgress.getProgress(info.id);
        if (progress != null && progress.getPercent() >= 1) {
            list.add(info.getDisplay().getFrame().getDisplayName().copy().withStyle(ChatFormatting.YELLOW));
        } else if (progress != null && progress.getPercent() < 1 && progress.getPercent() > 0) {
            list.add(Component.translatable("jea.advancement.partial", Mth.clamp(Math.round(progress.getPercent() * 100), 1, 99) + "%").withStyle(ChatFormatting.YELLOW));
        } else {
            list.add(Component.translatable("jea.advancement.incomplete").withStyle(ChatFormatting.YELLOW));
        }
        if (flag.isAdvanced()) {
            list.add(Component.literal(info.getId().toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
