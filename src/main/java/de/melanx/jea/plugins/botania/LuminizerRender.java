//package de.melanx.jea.plugins.botania;
//
//import com.google.common.collect.ImmutableMap;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import de.melanx.jea.render.LargeItemIngredientRender;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.LightTexture;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import com.mojang.math.Matrix4f;
//import com.mojang.math.Vector3f;
//import vazkii.botania.client.core.handler.MiscellaneousIcons;
//import vazkii.botania.client.core.helper.RenderHelper;
//import vazkii.botania.common.block.ModBlocks;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import java.util.Map;
//import java.util.function.Supplier;
//
//public class LuminizerRender extends LargeItemIngredientRender {
//
//    private static final Map<Item, Supplier<TextureAtlasSprite>> SPRITES = Map.of(
//            ModBlocks.lightRelayDetector.asItem(), MiscellaneousIcons.INSTANCE.lightRelayDetectorWorldIcon::sprite,
//            ModBlocks.lightRelayFork.asItem(), MiscellaneousIcons.INSTANCE.lightRelayForkWorldIcon::sprite,
//            ModBlocks.lightRelayToggle.asItem(), MiscellaneousIcons.INSTANCE.lightRelayToggleWorldIcon::sprite
//    );
//    
//    public LuminizerRender() {
//        super(24, 0);
//    }
//
//    @Override
//    public void render(@Nonnull PoseStack poseStack, int x, int y, @Nullable ItemStack stack) {
//        if (stack != null && !stack.isEmpty()) {
//            poseStack.pushPose();
//            int half = this.size / 2;
//            poseStack.translate(x + half, y + half, 0);
//            poseStack.mulPose(Vector3f.ZP.rotationDegrees(ClientTickHandler.ticksInGame + Minecraft.getInstance().getFrameTime()));
//            poseStack.translate(-half, -half, 0);
//            poseStack.scale(this.size / 16f, this.size / 16f, this.size / 16f);
//            TextureAtlasSprite sprite;
//            if (SPRITES.containsKey(stack.getItem())) {
//                sprite = SPRITES.get(stack.getItem()).get();
//            } else {
//                sprite = MiscellaneousIcons.INSTANCE.lightRelayWorldIcon.sprite();
//            }
//            Matrix4f matrix = poseStack.last().pose();
////            Minecraft.getInstance().getTextureManager().bindTexture(MiscellaneousIcons.INSTANCE.lightRelayWorldIcon.getAtlasLocation());
////            AbstractGui.blit(poseStack, 0, 0, 0, 16, 16, sprite);
//            float alpha = (float) (((Math.sin(ClientTickHandler.ticksInGame + Minecraft.getInstance().getFrameTime()) + 1) * 0.4) + 0.2);
//            VertexConsumer vertex = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderHelper.LIGHT_RELAY);
//            int light = LightTexture.pack(15, 15);
//            vertex.vertex(matrix, 0, 16, 0).color(0.9f, 0.9f, 0.9f, alpha).uv(sprite.getU0(), sprite.getV1()).uv2(light).endVertex();
//            vertex.vertex(matrix, 16, 16, 0).color(1, 1, 1, alpha).uv(sprite.getU1(), sprite.getV1()).uv2(light).endVertex();
//            vertex.vertex(matrix, 16, 0, 0).color(0.9f, 0.9f, 0.9f, alpha).uv(sprite.getU1(), sprite.getV0()).uv2(light).endVertex();
//            vertex.vertex(matrix, 0, 0, 0).color(1, 1, 1, alpha).uv(sprite.getU0(), sprite.getV0()).uv2(light).endVertex();
//            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
//            poseStack.popPose();
//        }
//    }
//}
