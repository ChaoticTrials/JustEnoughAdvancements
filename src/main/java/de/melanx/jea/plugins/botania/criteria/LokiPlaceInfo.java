package de.melanx.jea.plugins.botania.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.botania.common.advancements.LokiPlaceTrigger;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class LokiPlaceInfo implements ICriterionInfo<LokiPlaceTrigger.Instance> {

    public static final List<Block> BLOCKS = ImmutableList.of(
            Blocks.STONE, Blocks.OAK_PLANKS, ModBlocks.livingrock, Blocks.DIRT,
            ModBlocks.livingwood, Blocks.POLISHED_ANDESITE, ModBlocks.dreamwood
    );

    @Override
    public Class<LokiPlaceTrigger.Instance> criterionClass() {
        return LokiPlaceTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, LokiPlaceTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.fromItemPredicate(instance.getRing(), true, ModItems.lokiRing)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, LokiPlaceTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 52, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, LokiPlaceTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 52, SPACE_TOP + 72);
        float animationTime = (7 + ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 50;
        float swing = animationTime <= 6 ? animationTime / 6f : 0;
        Block block = JeaRender.cycle(BLOCKS);
        matrixStack.push();
        matrixStack.translate(27, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, new ItemStack(block));
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        if (animationTime > 3 && animationTime <= 33) {
            BlockState state = block.getDefaultState();
            BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
            int light = LightTexture.packLight(15, 15);
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH - 45, SPACE_TOP + 70, 0);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-13));
            JeaRender.normalize(matrixStack);
            JeaRender.transformForEntityRenderSide(matrixStack, false, 1.2f);
            brd.renderBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(-1, 0, 0);
            brd.renderBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(2, 0, 0);
            brd.renderBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(-1, 0, -1);
            brd.renderBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 0, 2);
            brd.renderBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 0, -1);
            WorldRenderer.drawBoundingBox(matrixStack, buffer.getBuffer(RenderType.getLines()), 0, 0, 0, 1, 1 ,1, 1, 1, 1, 1);
            matrixStack.pop();
        }
        if (!instance.getBlocksPlaced().isUnbounded()) {
            ITextComponent text = new TranslationTextComponent("jea.item.tooltip.botania.loki_block_amount", IngredientUtil.text(instance.getBlocksPlaced()));
            mc.fontRenderer.func_243248_b(matrixStack, text, 44, SPACE_TOP + 10, 0x000000);
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, LokiPlaceTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
