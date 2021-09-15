package de.melanx.jea.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
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
    protected void renderBlock(@Nonnull PoseStack poseStack, BlockState state) {
        float animationTime = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getFrameTime()) % this.totalTicks;
        boolean hasSurface = false;
        if (state.getBlock() instanceof BushBlock bush) {
            List<Block> surfaces;
            if (surface.containsKey(state.getBlock())) {
                surfaces = surface.get(state.getBlock()).orElse(null);
            } else {
                surfaces = ForgeRegistries.BLOCKS.getValues().stream()
                        .filter(b -> validGround(bush, b))
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
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(JeaRender.cycle(surfaces).defaultBlockState(), poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
                Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            }
        }
        if (animationTime > this.invisibleTicks) {
            if (state.getBlock() instanceof BushBlock && hasSurface) {
                poseStack.pushPose();
                poseStack.translate(0, 1, 0);
                BlockState renderState = state;
                if (state.hasProperty(BlockStateProperties.AGE_7)) {
                    renderState = state.setValue(BlockStateProperties.AGE_7, 3);
                }
                super.renderBlock(poseStack, renderState);
                poseStack.popPose();
            } else {
                super.renderBlock(poseStack, state);
            }
        }
    }
    
    private static boolean validGround(BushBlock crop, Block ground) {
        try {
            if (validGroundMethod == null) {
                validGroundMethod = ObfuscationReflectionHelper.findMethod(BushBlock.class, "m_6266_", BlockState.class, BlockGetter.class, BlockPos.class);
                validGroundMethod.setAccessible(true);
            }
            return (boolean) validGroundMethod.invoke(crop, ground.defaultBlockState(), Minecraft.getInstance().level, BlockPos.ZERO);
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }
}
