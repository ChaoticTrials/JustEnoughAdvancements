package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.plugins.botania.AlfPortalRenderer;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
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
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.EndPortalTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.Objects;

public class ChangeDimensionInfo implements ICriterionInfo<ChangeDimensionTrigger.Instance> {

    private static final ResourceLocation MYTHICBOTANY_ALFHEIM = new ResourceLocation("mythicbotany", "alfheim");
    
    private final EndPortalTileEntity tile = new EndPortalTileEntity();
    private TileEntityRenderer<EndPortalTileEntity> tileRender = null;
    
    @Override
    public Class<ChangeDimensionTrigger.Instance> criterionClass() {
        return ChangeDimensionTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ChangeDimensionTrigger.Instance instance, IIngredients ii) {
        if (World.THE_NETHER.equals(instance.to)) {
            ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                    ImmutableList.of(new ItemStack(Items.OBSIDIAN, 10))
            ));
        } else if (World.THE_END.equals(instance.to)) {
            ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                    ImmutableList.of(new ItemStack(Items.END_PORTAL_FRAME, 12)),
                    ImmutableList.of(new ItemStack(Items.ENDER_EYE, 12))
            ));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ChangeDimensionTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ChangeDimensionTrigger.Instance instance, double mouseX, double mouseY) {
        int y = 6;
        if (instance.from != null) {
            ITextComponent text = new TranslationTextComponent("jea.item.tooltip.change_dimension.from", IngredientUtil.dim(instance.from.getLocation()));
            mc.fontRenderer.func_243248_b(matrixStack, text, 5, SPACE_TOP + y, 0x000000);
            y += (3 + mc.fontRenderer.FONT_HEIGHT);
        }
        if (instance.to != null) {
            ITextComponent text = new TranslationTextComponent("jea.item.tooltip.change_dimension.to", IngredientUtil.dim(instance.to.getLocation()));
            mc.fontRenderer.func_243248_b(matrixStack, text, 5, SPACE_TOP + y, 0x000000);
        }
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 1.9f);
        SteveRender.defaultPose(mc);
        SteveRender.limbSwing(0.5f);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteveStatic(mc, matrixStack, buffer);
        matrixStack.pop();
        if (World.THE_END.equals(instance.to)) {
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
            JeaRender.normalize(matrixStack);
            matrixStack.translate(0, -1.5, 0);
            JeaRender.transformForEntityRenderSide(matrixStack, true, 0.8f);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(45));
            this.renderEndPortal(matrixStack, buffer, mc);
            matrixStack.pop();
        } else if (ModList.get().isLoaded("mythicbotany") && instance.to != null
                && MYTHICBOTANY_ALFHEIM.equals(instance.to.getLocation())) {
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
            JeaRender.normalize(matrixStack);
            JeaRender.transformForEntityRenderSide(matrixStack, true, 0.7f);
            AlfPortalRenderer.renderAlfPortal(matrixStack, buffer, mc, true);
            matrixStack.pop();
        } else {
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
            JeaRender.normalize(matrixStack);
            JeaRender.transformForEntityRenderSide(matrixStack, true, 0.8f);
            renderNetherPortal(matrixStack, buffer, mc);
            matrixStack.pop();
        }
    }
    
    @SuppressWarnings("deprecation")
    public static void renderNetherPortal(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc) {
        BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
        BlockState obsidian = Blocks.OBSIDIAN.getDefaultState();
        BlockState portal = Blocks.NETHER_PORTAL.getDefaultState().with(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X);
        int light = LightTexture.packLight(15, 15);
        int overlay = OverlayTexture.NO_OVERLAY;
        matrixStack.push();
        matrixStack.translate(-2, 0, -0.5);
        brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
        matrixStack.pop();
        for (int i = 1; i <= 3; i++) {
            matrixStack.push();
            matrixStack.translate(-2, i, -0.5);
            brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
            matrixStack.translate(1, 0, 0);
            brd.renderBlock(portal, matrixStack, buffer, light, overlay);
            matrixStack.translate(1, 0, 0);
            brd.renderBlock(portal, matrixStack, buffer, light, overlay);
            matrixStack.translate(1, 0, 0);
            brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
            matrixStack.pop();
        }
        matrixStack.push();
        matrixStack.translate(-2, 4, -0.5);
        brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(obsidian, matrixStack, buffer, light, overlay);
        matrixStack.pop();
    }

    @SuppressWarnings("deprecation")
    private void renderEndPortal(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc) {
        BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
        BlockState frame_n = Blocks.END_PORTAL_FRAME.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).with(BlockStateProperties.EYE, true);
        BlockState frame_s = Blocks.END_PORTAL_FRAME.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).with(BlockStateProperties.EYE, true);
        BlockState frame_w = Blocks.END_PORTAL_FRAME.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).with(BlockStateProperties.EYE, true);
        BlockState frame_e = Blocks.END_PORTAL_FRAME.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).with(BlockStateProperties.EYE, true);
        BlockState portal = Blocks.END_PORTAL.getDefaultState();
        int light = LightTexture.packLight(15, 15);
        int overlay = OverlayTexture.NO_OVERLAY;
        matrixStack.push();
        matrixStack.translate(-1.5, 0, -2.5);
        brd.renderBlock(frame_s, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(frame_s, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(frame_s, matrixStack, buffer, light, overlay);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(-1.5, 0, 1.5);
        brd.renderBlock(frame_n, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(frame_n, matrixStack, buffer, light, overlay);
        matrixStack.translate(1, 0, 0);
        brd.renderBlock(frame_n, matrixStack, buffer, light, overlay);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(-2.5, 0, -1.5);
        brd.renderBlock(frame_e, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, 0, 1);
        brd.renderBlock(frame_e, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, 0, 1);
        brd.renderBlock(frame_e, matrixStack, buffer, light, overlay);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(1.5, 0, -1.5);
        brd.renderBlock(frame_w, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, 0, 1);
        brd.renderBlock(frame_w, matrixStack, buffer, light, overlay);
        matrixStack.translate(0, 0, 1);
        brd.renderBlock(frame_w, matrixStack, buffer, light, overlay);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(-1.5, 0, -1.5);
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                this.tile.setWorldAndPos(Objects.requireNonNull(mc.world), BlockPos.ZERO);
                this.tile.cachedBlockState = portal;
                if (this.tileRender == null) {
                    this.tileRender = TileEntityRendererDispatcher.instance.getRenderer(this.tile);
                }
                if (this.tileRender != null) {
                    this.tileRender.render(this.tile, mc.getRenderPartialTicks(), matrixStack, buffer, light, overlay);
                }
                matrixStack.translate(0, 0, 1);
            }
            matrixStack.translate(1, 0, -3);
        }
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ChangeDimensionTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
