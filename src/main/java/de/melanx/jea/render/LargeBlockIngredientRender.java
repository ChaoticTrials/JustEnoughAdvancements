package de.melanx.jea.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class LargeBlockIngredientRender implements IIngredientRenderer<ItemStack> {
    
    public static final int SIZE = 48;
    private static final float SCALE = (float) Math.sqrt((((double) SIZE) * ((double) SIZE)) / 2);
    private static final double TRANSLATE_DOWN = (Math.sin(Math.toRadians(-22.5)) * (SIZE / 2d)) + SIZE;
    
    @Override
    public void render(@Nonnull PoseStack poseStack, int x, int y, @Nullable ItemStack stack) {
        if (stack != null && stack.getItem() instanceof BlockItem item) {
            BlockState state = item.getBlock().defaultBlockState();
            if (!state.isAir()) {
                poseStack.pushPose();
                poseStack.translate(x, y, 0);
                modifyPoseStack(poseStack);
                this.renderBlock(poseStack, state);
                poseStack.popPose();
            }
        }
    }
    
    public static void modifyPoseStack(@Nonnull PoseStack poseStack) {
        poseStack.translate(SIZE, TRANSLATE_DOWN, 100);
        poseStack.translate(0.5, 0, 0.5);
        poseStack.translate(-0.5, 0, -0.5);
        poseStack.scale(SCALE, -SCALE, SCALE);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(22.5f));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180 + 45));
    }
    
    protected void renderBlock(@Nonnull PoseStack poseStack, BlockState state) {
        //noinspection deprecation
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
    }

    @Nonnull
    @Override
    public List<Component> getTooltip(@Nonnull ItemStack stack, @Nonnull TooltipFlag flag) {
        return JustEnoughAdvancementsJEIPlugin.runtimeResult(runtime -> runtime.getIngredientManager().getIngredientRenderer(stack)).getTooltip(stack, flag);
    }

    @Nonnull
    @Override
    public Font getFontRenderer(@Nonnull Minecraft mc, @Nonnull ItemStack stack) {
        return JustEnoughAdvancementsJEIPlugin.runtimeResult(runtime -> runtime.getIngredientManager().getIngredientRenderer(stack)).getFontRenderer(mc, stack);
    }
}
