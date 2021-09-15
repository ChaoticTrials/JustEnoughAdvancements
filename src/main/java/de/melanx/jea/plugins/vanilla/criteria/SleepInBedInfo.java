package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.TooltipUtil;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.LocationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;

public class SleepInBedInfo implements ICriterionInfo<LocationTrigger.TriggerInstance> {

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
        poseStack.translate(116, SPACE_TOP + 50, 0);
        JeaRender.normalize(poseStack);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-15));
        JeaRender.transformForEntityRenderFront(poseStack, true, 2.8f);
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
        poseStack.translate(-0.5, -0.6, 0.95);
        int light = LightTexture.pack(15, 15);
        //noinspection deprecation
        mc.getBlockRenderer().renderSingleBlock(Blocks.RED_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT), poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        SteveRender.defaultPose(mc);
        SteveRender.setPose(mc, 0, 16, 0, 0, Pose.SLEEPING, InteractionHand.MAIN_HAND, false, false, 0, false);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (mouseX > 26 && mouseX < RECIPE_WIDTH - 26 && mouseY > SPACE_TOP + 28 && mouseY < SPACE_TOP + 82) {
            ArrayList<MutableComponent> list = new ArrayList<>();
            TooltipUtil.addLocationValues(list, instance.location);
            tooltip.addAll(list);
        }
    }
}
