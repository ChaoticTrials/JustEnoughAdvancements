package de.melanx.jea.ingredient;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AdvancementIngredientRenderer implements IIngredientRenderer<AdvancementInfo> {
    @Override
    public void render(@Nonnull MatrixStack ms, int x, int y, @Nullable AdvancementInfo ingredient) {
        if (ingredient != null) {
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer font = this.getFontRenderer(minecraft, ingredient);
            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            //noinspection ConstantConditions
            itemRenderer.renderItemAndEffectIntoGUI(null, ingredient.display, x, y);
            itemRenderer.renderItemOverlayIntoGUI(font, ingredient.display, x, y, null);
        }
    }

    @Nonnull
    @Override
    public List<ITextComponent> getTooltip(@Nonnull AdvancementInfo ingredient, @Nonnull ITooltipFlag flag) {
        List<ITextComponent> list = new ArrayList<>();
        list.add(ingredient.translation);
        return list;
    }
}
