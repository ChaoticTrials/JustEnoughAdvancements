package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LargeBlockAppearingIngredientRender extends LargeBlockIngredientRender {
    
    private static Method validGroundMethod = null;
    private static final Map<Block, Optional<List<Block>>> surface = new HashMap<>();
    
    private final int invisibleTicks;
    private final int totalTicks;

    public LargeBlockAppearingIngredientRender(int visibleTicks, int invisibleTicks) {
        this.invisibleTicks = invisibleTicks;
        this.totalTicks = visibleTicks + invisibleTicks;
    }

    @Override
    protected void renderBlock(@Nonnull MatrixStack matrixStack, BlockState state) {
        float animationTime = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()) % this.totalTicks;
        boolean hasSurface = false;
        if (state.getBlock() instanceof BushBlock) {
            List<Block> surfaces;
            if (surface.containsKey(state.getBlock())) {
                surfaces = surface.get(state.getBlock()).orElse(null);
            } else {
                surfaces = ForgeRegistries.BLOCKS.getValues().stream()
                        .filter(b -> validGround((BushBlock) state.getBlock(), b))
                        .collect(Collectors.toList());
                if (surfaces.isEmpty()) {
                    surface.put(state.getBlock(), Optional.empty());
                    surfaces = null;
                } else {
                    surface.put(state.getBlock(), Optional.of(surfaces));
                }
            }
            if (surfaces != null) {
                hasSurface = true;
                //noinspection deprecation
                Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(JeaRender.cycle(surfaces).getDefaultState(), matrixStack, Minecraft.getInstance().getRenderTypeBuffers().getBufferSource(), LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
                Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish();
            }
        }
        if (animationTime > this.invisibleTicks) {
            if (state.getBlock() instanceof BushBlock && hasSurface) {
                matrixStack.push();
                matrixStack.translate(0, 1, 0);
                BlockState renderState = state;
                if (state.hasProperty(BlockStateProperties.AGE_0_7)) {
                    renderState = state.with(BlockStateProperties.AGE_0_7, 3);
                }
                super.renderBlock(matrixStack, renderState);
                matrixStack.pop();
            } else {
                super.renderBlock(matrixStack, state);
            }
        }
    }
    
    private static boolean validGround(BushBlock crop, Block ground) {
        try {
            if (validGroundMethod == null) {
                validGroundMethod = ObfuscationReflectionHelper.findMethod(BushBlock.class, "func_200014_a_", BlockState.class, IBlockReader.class, BlockPos.class);
                validGroundMethod.setAccessible(true);
            }
            return (boolean) validGroundMethod.invoke(crop, ground.getDefaultState(), Minecraft.getInstance().world, BlockPos.ZERO);
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }
}
