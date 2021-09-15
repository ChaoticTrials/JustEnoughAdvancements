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
import net.minecraft.advancements.critereon.LocationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

public class LocationInfo implements ICriterionInfo<LocationTrigger.TriggerInstance> {

    @Override
    public Class<LocationTrigger.TriggerInstance> criterionClass() {
        return LocationTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(17, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.limbSwing(0.25f);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteveStatic(mc, poseStack, buffer);
        poseStack.popPose();
        List<MutableComponent> text = new ArrayList<>();
        TooltipUtil.addLocationValuesNoIn(text, instance.location);
        mc.font.draw(poseStack, new TranslatableComponent("jea.item.tooltip.location_trigger").withStyle(ChatFormatting.DARK_RED), 42, SPACE_TOP + 14, 0x000000);
        int y = SPACE_TOP + 28;
        for (MutableComponent line : text) {
            mc.font.draw(poseStack, line, 38, y, 0x000000);
            y += (mc.font.lineHeight + 2);
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        //
    }
}
