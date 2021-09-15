//package de.melanx.jea.plugins.botania;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.Sheets;
//import net.minecraft.client.renderer.block.BlockRenderDispatcher;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.LightTexture;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import com.mojang.math.Matrix3f;
//import com.mojang.math.Matrix4f;
//import com.mojang.math.Vector3f;
//import vazkii.botania.api.state.BotaniaStateProps;
//import vazkii.botania.api.state.enums.AlfPortalState;
//import vazkii.botania.client.core.handler.MiscellaneousIcons;
//import vazkii.botania.common.block.ModBlocks;
//
//public class AlfPortalRenderer {
//
//    @SuppressWarnings("deprecation")
//    public static void renderAlfPortal(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, boolean active) {
//        BlockRenderDispatcher brd = mc.getBlockRenderer();
//        BlockState core = ModBlocks.alfPortal.defaultBlockState().setValue(BotaniaStateProps.ALFPORTAL_STATE, active ? AlfPortalState.ON_X : AlfPortalState.OFF);
//        BlockState livingwood = ModBlocks.livingwood.defaultBlockState();
//        BlockState glimmering = ModBlocks.livingwoodGlimmering.defaultBlockState();
//        BlockState pool = ModBlocks.creativePool.defaultBlockState();
//        BlockState pylon = ModBlocks.naturaPylon.defaultBlockState();
//        int light = LightTexture.pack(15, 15);
//        int overlay = OverlayTexture.NO_OVERLAY;
//        poseStack.pushPose();
//        poseStack.translate(-1.5, 0, -0.5);
//        brd.renderSingleBlock(livingwood, poseStack, buffer, light, overlay);
//        poseStack.translate(1, 0, 0);
//        brd.renderSingleBlock(core, poseStack, buffer, light, overlay);
//        poseStack.translate(1, 0, 0);
//        brd.renderSingleBlock(livingwood, poseStack, buffer, light, overlay);
//        poseStack.popPose();
//        for (int i = 0; i < 3; i++) {
//            poseStack.pushPose();
//            poseStack.translate(-2.5, 1 + i, -0.5);
//            brd.renderSingleBlock(i == 1 ? glimmering : livingwood, poseStack, buffer, light, overlay);
//            poseStack.translate(4, 0, 0);
//            brd.renderSingleBlock(i == 1 ? glimmering : livingwood, poseStack, buffer, light, overlay);
//            poseStack.popPose();
//        }
//        poseStack.pushPose();
//        poseStack.translate(-1.5, 4, -0.5);
//        brd.renderSingleBlock(livingwood, poseStack, buffer, light, overlay);
//        poseStack.translate(1, 0, 0);
//        brd.renderSingleBlock(glimmering, poseStack, buffer, light, overlay);
//        poseStack.translate(1, 0, 0);
//        brd.renderSingleBlock(livingwood, poseStack, buffer, light, overlay);
//        poseStack.popPose();
//        
//        if (active) {
//            mc.getTextureManager().bind(MiscellaneousIcons.INSTANCE.alfPortalTex.atlasLocation());
//            TextureAtlasSprite sprite = MiscellaneousIcons.INSTANCE.alfPortalTex.sprite();
//            float alpha = (float) (Math.min(1, ((Math.sin((ClientTickHandler.ticksInGame + mc.getFrameTime()) / 8d) + 4) / 7d) + 0.6) * 0.8);
//
//            poseStack.pushPose();
//            poseStack.translate(-1.5, 1, 0.3);
//            renderPortal(poseStack, buffer, sprite, alpha);
//            poseStack.popPose();
//
//            poseStack.pushPose();
//            poseStack.translate(1.5, 1, -0.3);
//            poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
//            renderPortal(poseStack, buffer, sprite, alpha);
//            poseStack.popPose();
//        }
//        
//        poseStack.pushPose();
//        poseStack.translate(-2, 0, 3);
//        brd.renderSingleBlock(pool, poseStack, buffer, light, overlay);
//        poseStack.translate(0, 0.75, 0);
//        brd.renderSingleBlock(pylon, poseStack, buffer, light, overlay);
//        poseStack.popPose();
//
//        poseStack.pushPose();
//        poseStack.translate(2, 0, 3);
//        brd.renderSingleBlock(pool, poseStack, buffer, light, overlay);
//        poseStack.translate(0, 0.75, 0);
//        brd.renderSingleBlock(pylon, poseStack, buffer, light, overlay);
//        poseStack.popPose();
//    }
//
//    private static void renderPortal(PoseStack poseStack, MultiBufferSource buffer, TextureAtlasSprite sprite, float alpha) {
//        VertexConsumer vertex = buffer.getBuffer(Sheets.translucentItemSheet());
//		Matrix4f model = poseStack.last().pose();
//		Matrix3f normal = poseStack.last().normal();
//		vertex.vertex(model, 0, 0, 0).color(1, 1, 1, alpha).uv(sprite.getU0(), sprite.getV0()).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
//		vertex.vertex(model, 3, 0, 0).color(1, 1, 1, alpha).uv(sprite.getU1(), sprite.getV0()).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
//		vertex.vertex(model, 3, 3, 0).color(1, 1, 1, alpha).uv(sprite.getU1(), sprite.getV1()).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
//		vertex.vertex(model, 0, 3, 0).color(1, 1, 1, alpha).uv(sprite.getU0(), sprite.getV1()).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
//    }
//}
