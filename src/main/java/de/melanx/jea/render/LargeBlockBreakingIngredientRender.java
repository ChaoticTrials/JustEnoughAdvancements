package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import io.github.noeppi_noeppi.libx.render.RenderHelperBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;

import javax.annotation.Nonnull;

public class LargeBlockBreakingIngredientRender extends LargeBlockIngredientRender {

    private final int breakSpeed;

    public LargeBlockBreakingIngredientRender(int breakSpeed) {
        this.breakSpeed = breakSpeed;
    }

    @Override
    protected void renderBlock(@Nonnull MatrixStack matrixStack, BlockState state) {
        super.renderBlock(matrixStack, state);
        int breakProgress = (ClientTickHandler.ticksInGame / this.breakSpeed) % 10;
        RenderHelperBlock.renderBlockBreak(state, matrixStack, Minecraft.getInstance().getRenderTypeBuffers().getBufferSource(), LightTexture.packLight(15 ,15), OverlayTexture.NO_OVERLAY, breakProgress);
        Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish();
        Minecraft.getInstance().getRenderTypeBuffers().getCrumblingBufferSource().finish();
    }
}
