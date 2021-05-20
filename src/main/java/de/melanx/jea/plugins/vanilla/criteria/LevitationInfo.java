package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.TooltipUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.LevitationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class LevitationInfo implements ICriterionInfo<LevitationTrigger.Instance> {

    @Override
    public Class<LevitationTrigger.Instance> criterionClass() {
        return LevitationTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, LevitationTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, LevitationTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, LevitationTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 40, SPACE_TOP + 85, 0);
        JeaRender.normalize(matrixStack);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-15));
        JeaRender.transformForEntityRenderFront(matrixStack, true, 2.2f);
        RenderEntityCache.renderPlainEntity(mc, EntityType.SHULKER, matrixStack, buffer);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(15, SPACE_TOP + 86 - ((ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 25), 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderFront(matrixStack, false, 2);
        SteveRender.defaultPose(mc);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        List<IFormattableTextComponent> text = new ArrayList<>();
        if (!instance.duration.isUnbounded()) {
            text.add(new TranslationTextComponent("jea.item.tooltip.levitate.duration", IngredientUtil.text(instance.duration, value -> new StringTextComponent(TooltipUtil.formatTimeTicks(value)))));
        }
        TooltipUtil.addDistanceValues(text, instance.distance);
        int y = SPACE_TOP + 6;
        for (IFormattableTextComponent tc : text) {
            mc.fontRenderer.drawText(matrixStack, tc, 30, y, 0x000000);
            y += (mc.fontRenderer.FONT_HEIGHT + 2);
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, LevitationTrigger.Instance instance, double mouseX, double mouseY) {
        
    }
}
