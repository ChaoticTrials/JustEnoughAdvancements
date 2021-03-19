package de.melanx.jea.recipe;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

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

    public void draw(MatrixStack matrixStack, IDrawableStatic complete, IDrawableStatic incomplete) {
        switch (this) {
            case COMPLETE:
                complete.draw(matrixStack);
                break;
            case PARTIALLY_COMPLETE:
                complete.draw(matrixStack,0, 0, 0, 0, 0, 7);
                incomplete.draw(matrixStack,0, 0, 0, 0, 8, 0);
                break;
            case INCOMPLETE:
                incomplete.draw(matrixStack);
                break;
        }
    }
    
    public void addTooltip(List<ITextComponent> list) {
        list.add(new TranslationTextComponent(this.translationKey).mergeStyle(TextFormatting.GREEN));
        if (this.subTitle != null) {
            list.add(new TranslationTextComponent(this.subTitle));
        }
    }
}
