//package de.melanx.jea.plugins.botania.criteria;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.SteveRender;
//import de.melanx.jea.util.IngredientUtil;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.*;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.InteractionHand;
//import com.mojang.math.Vector3f;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.TranslatableComponent;
//import vazkii.botania.common.advancements.LokiPlaceTrigger;
//import vazkii.botania.common.block.ModBlocks;
//import vazkii.botania.common.item.ModItems;
//
//import java.util.List;
//
//import net.minecraft.client.renderer.block.BlockRenderDispatcher;
//
//public class LokiPlaceInfo implements ICriterionInfo<LokiPlaceTrigger.Instance> {
//
//    public static final List<Block> BLOCKS = List.of(
//            Blocks.STONE, Blocks.OAK_PLANKS, ModBlocks.livingrock, Blocks.DIRT,
//            ModBlocks.livingwood, Blocks.POLISHED_ANDESITE, ModBlocks.dreamwood
//    );
//
//    @Override
//    public Class<LokiPlaceTrigger.Instance> criterionClass() {
//        return LokiPlaceTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, LokiPlaceTrigger.Instance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                IngredientUtil.fromItemPredicate(instance.getRing(), true, ModItems.lokiRing)
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, LokiPlaceTrigger.Instance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, 52, SPACE_TOP + 72);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    @SuppressWarnings("deprecation")
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, LokiPlaceTrigger.Instance instance, double mouseX, double mouseY) {
//        JeaRender.slotAt(poseStack, 52, SPACE_TOP + 72);
//        float animationTime = (7 + ClientTickHandler.ticksInGame + mc.getFrameTime()) % 50;
//        float swing = animationTime <= 6 ? animationTime / 6f : 0;
//        Block block = JeaRender.cycle(BLOCKS);
//        poseStack.pushPose();
//        poseStack.translate(27, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
//        SteveRender.defaultPose(mc);
//        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
//        SteveRender.setEquipmentHand(mc, new ItemStack(block));
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//        if (animationTime > 3 && animationTime <= 33) {
//            BlockState state = block.defaultBlockState();
//            BlockRenderDispatcher brd = mc.getBlockRenderer();
//            int light = LightTexture.pack(15, 15);
//            poseStack.pushPose();
//            poseStack.translate(RECIPE_WIDTH - 45, SPACE_TOP + 70, 0);
//            poseStack.mulPose(Vector3f.XP.rotationDegrees(-13));
//            JeaRender.normalize(poseStack);
//            JeaRender.transformForEntityRenderSide(poseStack, false, 1.2f);
//            brd.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//            poseStack.translate(-1, 0, 0);
//            brd.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//            poseStack.translate(2, 0, 0);
//            brd.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//            poseStack.translate(-1, 0, -1);
//            brd.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//            poseStack.translate(0, 0, 2);
//            brd.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//            poseStack.translate(0, 0, -1);
//            LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.lines()), 0, 0, 0, 1, 1 ,1, 1, 1, 1, 1);
//            poseStack.popPose();
//        }
//        if (!instance.getBlocksPlaced().isAny()) {
//            Component text = new TranslatableComponent("jea.item.tooltip.botania.loki_block_amount", IngredientUtil.text(instance.getBlocksPlaced()));
//            mc.font.draw(poseStack, text, 44, SPACE_TOP + 10, 0x000000);
//        }
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, LokiPlaceTrigger.Instance instance, double mouseX, double mouseY) {
//
//    }
//}
