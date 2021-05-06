package de.melanx.jea.plugins.botania;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.melanx.jea.render.LargeItemIngredientRender;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class LuminizerRender extends LargeItemIngredientRender {

    private static final Map<Item, Supplier<TextureAtlasSprite>> SPRITES = ImmutableMap.of(
            ModBlocks.lightRelayDetector.asItem(), MiscellaneousIcons.INSTANCE.lightRelayDetectorWorldIcon::getSprite,
            ModBlocks.lightRelayFork.asItem(), MiscellaneousIcons.INSTANCE.lightRelayForkWorldIcon::getSprite,
            ModBlocks.lightRelayToggle.asItem(), MiscellaneousIcons.INSTANCE.lightRelayToggleWorldIcon::getSprite
    );
    
    public LuminizerRender() {
        super(24, 0);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int x, int y, @Nullable ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            matrixStack.push();
            int half = this.size / 2;
            matrixStack.translate(x + half, y + half, 0);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()));
            matrixStack.translate(-half, -half, 0);
            matrixStack.scale(this.size / 16f, this.size / 16f, this.size / 16f);
            TextureAtlasSprite sprite;
            if (SPRITES.containsKey(stack.getItem())) {
                sprite = SPRITES.get(stack.getItem()).get();
            } else {
                sprite = MiscellaneousIcons.INSTANCE.lightRelayWorldIcon.getSprite();
            }
            Matrix4f matrix = matrixStack.getLast().getMatrix();
//            Minecraft.getInstance().getTextureManager().bindTexture(MiscellaneousIcons.INSTANCE.lightRelayWorldIcon.getAtlasLocation());
//            AbstractGui.blit(matrixStack, 0, 0, 0, 16, 16, sprite);
            float alpha = (float) (((Math.sin(ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()) + 1) * 0.4) + 0.2);
            IVertexBuilder vertex = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().getBuffer(RenderHelper.LIGHT_RELAY);
            int light = LightTexture.packLight(15, 15);
            vertex.pos(matrix, 0, 16, 0).color(0.9f, 0.9f, 0.9f, alpha).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(light).endVertex();
            vertex.pos(matrix, 16, 16, 0).color(1, 1, 1, alpha).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(light).endVertex();
            vertex.pos(matrix, 16, 0, 0).color(0.9f, 0.9f, 0.9f, alpha).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(light).endVertex();
            vertex.pos(matrix, 0, 0, 0).color(1, 1, 1, alpha).tex(sprite.getMinU(), sprite.getMinV()).lightmap(light).endVertex();
            Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish();
            matrixStack.pop();
        }
    }
}
