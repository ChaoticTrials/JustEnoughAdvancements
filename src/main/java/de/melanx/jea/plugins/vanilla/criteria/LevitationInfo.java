package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
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
import net.minecraft.advancements.critereon.LevitationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class LevitationInfo implements ICriterionInfo<LevitationTrigger.TriggerInstance> {

    @Override
    public Class<LevitationTrigger.TriggerInstance> criterionClass() {
        return LevitationTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, LevitationTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, LevitationTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, LevitationTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 40, SPACE_TOP + 85, 0);
        JeaRender.normalize(poseStack);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-15));
        JeaRender.transformForEntityRenderFront(poseStack, true, 2.2f);
        RenderEntityCache.renderPlainEntity(mc, EntityType.SHULKER, poseStack, buffer);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(15, SPACE_TOP + 86 - ((ClientTickHandler.ticksInGame + mc.getFrameTime()) % 25), 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderFront(poseStack, false, 2);
        SteveRender.defaultPose(mc);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        List<MutableComponent> text = new ArrayList<>();
        if (!instance.duration.isAny()) {
            text.add(new TranslatableComponent("jea.item.tooltip.levitate.duration", IngredientUtil.text(instance.duration, value -> new TextComponent(TooltipUtil.formatTimeTicks(value)))));
        }
        TooltipUtil.addDistanceValues(text, instance.distance);
        int y = SPACE_TOP + 6;
        for (MutableComponent tc : text) {
            mc.font.draw(poseStack, tc, 30, y, 0x000000);
            y += (mc.font.lineHeight + 2);
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, LevitationTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        
    }
}
