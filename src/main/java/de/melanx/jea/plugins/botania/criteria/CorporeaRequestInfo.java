package de.melanx.jea.plugins.botania.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.TooltipUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.botania.common.advancements.CorporeaRequestTrigger;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CorporeaRequestInfo implements ICriterionInfo<CorporeaRequestTrigger.Instance> {

    private final TileCorporeaIndex tile = new TileCorporeaIndex();
    private TileEntityRenderer<TileCorporeaIndex> tileRender = null;

    @Override
    public Class<CorporeaRequestTrigger.Instance> criterionClass() {
        return CorporeaRequestTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, CorporeaRequestTrigger.Instance instance, IIngredients ii) {
        
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, CorporeaRequestTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, CorporeaRequestTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 78, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderFront(matrixStack, false, 2.4f);
        SteveRender.defaultPose(mc);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 65, SPACE_TOP + 65, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-13));
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 3.2f);
        this.tile.setWorldAndPos(Objects.requireNonNull(mc.world), BlockPos.ZERO);
        this.tile.cachedBlockState = ModBlocks.corporeaIndex.getDefaultState();
        this.tile.hasCloseby = true;
        this.tile.ticksWithCloseby = ClientTickHandler.ticksInGame;
        this.tile.ticks = ClientTickHandler.ticksInGame;
        if (this.tileRender == null) {
            this.tileRender = TileEntityRendererDispatcher.instance.getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            this.tileRender.render(this.tile, mc.getRenderPartialTicks(), matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        }
        matrixStack.pop();

        if (!instance.getCount().isUnbounded()) {
            ITextComponent text = new TranslationTextComponent("jea.item.tooltip.botania.corporea_request_amount", IngredientUtil.text(instance.getCount()));
            mc.fontRenderer.func_243248_b(matrixStack, text, 10, SPACE_TOP + 80, 0x000000);
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, CorporeaRequestTrigger.Instance instance, double mouseX, double mouseY) {
        if (mouseX < RECIPE_WIDTH - 8 && mouseX > RECIPE_WIDTH - 55 && mouseY > SPACE_TOP + 28 && mouseY < SPACE_TOP + 67) {
            List<IFormattableTextComponent> list = new ArrayList<>();
            TooltipUtil.addLocationValues(list, instance.getIndexPos());
            if (!list.isEmpty()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.botania.corporea_location").mergeStyle(TextFormatting.GOLD));
                tooltip.addAll(list);
            }
        }
    }
}
