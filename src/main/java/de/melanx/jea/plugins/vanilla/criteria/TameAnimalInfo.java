package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.TameAnimalTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class TameAnimalInfo implements ICriterionInfo<TameAnimalTrigger.Instance> {

    @Override
    public Class<TameAnimalTrigger.Instance> criterionClass() {
        return TameAnimalTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH / 2d, SPACE_TOP + 80, 0);
        JeaRender.normalize(matrixStack);
        RenderEntityCache.renderEntity(mc, instance.entity, matrixStack, buffer, JeaRender.entityRenderFront(false, 3), ((ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 40) > 20 ? DefaultEntityProperties.DEFAULT : DefaultEntityProperties.TAMED);
        matrixStack.pop();
        ITextComponent text = new TranslationTextComponent("jea.item.tooltip.tame_animal");
        //noinspection IntegerDivisionInFloatingPointContext
        mc.fontRenderer.func_243248_b(matrixStack, text, (RECIPE_WIDTH / 2) - (mc.fontRenderer.getStringPropertyWidth(text) / 2), SPACE_TOP + 10, 0xffd100);
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.Instance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.entity, RECIPE_WIDTH / 2d, SPACE_TOP + 80, JeaRender.normalScale(3), ((ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()) % 40) > 20 ? DefaultEntityProperties.DEFAULT : DefaultEntityProperties.TAMED, mouseX, mouseY);
    }
}
