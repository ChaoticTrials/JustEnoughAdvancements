//package de.melanx.jea.plugins.botania;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.util.ItemUtil;
//import de.melanx.jea.render.LargeBlockIngredientRender;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.LightTexture;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
//import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import net.minecraft.core.Direction;
//import net.minecraft.core.BlockPos;
//import vazkii.botania.common.block.ModBlocks;
//import vazkii.botania.common.block.tile.TileTinyPotato;
//
//import javax.annotation.Nonnull;
//import java.util.List;
//import java.util.Objects;
//
//public class TinyPotatoRender extends LargeBlockIngredientRender {
//
//    private final TileTinyPotato tile = new TileTinyPotato();
//    private BlockEntityRenderer<TileTinyPotato> tileRender = null;
//    
//    @Override
//    protected void renderBlock(@Nonnull PoseStack poseStack, BlockState state) {
//        if (state.getBlock() == ModBlocks.tinyPotato) {
//            List<ItemStack> stacks = ItemUtil.creators();
//            int animationTimeTicks = ClientTickHandler.ticksInGame % 40;
//            int idx = (ClientTickHandler.ticksInGame / 40) % stacks.size();
//            this.tile.setLevelAndPosition(Objects.requireNonNull(Minecraft.getInstance().level), BlockPos.ZERO);
//            this.tile.jumpTicks = animationTimeTicks >= 20 ? animationTimeTicks - 20 : 0;
//            if (animationTimeTicks >= 20) {
//                this.tile.setItem(1, stacks.get(idx));
//            } else {
//                this.tile.setItem(1, ItemStack.EMPTY);
//            }
//            this.tile.blockState = state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
//            if (this.tileRender == null) {
//                this.tileRender = BlockEntityRenderDispatcher.instance.getRenderer(this.tile);
//            }
//            if (this.tileRender != null) {
//                this.tileRender.render(this.tile, Minecraft.getInstance().getFrameTime(), poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
//                Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
//            }
//        } else {
//            super.renderBlock(poseStack, state);
//        }
//    }
//}
