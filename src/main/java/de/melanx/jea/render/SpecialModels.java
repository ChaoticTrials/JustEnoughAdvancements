package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.melanx.jea.JustEnoughAdvancements;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.HashMap;

public class SpecialModels {
    
    public static final ResourceLocation UNKNOWN_ENTITY = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "special/unknown_entity");
    public static final ResourceLocation FLUID_POOL = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "special/fluid_pool");
    
    private static final HashMap<ResourceLocation, IBakedModel> models = new HashMap<>();
    
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(UNKNOWN_ENTITY);
        ModelLoader.addSpecialModel(FLUID_POOL);
    }

    public static void bakeModels(ModelBakeEvent event) {
        models.put(UNKNOWN_ENTITY, event.getModelRegistry().get(UNKNOWN_ENTITY));
        models.put(FLUID_POOL, event.getModelRegistry().get(FLUID_POOL));
    }
    
    public static IBakedModel getModel(ResourceLocation id) {
        return models.get(id);
    }

    public static void renderFluidPool(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, FluidStack fluidStack, @Nullable EntityType<?> entity) {
        int light = LightTexture.packLight(15, 15);
        int overlay = OverlayTexture.NO_OVERLAY;
        matrixStack.push();
        matrixStack.translate(-0.5, 0, -0.5);
        //noinspection deprecation
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
                .renderModelBrightnessColor(matrixStack.getLast(),
                        buffer.getBuffer(RenderType.getSolid()), null,
                        getModel(SpecialModels.FLUID_POOL),
                        1, 1, 1, light, overlay);
        if (!fluidStack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(3/16d, 0.21, 3/16d);
            matrixStack.scale(10/16f, 10/16f, 10/16f);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
            Fluid fluid = fluidStack.getFluid();
            int color = fluid.getAttributes().getColor(fluidStack);
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getAttributes().getStillTexture(fluidStack));
            mc.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
            IVertexBuilder vertex = buffer.getBuffer(Atlases.getTranslucentCullBlockType());
            net.minecraft.client.renderer.RenderHelper.setupGuiFlatDiffuseLighting();
            RenderHelper.renderIconColored(matrixStack, vertex, 0, 0, sprite, 1, 1, 1, color, light, overlay);
            matrixStack.pop();
        }
        if (entity != null) {
            matrixStack.push();
            matrixStack.translate(0.5, 0.16, 0.5);
            matrixStack.scale(0.3f, 0.3f, 0.3f);
            RenderEntityCache.renderPlainEntity(mc, entity, matrixStack, buffer);
            matrixStack.pop();
        }
        matrixStack.pop();
    }
}
