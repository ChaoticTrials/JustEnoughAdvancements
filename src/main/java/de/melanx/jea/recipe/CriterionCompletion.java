package de.melanx.jea.recipe;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;
import java.util.List;

public enum CriterionCompletion {
    
    COMPLETE("jea.criterion.complete", null),
    PARTIALLY_COMPLETE("jea.criterion.not_required", "jea.criterion.not_required_description"),
    INCOMPLETE("jea.criterion.incomplete", null);
    
    public final String translationKey;
    @Nullable
    public final String subTitle;

    CriterionCompletion(String translationKey, @Nullable String subTitle) {
        this.translationKey = translationKey;
        this.subTitle = subTitle;
    }

    public void draw(PoseStack poseStack, IDrawableStatic complete, IDrawableStatic incomplete) {
        switch (this) {
            case COMPLETE -> complete.draw(poseStack);
            case PARTIALLY_COMPLETE -> {
                complete.draw(poseStack, 0, 0, 0, 0, 0, 7);
                incomplete.draw(poseStack, 0, 0, 0, 0, 8, 0);
            }
            case INCOMPLETE -> incomplete.draw(poseStack);
        }
    }
    
    public void addTooltip(List<Component> list) {
        list.add(new TranslatableComponent(this.translationKey).withStyle(ChatFormatting.GREEN));
        if (this.subTitle != null) {
            list.add(new TranslatableComponent(this.subTitle));
        }
    }
}
