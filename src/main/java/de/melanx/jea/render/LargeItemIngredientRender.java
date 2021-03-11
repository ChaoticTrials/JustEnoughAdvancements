package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class LargeItemIngredientRender implements IIngredientRenderer<ItemStack> {

    public final int size;
    public final float rotationDegrees;

    public LargeItemIngredientRender(int size, float rotationDegrees) {
        this.size = size;
        this.rotationDegrees = rotationDegrees;
    }
    
    @Override
    public void render(@Nonnull MatrixStack matrixStack, int x, int y, @Nullable ItemStack stack) {
        if (stack != null) {
            matrixStack.push();
            int half = this.size / 2;
            matrixStack.translate(x + half, y + half, -(this.size  * (this.size / 16d)));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(this.rotationDegrees));
            matrixStack.translate(-half, -half, 0);
            matrixStack.scale(this.size / 16f, this.size / 16f, this.size / 16f);
            //noinspection deprecation
            RenderSystem.pushMatrix();
            //noinspection deprecation
            RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
            Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(stack, 0, 0);
            //noinspection deprecation
            RenderSystem.popMatrix();
            matrixStack.pop();
        }
    }

    @Nonnull
    @Override
    public List<ITextComponent> getTooltip(@Nonnull ItemStack stack, @Nonnull ITooltipFlag flag) {
        return JustEnoughAdvancementsJEIPlugin.runtimeResult(runtime -> runtime.getIngredientManager().getIngredientRenderer(stack)).getTooltip(stack, flag);
    }

    @Nonnull
    @Override
    public FontRenderer getFontRenderer(@Nonnull Minecraft mc, @Nonnull ItemStack stack) {
        return JustEnoughAdvancementsJEIPlugin.runtimeResult(runtime -> runtime.getIngredientManager().getIngredientRenderer(stack)).getFontRenderer(mc, stack);
    }
}
