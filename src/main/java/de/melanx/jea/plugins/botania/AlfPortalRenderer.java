package de.melanx.jea.plugins.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.ModBlocks;

public class AlfPortalRenderer {

    @SuppressWarnings("deprecation")
    public static void renderAlfPortal(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, boolean active) {
        BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
        BlockState core = ModBlocks.alfPortal.getDefaultState().with(BotaniaStateProps.ALFPORTAL_STATE, active ? AlfPortalState.ON_X : AlfPortalState.OFF);
        BlockState livingwood = ModBlocks.livingwood.getDefaultState();
        BlockState glimmering = ModBlocks.livingwoodGlimmering.getDefaultState();
        BlockState pool = ModBlocks.creativePool.getDefaultState();
        BlockState pylon = ModBlocks.naturaPylon.getDefaultState();
        int light = LightTexture.packLight(15, 15);
        int overlay = OverlayTexture.NO_OVERLAY;
        matrixStack.push();
        matrixStack.translate(-1.5, 0, -0.5);
        brd.renderBlock(livingwood, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(core, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(livingwood, matrixStack, buffer, light, overlay);
        matrixStack.pop();
        for (int i = 0; i < 3; i++) {
            matrixStack.push();
            matrixStack.translate(-2.5, 1 + i, -0.5);
            brd.renderBlock(i == 1 ? glimmering : livingwood, matrixStack, buffer, light, overlay);
            matrixStack.translate(4, 0, 0);
            brd.renderBlock(i == 1 ? glimmering : livingwood, matrixStack, buffer, light, overlay);
            matrixStack.pop();
        }
        matrixStack.push();
        matrixStack.translate(-1.5, 4, -0.5);
        brd.renderBlock(livingwood, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(glimmering, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(livingwood, matrixStack, buffer, light, overlay);
        matrixStack.pop();
        
        if (active) {
            mc.getTextureManager().bindTexture(MiscellaneousIcons.INSTANCE.alfPortalTex.getAtlasLocation());
            TextureAtlasSprite sprite = MiscellaneousIcons.INSTANCE.alfPortalTex.getSprite();
            float alpha = (float) (Math.min(1, ((Math.sin((ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) / 8d) + 4) / 7d) + 0.6) * 0.8);

            matrixStack.push();
            matrixStack.translate(-1.5, 1, 0.3);
            renderPortal(matrixStack, buffer, sprite, alpha);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(1.5, 1, -0.3);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
            renderPortal(matrixStack, buffer, sprite, alpha);
            matrixStack.pop();
        }
        
        matrixStack.push();
        matrixStack.translate(-2, 0, 3);
        brd.renderBlock(pool, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, 0.75, 0);
        brd.renderBlock(pylon, matrixStack, buffer, light, overlay);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(2, 0, 3);
        brd.renderBlock(pool, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, 0.75, 0);
        brd.renderBlock(pylon, matrixStack, buffer, light, overlay);
        matrixStack.pop();
    }

    private static void renderPortal(MatrixStack matrixStack, IRenderTypeBuffer buffer, TextureAtlasSprite sprite, float alpha) {
        IVertexBuilder vertex = buffer.getBuffer(Atlases.getItemEntityTranslucentCullType());
		Matrix4f model = matrixStack.getLast().getMatrix();
		Matrix3f normal = matrixStack.getLast().getNormal();
		vertex.pos(model, 0, 0, 0).color(1, 1, 1, alpha).tex(sprite.getMinU(), sprite.getMinV()).overlay(OverlayTexture.NO_OVERLAY).lightmap(0xF000F0).normal(normal, 1, 0, 0).endVertex();
		vertex.pos(model, 3, 0, 0).color(1, 1, 1, alpha).tex(sprite.getMaxU(), sprite.getMinV()).overlay(OverlayTexture.NO_OVERLAY).lightmap(0xF000F0).normal(normal, 1, 0, 0).endVertex();
		vertex.pos(model, 3, 3, 0).color(1, 1, 1, alpha).tex(sprite.getMaxU(), sprite.getMaxV()).overlay(OverlayTexture.NO_OVERLAY).lightmap(0xF000F0).normal(normal, 1, 0, 0).endVertex();
		vertex.pos(model, 0, 3, 0).color(1, 1, 1, alpha).tex(sprite.getMinU(), sprite.getMaxV()).overlay(OverlayTexture.NO_OVERLAY).lightmap(0xF000F0).normal(normal, 1, 0, 0).endVertex();
    }
}
