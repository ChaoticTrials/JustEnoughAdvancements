package de.melanx.jea.plugins.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.util.ItemUtil;
import de.melanx.jea.render.LargeBlockIngredientRender;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileTinyPotato;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class TinyPotatoRender extends LargeBlockIngredientRender {

    private final TileTinyPotato tile = new TileTinyPotato();
    private TileEntityRenderer<TileTinyPotato> tileRender = null;
    
    @Override
    protected void renderBlock(@Nonnull MatrixStack matrixStack, BlockState state) {
        if (state.getBlock() == ModBlocks.tinyPotato) {
            List<ItemStack> stacks = ItemUtil.creators();
            int animationTimeTicks = ClientTickHandler.ticksInGame % 40;
            int idx = (ClientTickHandler.ticksInGame / 40) % stacks.size();
            this.tile.setWorldAndPos(Objects.requireNonNull(Minecraft.getInstance().world), BlockPos.ZERO);
            this.tile.jumpTicks = animationTimeTicks >= 20 ? animationTimeTicks - 20 : 0;
            if (animationTimeTicks >= 20) {
                this.tile.setInventorySlotContents(1, stacks.get(idx));
            } else {
                this.tile.setInventorySlotContents(1, ItemStack.EMPTY);
            }
            this.tile.cachedBlockState = state.with(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
            if (this.tileRender == null) {
                this.tileRender = TileEntityRendererDispatcher.instance.getRenderer(this.tile);
            }
            if (this.tileRender != null) {
                this.tileRender.render(this.tile, Minecraft.getInstance().getRenderPartialTicks(), matrixStack, Minecraft.getInstance().getRenderTypeBuffers().getBufferSource(), LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
                Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish();
            }
        } else {
            super.renderBlock(matrixStack, state);
        }
    }
}
