package de.melanx.jea.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.noeppi_noeppi.libx.annotation.model.Model;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class SpecialModels {
    
    @Model("special/fluid_pool")
    public static BakedModel FLUID_POOL = null;
    
    public static void renderFluidPool(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, FluidStack fluidStack, @Nullable EntityType<?> entity) {
        int light = LightTexture.pack(15, 15);
        int overlay = OverlayTexture.NO_OVERLAY;
        poseStack.pushPose();
        poseStack.translate(-0.5, 0, -0.5);
        //noinspection deprecation
        Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                .renderModel(poseStack.last(),
                        buffer.getBuffer(RenderType.solid()), null,
                        FLUID_POOL, 1, 1, 1, light, overlay);
        if (!fluidStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(3/16d, 0.21, 3/16d);
            poseStack.scale(10/16f, 10/16f, 10/16f);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
            Fluid fluid = fluidStack.getFluid();
            int color = fluid.getAttributes().getColor(fluidStack);
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluid.getAttributes().getStillTexture(fluidStack));
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            VertexConsumer vertex = buffer.getBuffer(Sheets.translucentCullBlockSheet());
            com.mojang.blaze3d.platform.Lighting.setupForFlatItems();
            RenderHelper.renderIconColored(poseStack, vertex, 0, 0, sprite, 1, 1, 1, color, light, overlay);
            poseStack.popPose();
        }
        if (entity != null) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.16, 0.5);
            poseStack.scale(0.3f, 0.3f, 0.3f);
            RenderEntityCache.renderPlainEntity(mc, entity, poseStack, buffer);
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}
