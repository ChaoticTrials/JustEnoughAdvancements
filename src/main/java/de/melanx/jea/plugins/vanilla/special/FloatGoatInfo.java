package de.melanx.jea.plugins.vanilla.special;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.StartRidingTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class FloatGoatInfo implements ICriterionInfo<StartRidingTrigger.TriggerInstance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("minecraft", "husbandry/ride_a_boat_with_a_goat");
    public static final String CRITERION = "ride_a_boat_with_a_goat";
    
    @Override
    public Class<StartRidingTrigger.TriggerInstance> criterionClass() {
        return StartRidingTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, StartRidingTrigger.TriggerInstance instance, IIngredients ii) {
        
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, StartRidingTrigger.TriggerInstance instance, IIngredients ii) {

    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, StartRidingTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(63, SPACE_TOP + 80, 3);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 3.2f);
        RenderEntityCache.renderPlainEntity(mc, EntityType.BOAT, poseStack, buffer);
        poseStack.popPose();
        
        poseStack.pushPose();
        poseStack.translate(63, SPACE_TOP + 86, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.2f);
        SteveRender.defaultPose(mc);
        SteveRender.sitting(true);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteveStatic(mc, poseStack, buffer);
        poseStack.popPose();
        
        poseStack.pushPose();
        poseStack.translate(43, SPACE_TOP + 76, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderFront(poseStack, true, 2.2f);
        RenderEntityCache.renderPlainEntity(mc, EntityType.GOAT, poseStack, buffer);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, StartRidingTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
