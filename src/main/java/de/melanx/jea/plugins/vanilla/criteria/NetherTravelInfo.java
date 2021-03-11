package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.TooltipUtil;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.NetherTravelTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.List;

public class NetherTravelInfo implements ICriterionInfo<NetherTravelTrigger.Instance> {

    @Override
    public Class<NetherTravelTrigger.Instance> criterionClass() {
        return NetherTravelTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, NetherTravelTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, NetherTravelTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, NetherTravelTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 0.8f);
        ChangeDimensionInfo.renderNetherPortal(matrixStack, buffer, mc);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, true, 0.8f);
        ChangeDimensionInfo.renderNetherPortal(matrixStack, buffer, mc);
        matrixStack.pop();
        matrixStack.push();
        //noinspection IntegerDivisionInFloatingPointContext
        matrixStack.translate(RECIPE_WIDTH / 2, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 1.9f);
        SteveRender.defaultPose(mc);
        SteveRender.limbSwing(0.5f);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteveStatic(mc, matrixStack, buffer);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, NetherTravelTrigger.Instance instance, double mouseX, double mouseY) {
        if (mouseY < SPACE_TOP + 90 && mouseY > SPACE_TOP + 25) {
            if (mouseX > 12 && mouseX < 48) {
                List<IFormattableTextComponent> list = new ArrayList<>();
                TooltipUtil.addLocationValues(list, instance.entered);
                if (!list.isEmpty()) {
                    tooltip.add(new TranslationTextComponent("jea.item.tooltip.nether_travel.entered").mergeStyle(TextFormatting.GOLD));
                    tooltip.addAll(list);
                }
            }
            if (mouseX < RECIPE_WIDTH - 12 && mouseX > RECIPE_WIDTH - 48) {
                List<IFormattableTextComponent> list = new ArrayList<>();
                TooltipUtil.addLocationValues(list, instance.exited);
                if (!list.isEmpty()) {
                    tooltip.add(new TranslationTextComponent("jea.item.tooltip.nether_travel.exited").mergeStyle(TextFormatting.GOLD));
                    tooltip.addAll(list);
                }
            }
        }
        if (mouseY < SPACE_TOP + 90 && mouseY > SPACE_TOP + 30
                && mouseX > (RECIPE_WIDTH / 2d) - 12 && mouseX < (RECIPE_WIDTH / 2d) + 12) {
            List<IFormattableTextComponent> list = new ArrayList<>();
            TooltipUtil.addDistanceValues(list, instance.distance);
            if (!list.isEmpty()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.nether_travel.distance").mergeStyle(TextFormatting.GOLD));
                tooltip.addAll(list);
            }
        }
    }
}
