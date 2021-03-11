package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.advancements.criterion.EnterBlockTrigger;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Objects;

public class EnterBlockInfo implements ICriterionInfo<EnterBlockTrigger.Instance> {

    private final EndGatewayTileEntity tile = new EndGatewayTileEntity();
    private TileEntityRenderer<EndGatewayTileEntity> tileRender = null;

    @Override
    public Class<EnterBlockTrigger.Instance> criterionClass() {
        return EnterBlockTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, EnterBlockTrigger.Instance instance, IIngredients ii) {
        ItemStack stack = new ItemStack(instance.block);
        if (!stack.isEmpty()) {
            ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                    ImmutableList.of(stack)
            ));
        } else if (instance.block == Blocks.END_GATEWAY) {
            ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                    ImmutableList.of(new ItemStack(Items.ENDER_PEARL))
            ));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, EnterBlockTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, EnterBlockTrigger.Instance instance, double mouseX, double mouseY) {
        BlockState state = IngredientUtil.getState(instance.block, instance.properties);
        //noinspection deprecation
        if (state.isAir()) {
            ITextComponent text = new TranslationTextComponent("jea.item.tooltip.in_block.any");
            int width = mc.fontRenderer.getStringPropertyWidth(text);
            mc.fontRenderer.func_243248_b(matrixStack, text, (RECIPE_WIDTH / 2f) - (width / 2f), SPACE_TOP + (RECIPE_HEIGHT / 2f) - ((mc.fontRenderer.FONT_HEIGHT + 1) / 2f), 0x000000);
        } else if (state.getBlock() == Blocks.END_GATEWAY) {
            float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 26;
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
                matrixStack.push();
                matrixStack.translate(MathHelper.lerp(fly, 30, RECIPE_WIDTH - 45), SPACE_TOP + 50 - (15 * Math.sin(fly * Math.PI)), 10);
                RenderHelperItem.renderItemGui(matrixStack, buffer, new ItemStack(Items.ENDER_PEARL), 0, 0, 8, false);
                matrixStack.pop();
            }
            matrixStack.push();
            matrixStack.translate(30, SPACE_TOP + 90, 0);
            JeaRender.normalize(matrixStack);
            JeaRender.transformForEntityRenderSide(matrixStack, false, 1.9f);
            SteveRender.defaultPose(mc);
            SteveRender.limbSwing(0.5f);
            SteveRender.swing(swing, Hand.MAIN_HAND);
            SteveRender.setEquipmentHand(mc, held);
            SteveRender.renderSteveStatic(mc, matrixStack, buffer);
            matrixStack.pop();
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
            JeaRender.normalize(matrixStack);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-10));
            JeaRender.transformForEntityRenderSide(matrixStack, true, 0.8f);
            this.renderEndGateway(matrixStack, buffer, mc);
            matrixStack.pop();
        } else {
            ITextComponent text = new TranslationTextComponent("jea.item.tooltip.in_block.title");
            int width = mc.fontRenderer.getStringPropertyWidth(text);
            mc.fontRenderer.func_243248_b(matrixStack, text, (RECIPE_WIDTH / 2f) - (width / 2f), SPACE_TOP + 10, 0x000000);
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH / 2d, SPACE_TOP + 90, 0);
            JeaRender.normalize(matrixStack);
            JeaRender.transformForEntityRenderFront(matrixStack, false, 2.2f);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(4));
            SteveRender.defaultPose(mc);
            SteveRender.clearEquipment(mc);
            SteveRender.renderSteve(mc, matrixStack, buffer);
            matrixStack.translate(-0.5, 0, -0.5);
            //noinspection deprecation
            mc.getBlockRendererDispatcher().renderBlock((instance.block == null ? Blocks.COBWEB : instance.block).getDefaultState(), matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
            matrixStack.pop();
        }
    }

    @SuppressWarnings("deprecation")
    private void renderEndGateway(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc) {
        BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
        BlockState bedrock = Blocks.BEDROCK.getDefaultState();
        BlockState gateway = Blocks.END_GATEWAY.getDefaultState();
        int light = LightTexture.packLight(15, 15);
        int overlay = OverlayTexture.NO_OVERLAY;
        matrixStack.push();
        matrixStack.translate(-0.5, 0, -0.5);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, 1, 0);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(-2, 0, 0);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 1);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, 0, -2);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(-0.5, 4, -0.5);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, -1, 0);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(-2, 0, 0);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 1);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, 0, -2);
        brd.renderBlock(bedrock, matrixStack, buffer, light, overlay);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(-0.5, 2, -0.5);
        this.tile.setWorldAndPos(Objects.requireNonNull(mc.world), JeaRender.BELOW_WORLD);
        this.tile.cachedBlockState = gateway;
        if (this.tileRender == null) {
            this.tileRender = TileEntityRendererDispatcher.instance.getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            // partial ticks must be 0 or there's a glitched beam.
            this.tileRender.render(this.tile, 0, matrixStack, buffer, light, overlay);
        }
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, EnterBlockTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
