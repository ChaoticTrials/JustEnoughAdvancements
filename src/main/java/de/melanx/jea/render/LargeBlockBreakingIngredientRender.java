package de.melanx.jea.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import io.github.noeppi_noeppi.libx.render.RenderHelperBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class LargeBlockBreakingIngredientRender extends LargeBlockIngredientRender {

    private final int breakSpeed;

    public LargeBlockBreakingIngredientRender(int breakSpeed) {
        this.breakSpeed = breakSpeed;
    }

    @Override
    protected void renderBlock(@Nonnull PoseStack poseStack, BlockState state) {
        super.renderBlock(poseStack, state);
        int breakProgress = (ClientTickHandler.ticksInGame / this.breakSpeed) % 10;
        RenderHelperBlock.renderBlockBreak(state, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), LightTexture.pack(15 ,15), OverlayTexture.NO_OVERLAY, breakProgress);
        Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
        Minecraft.getInstance().renderBuffers().crumblingBufferSource().endBatch();
    }
}
