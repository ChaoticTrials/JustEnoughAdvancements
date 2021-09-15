package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

import java.util.List;

public class UseTotemInfo implements ICriterionInfo<UsedTotemTrigger.TriggerInstance> {

    @Override
    public Class<UsedTotemTrigger.TriggerInstance> criterionClass() {
        return UsedTotemTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, UsedTotemTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                IngredientUtil.fromItemPredicate(instance.item, true, Items.TOTEM_OF_UNDYING)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, UsedTotemTrigger.TriggerInstance instance, IIngredients ii) {
         layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) + 10, SPACE_TOP + 36, 48, 48, 0, 0);
         layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, UsedTotemTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 85, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderFront(poseStack, false, 2.7f);
        SteveRender.defaultPose(mc);
        SteveRender.hurtTime(10);
        SteveRender.use(10, InteractionHand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, true, Items.TOTEM_OF_UNDYING)));
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        Component text = new TranslatableComponent("jea.item.tooltip.totem");
        mc.font.draw(poseStack, text, 60, SPACE_TOP + 15, 0x000000);
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, UsedTotemTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
