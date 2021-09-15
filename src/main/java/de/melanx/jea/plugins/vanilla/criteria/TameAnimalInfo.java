package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class TameAnimalInfo implements ICriterionInfo<TameAnimalTrigger.TriggerInstance> {

    @Override
    public Class<TameAnimalTrigger.TriggerInstance> criterionClass() {
        return TameAnimalTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH / 2d, SPACE_TOP + 80, 0);
        JeaRender.normalize(poseStack);
        RenderEntityCache.renderEntity(mc, instance.entity, poseStack, buffer, JeaRender.entityRenderFront(false, 3), ((ClientTickHandler.ticksInGame + mc.getFrameTime()) % 40) > 20 ? DefaultEntityProperties.DEFAULT : DefaultEntityProperties.TAMED);
        poseStack.popPose();
        Component text = new TranslatableComponent("jea.item.tooltip.tame_animal");
        //noinspection IntegerDivisionInFloatingPointContext
        mc.font.draw(poseStack, text, (RECIPE_WIDTH / 2) - (mc.font.width(text) / 2), SPACE_TOP + 10, 0xffd100);
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.entity, RECIPE_WIDTH / 2d, SPACE_TOP + 80, JeaRender.normalScale(3), ((ClientTickHandler.ticksInGame + Minecraft.getInstance().getFrameTime()) % 40) > 20 ? DefaultEntityProperties.DEFAULT : DefaultEntityProperties.TAMED, mouseX, mouseY);
    }
}
