package de.melanx.jea.recipe;

import de.melanx.jea.AdvancementInfo;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public enum AdvancementCompletion {
    
    COMPLETE,
    PARTIALLY_COMPLETE,
    INCOMPLETE;

    public void draw(GuiGraphics graphics, IDrawableStatic complete, IDrawableStatic incomplete) {
        switch (this) {
            case COMPLETE -> complete.draw(graphics);
            case PARTIALLY_COMPLETE -> {
                complete.draw(graphics, 0, 0, 0, 0, 0, 7);
                incomplete.draw(graphics, 0, 0, 0, 0, 8, 0);
            }
            case INCOMPLETE -> incomplete.draw(graphics);
        }
    }
    
    public void addTooltip(List<Component> list, AdvancementInfo info) {
        switch (this) {
            case COMPLETE -> list.add(info.getDisplay().getFrame().getDisplayName().copy().withStyle(ChatFormatting.GREEN));
            case PARTIALLY_COMPLETE -> list.add(Component.translatable("jea.advancement.partial.simple").withStyle(ChatFormatting.YELLOW));
            case INCOMPLETE -> list.add(Component.translatable("jea.advancement.incomplete").withStyle(ChatFormatting.RED));
        }
    }
}
