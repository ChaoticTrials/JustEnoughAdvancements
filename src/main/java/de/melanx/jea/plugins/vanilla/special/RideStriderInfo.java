package de.melanx.jea.plugins.vanilla.special;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class RideStriderInfo implements ICriterionInfo<ItemDurabilityTrigger.TriggerInstance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("minecraft", "nether/ride_strider");
    public static final String CRITERION = "used_warped_fungus_on_a_stick";
    
    @Override
    public Class<ItemDurabilityTrigger.TriggerInstance> criterionClass() {
        return ItemDurabilityTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(63, SPACE_TOP + 90, 3);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 1.4f);
        RenderEntityCache.renderPlainEntity(mc, EntityType.STRIDER, poseStack, buffer);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(55, SPACE_TOP + 78, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.2f);
        SteveRender.defaultPose(mc);
        SteveRender.sitting(true);
        SteveRender.setEquipmentHand(mc, new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK));
        SteveRender.renderSteveStatic(mc, poseStack, buffer);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
