package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderMisc;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.TooltipUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.PositionTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.BellTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WinRaidInfo implements ICriterionInfo<PositionTrigger.Instance> {

    private final BellTileEntity tile = new BellTileEntity();
    private TileEntityRenderer<BellTileEntity> tileRender = null;
    
    @Override
    public Class<PositionTrigger.Instance> criterionClass() {
        return PositionTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, PositionTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, PositionTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, PositionTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(35, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderFront(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteveStatic(mc, matrixStack, buffer);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 70, SPACE_TOP + 82, 0);
        JeaRender.normalize(matrixStack);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-15));
        JeaRender.transformForEntityRenderFront(matrixStack, false, 2.8f);
        //noinspection deprecation
        mc.getBlockRendererDispatcher().renderBlock(Blocks.BELL.getDefaultState(), matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        this.tile.setWorldAndPos(Objects.requireNonNull(mc.world), BlockPos.ZERO);
        this.tile.cachedBlockState = Blocks.BELL.getDefaultState();
        this.tile.ringDirection = Direction.SOUTH;
        this.tile.ringingTicks = Math.max(0, (ClientTickHandler.ticksInGame % 60) - 10);
        this.tile.isRinging = this.tile.ringingTicks > 0;
        if (this.tileRender == null) {
            this.tileRender = TileEntityRendererDispatcher.instance.getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            this.tileRender.render(this.tile, mc.getRenderPartialTicks(), matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        }
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 58, SPACE_TOP + 5, 0);
        matrixStack.scale(1.5f, 1.5f, 1.5f);
        RenderMisc.renderMobEffect(matrixStack, mc, Effects.HERO_OF_THE_VILLAGE);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, PositionTrigger.Instance instance, double mouseX, double mouseY) {
        if (mouseY > SPACE_TOP + 6 && mouseY < SPACE_TOP + 90 && mouseX > 17 && mouseX < 53) {
            ArrayList<IFormattableTextComponent> list = new ArrayList<>();
            TooltipUtil.addLocationValues(list, instance.location);
            tooltip.addAll(list);
        }
        RenderMisc.addMobEffectTooltip(tooltip, new EffectInstance(Effects.HERO_OF_THE_VILLAGE, 0, 0), Optional.empty(), RECIPE_WIDTH - 58, SPACE_TOP + 5, (int) (1.5f * 20), mouseX, mouseY);
    }
}
