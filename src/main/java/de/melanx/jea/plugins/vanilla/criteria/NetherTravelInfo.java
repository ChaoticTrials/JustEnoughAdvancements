package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.TooltipUtil;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.NetherTravelTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

public class NetherTravelInfo implements ICriterionInfo<NetherTravelTrigger.TriggerInstance> {

    @Override
    public Class<NetherTravelTrigger.TriggerInstance> criterionClass() {
        return NetherTravelTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, NetherTravelTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, NetherTravelTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, NetherTravelTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 0.8f);
        ChangeDimensionInfo.renderNetherPortal(poseStack, buffer, mc);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, true, 0.8f);
        ChangeDimensionInfo.renderNetherPortal(poseStack, buffer, mc);
        poseStack.popPose();
        poseStack.pushPose();
        //noinspection IntegerDivisionInFloatingPointContext
        poseStack.translate(RECIPE_WIDTH / 2, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 1.9f);
        SteveRender.defaultPose(mc);
        SteveRender.limbSwing(0.5f);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteveStatic(mc, poseStack, buffer);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, NetherTravelTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (mouseY < SPACE_TOP + 90 && mouseY > SPACE_TOP + 30 && mouseX > (RECIPE_WIDTH / 2d) - 12 && mouseX < (RECIPE_WIDTH / 2d) + 12) {
            List<MutableComponent> list = new ArrayList<>();
            TooltipUtil.addDistanceValues(list, instance.distance);
            if (!list.isEmpty()) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.nether_travel.distance").withStyle(ChatFormatting.GOLD));
                tooltip.addAll(list);
            }
        }
        if (mouseY < SPACE_TOP + 90 && mouseY > SPACE_TOP + 25) {
            if (mouseX > 12 && mouseX < 48) {
                List<MutableComponent> list = new ArrayList<>();
                TooltipUtil.addLocationValues(list, instance.entered);
                if (!list.isEmpty()) {
                    tooltip.add(new TranslatableComponent("jea.item.tooltip.nether_travel.entered").withStyle(ChatFormatting.GOLD));
                    tooltip.addAll(list);
                }
            }
            if (mouseX < RECIPE_WIDTH - 12 && mouseX > RECIPE_WIDTH - 48) {
                List<MutableComponent> list = new ArrayList<>();
                TooltipUtil.addLocationValues(list, instance.exited);
                if (!list.isEmpty()) {
                    tooltip.add(new TranslatableComponent("jea.item.tooltip.nether_travel.exited").withStyle(ChatFormatting.GOLD));
                    tooltip.addAll(list);
                }
            }
        }
    }
}
