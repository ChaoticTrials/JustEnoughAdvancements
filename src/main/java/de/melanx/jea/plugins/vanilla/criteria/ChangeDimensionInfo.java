package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;
import java.util.Objects;

public class ChangeDimensionInfo implements ICriterionInfo<ChangeDimensionTrigger.TriggerInstance> {

    private static final ResourceLocation MYTHICBOTANY_ALFHEIM = new ResourceLocation("mythicbotany", "alfheim");
    
    private final TheEndPortalBlockEntity tile = new TheEndPortalBlockEntity(JeaRender.BELOW_WORLD, Blocks.END_PORTAL.defaultBlockState());
    private BlockEntityRenderer<TheEndPortalBlockEntity> tileRender = null;
    
    @Override
    public Class<ChangeDimensionTrigger.TriggerInstance> criterionClass() {
        return ChangeDimensionTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ChangeDimensionTrigger.TriggerInstance instance, IIngredients ii) {
        if (Level.NETHER.equals(instance.to)) {
            ii.setInputLists(VanillaTypes.ITEM, List.of(
                    List.of(new ItemStack(Items.OBSIDIAN, 10))
            ));
        } else if (Level.END.equals(instance.to)) {
            ii.setInputLists(VanillaTypes.ITEM, List.of(
                    List.of(new ItemStack(Items.END_PORTAL_FRAME, 12)),
                    List.of(new ItemStack(Items.ENDER_EYE, 12))
            ));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ChangeDimensionTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ChangeDimensionTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        int y = 6;
        if (instance.from != null) {
            Component text = new TranslatableComponent("jea.item.tooltip.change_dimension.from", IngredientUtil.dim(instance.from.location()));
            mc.font.draw(poseStack, text, 5, SPACE_TOP + y, 0x000000);
            y += (3 + mc.font.lineHeight);
        }
        if (instance.to != null) {
            Component text = new TranslatableComponent("jea.item.tooltip.change_dimension.to", IngredientUtil.dim(instance.to.location()));
            mc.font.draw(poseStack, text, 5, SPACE_TOP + y, 0x000000);
        }
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 1.9f);
        SteveRender.defaultPose(mc);
        SteveRender.limbSwing(0.5f);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteveStatic(mc, poseStack, buffer);
        poseStack.popPose();
        if (Level.END.equals(instance.to)) {
            poseStack.pushPose();
            poseStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
            JeaRender.normalize(poseStack);
            poseStack.translate(0, -1.5, 0);
            JeaRender.transformForEntityRenderSide(poseStack, true, 0.8f);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(45));
            this.renderEndPortal(poseStack, buffer, mc);
            poseStack.popPose();
//        } else if (ModList.get().isLoaded("mythicbotany") && instance.to != null
//                && MYTHICBOTANY_ALFHEIM.equals(instance.to.location())) {
//            poseStack.pushPose();
//            poseStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
//            JeaRender.normalize(poseStack);
//            JeaRender.transformForEntityRenderSide(poseStack, true, 0.7f);
//            AlfPortalRenderer.renderAlfPortal(poseStack, buffer, mc, true);
//            poseStack.popPose();
        } else {
            poseStack.pushPose();
            poseStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
            JeaRender.normalize(poseStack);
            JeaRender.transformForEntityRenderSide(poseStack, true, 0.8f);
            renderNetherPortal(poseStack, buffer, mc);
            poseStack.popPose();
        }
    }
    
    @SuppressWarnings("deprecation")
    public static void renderNetherPortal(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc) {
        BlockRenderDispatcher brd = mc.getBlockRenderer();
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        BlockState portal = Blocks.NETHER_PORTAL.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X);
        int light = LightTexture.pack(15, 15);
        int overlay = OverlayTexture.NO_OVERLAY;
        poseStack.pushPose();
        poseStack.translate(-2, 0, -0.5);
        brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
        poseStack.popPose();
        for (int i = 1; i <= 3; i++) {
            poseStack.pushPose();
            poseStack.translate(-2, i, -0.5);
            brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
            poseStack.translate(1, 0, 0);
            brd.renderSingleBlock(portal, poseStack, buffer, light, overlay);
            poseStack.translate(1, 0, 0);
            brd.renderSingleBlock(portal, poseStack, buffer, light, overlay);
            poseStack.translate(1, 0, 0);
            brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
            poseStack.popPose();
        }
        poseStack.pushPose();
        poseStack.translate(-2, 4, -0.5);
        brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(obsidian, poseStack, buffer, light, overlay);
        poseStack.popPose();
    }

    @SuppressWarnings("deprecation")
    private void renderEndPortal(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc) {
        BlockRenderDispatcher brd = mc.getBlockRenderer();
        BlockState frame_n = Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.EYE, true);
        BlockState frame_s = Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(BlockStateProperties.EYE, true);
        BlockState frame_w = Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).setValue(BlockStateProperties.EYE, true);
        BlockState frame_e = Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).setValue(BlockStateProperties.EYE, true);
        BlockState portal = Blocks.END_PORTAL.defaultBlockState();
        int light = LightTexture.pack(15, 15);
        int overlay = OverlayTexture.NO_OVERLAY;
        poseStack.pushPose();
        poseStack.translate(-1.5, 0, -2.5);
        brd.renderSingleBlock(frame_s, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(frame_s, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(frame_s, poseStack, buffer, light, overlay);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(-1.5, 0, 1.5);
        brd.renderSingleBlock(frame_n, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(frame_n, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(frame_n, poseStack, buffer, light, overlay);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(-2.5, 0, -1.5);
        brd.renderSingleBlock(frame_e, poseStack, buffer, light, overlay);
        poseStack.translate(0, 0, 1);
        brd.renderSingleBlock(frame_e, poseStack, buffer, light, overlay);
        poseStack.translate(0, 0, 1);
        brd.renderSingleBlock(frame_e, poseStack, buffer, light, overlay);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(1.5, 0, -1.5);
        brd.renderSingleBlock(frame_w, poseStack, buffer, light, overlay);
        poseStack.translate(0, 0, 1);
        brd.renderSingleBlock(frame_w, poseStack, buffer, light, overlay);
        poseStack.translate(0, 0, 1);
        brd.renderSingleBlock(frame_w, poseStack, buffer, light, overlay);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(-1.5, 0, -1.5);
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                this.tile.setLevel(Objects.requireNonNull(mc.level));
                this.tile.blockState = portal;
                if (this.tileRender == null) {
                    this.tileRender = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(this.tile);
                }
                if (this.tileRender != null) {
                    this.tileRender.render(this.tile, mc.getFrameTime(), poseStack, buffer, light, overlay);
                }
                poseStack.translate(0, 0, 1);
            }
            poseStack.translate(1, 0, -3);
        }
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ChangeDimensionTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
