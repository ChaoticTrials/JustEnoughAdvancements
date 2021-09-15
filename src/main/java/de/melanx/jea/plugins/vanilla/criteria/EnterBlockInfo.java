package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import io.github.noeppi_noeppi.libx.render.RenderHelperItem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

public class EnterBlockInfo implements ICriterionInfo<EnterBlockTrigger.TriggerInstance> {

    private final TheEndGatewayBlockEntity tile = new TheEndGatewayBlockEntity(JeaRender.BELOW_WORLD, Blocks.END_GATEWAY.defaultBlockState());
    private BlockEntityRenderer<TheEndGatewayBlockEntity> tileRender = null;

    @Override
    public Class<EnterBlockTrigger.TriggerInstance> criterionClass() {
        return EnterBlockTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, EnterBlockTrigger.TriggerInstance instance, IIngredients ii) {
        ItemStack stack = new ItemStack(instance.block);
        if (!stack.isEmpty()) {
            ii.setInputLists(VanillaTypes.ITEM, List.of(
                    List.of(stack)
            ));
        } else if (instance.block == Blocks.END_GATEWAY) {
            ii.setInputLists(VanillaTypes.ITEM, List.of(
                    List.of(new ItemStack(Items.ENDER_PEARL))
            ));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, EnterBlockTrigger.TriggerInstance instance, IIngredients ii) {

    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, EnterBlockTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        BlockState state = IngredientUtil.getState(instance.block, instance.state);
        if (state.isAir()) {
            Component text = new TranslatableComponent("jea.item.tooltip.in_block.any");
            int width = mc.font.width(text);
            mc.font.draw(poseStack, text, (RECIPE_WIDTH / 2f) - (width / 2f), SPACE_TOP + (RECIPE_HEIGHT / 2f) - ((mc.font.lineHeight + 1) / 2f), 0x000000);
        } else if (state.getBlock() == Blocks.END_GATEWAY) {
            float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 26;
            float swing;
            ItemStack held;
            float fly;
            if (animationTime < 10) {
                swing = 0f;
                held = animationTime > 5 ? new ItemStack(Items.ENDER_PEARL) : ItemStack.EMPTY;
                fly = 0;
            } else {
                if (animationTime < 16) {
                    swing = (animationTime - 10) / 6f;
                    held = swing < 1/3f ? new ItemStack(Items.ENDER_PEARL) : ItemStack.EMPTY;
                } else {
                    swing = 0;
                    held = ItemStack.EMPTY;
                }
                if (animationTime < 12) {
                    fly = 0;
                } else {
                    fly = (animationTime - 12) / 14f;
                }
            }
            if (fly > 0) {
                poseStack.pushPose();
                poseStack.translate(Mth.lerp(fly, 30, RECIPE_WIDTH - 45), SPACE_TOP + 50 - (15 * Math.sin(fly * Math.PI)), 10);
                RenderHelperItem.renderItemGui(poseStack, buffer, new ItemStack(Items.ENDER_PEARL), 0, 0, 8, false);
                poseStack.popPose();
            }
            poseStack.pushPose();
            poseStack.translate(30, SPACE_TOP + 90, 0);
            JeaRender.normalize(poseStack);
            JeaRender.transformForEntityRenderSide(poseStack, false, 1.9f);
            SteveRender.defaultPose(mc);
            SteveRender.limbSwing(0.5f);
            SteveRender.swing(swing, InteractionHand.MAIN_HAND);
            SteveRender.setEquipmentHand(mc, held);
            SteveRender.renderSteveStatic(mc, poseStack, buffer);
            poseStack.popPose();
            poseStack.pushPose();
            poseStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
            JeaRender.normalize(poseStack);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(-10));
            JeaRender.transformForEntityRenderSide(poseStack, true, 0.8f);
            this.renderEndGateway(poseStack, buffer, mc);
            poseStack.popPose();
        } else {
            Component text = new TranslatableComponent("jea.item.tooltip.in_block.title");
            int width = mc.font.width(text);
            mc.font.draw(poseStack, text, (RECIPE_WIDTH / 2f) - (width / 2f), SPACE_TOP + 10, 0x000000);
            poseStack.pushPose();
            poseStack.translate(RECIPE_WIDTH / 2d, SPACE_TOP + 90, 0);
            JeaRender.normalize(poseStack);
            JeaRender.transformForEntityRenderFront(poseStack, false, 2.2f);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(4));
            SteveRender.defaultPose(mc);
            SteveRender.clearEquipment(mc);
            SteveRender.renderSteve(mc, poseStack, buffer);
            poseStack.translate(-0.5, 0, -0.5);
            //noinspection deprecation
            mc.getBlockRenderer().renderSingleBlock((instance.block == null ? Blocks.COBWEB : instance.block).defaultBlockState(), poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }

    @SuppressWarnings("deprecation")
    private void renderEndGateway(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc) {
        BlockRenderDispatcher brd = mc.getBlockRenderer();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState gateway = Blocks.END_GATEWAY.defaultBlockState();
        int light = LightTexture.pack(15, 15);
        int overlay = OverlayTexture.NO_OVERLAY;
        poseStack.pushPose();
        poseStack.translate(-0.5, 0, -0.5);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(0, 1, 0);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(-2, 0, 0);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 1);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(0, 0, -2);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(-0.5, 4, -0.5);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(0, -1, 0);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 0);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(-2, 0, 0);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(1, 0, 1);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.translate(0, 0, -2);
        brd.renderSingleBlock(bedrock, poseStack, buffer, light, overlay);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(-0.5, 2, -0.5);
        this.tile.setLevel(Objects.requireNonNull(mc.level));
        this.tile.blockState = gateway;
        if (this.tileRender == null) {
            this.tileRender = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            // partial ticks must be 0 or there's a glitched beam.
            this.tileRender.render(this.tile, 0, poseStack, buffer, light, overlay);
        }
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, EnterBlockTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
