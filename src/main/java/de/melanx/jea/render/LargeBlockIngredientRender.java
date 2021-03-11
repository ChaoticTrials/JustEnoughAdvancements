package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class LargeBlockIngredientRender implements IIngredientRenderer<ItemStack> {
    
    public static final int SIZE = 48;
    private static final float SCALE = (float) Math.sqrt((((double) SIZE) * ((double) SIZE)) / 2);
    private static final double TRANSLATE_DOWN = (Math.sin(Math.toRadians(-22.5)) * (SIZE / 2d)) + SIZE;
    
    @Override
    public void render(@Nonnull MatrixStack matrixStack, int x, int y, @Nullable ItemStack stack) {
        if (stack != null && stack.getItem() instanceof BlockItem) {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            //noinspection deprecation
            if (!state.isAir()) {
                matrixStack.push();
                matrixStack.translate(x, y, 0);
                modifyMatrixStack(matrixStack);
                this.renderBlock(matrixStack, state);
                matrixStack.pop();
            }
        }
    }
    
    public static void modifyMatrixStack(@Nonnull MatrixStack matrixStack) {
        matrixStack.translate(SIZE, TRANSLATE_DOWN, 100);
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.translate(-0.5, 0, -0.5);
        matrixStack.scale(SCALE, -SCALE, SCALE);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(22.5f));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180 + 45));
    }
    
    protected void renderBlock(@Nonnull MatrixStack matrixStack, BlockState state) {
        //noinspection deprecation
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(state, matrixStack, Minecraft.getInstance().getRenderTypeBuffers().getBufferSource(), LightTexture.packLight(15 ,15), OverlayTexture.NO_OVERLAY);
        Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish();
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
