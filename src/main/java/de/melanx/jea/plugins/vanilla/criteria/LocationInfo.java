package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.TooltipUtil;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.PositionTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class LocationInfo implements ICriterionInfo<PositionTrigger.Instance> {

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
        matrixStack.translate(17, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.limbSwing(0.25f);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteveStatic(mc, matrixStack, buffer);
        matrixStack.pop();
        List<IFormattableTextComponent> text = new ArrayList<>();
        TooltipUtil.addLocationValuesNoIn(text, instance.location);
        mc.fontRenderer.drawText(matrixStack, new TranslationTextComponent("jea.item.tooltip.location_trigger").mergeStyle(TextFormatting.DARK_RED), 42, SPACE_TOP + 14, 0x000000);
        int y = SPACE_TOP + 28;
        for (IFormattableTextComponent line : text) {
            mc.fontRenderer.drawText(matrixStack, line, 38, y, 0x000000);
            y += (mc.fontRenderer.FONT_HEIGHT + 2);
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, PositionTrigger.Instance instance, double mouseX, double mouseY) {
        //
    }
}
