package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class EntityInteractionInfo implements ICriterionInfo<PlayerInteractTrigger.TriggerInstance> {

    @Override
    public Class<PlayerInteractTrigger.TriggerInstance> criterionClass() {
        return PlayerInteractTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, PlayerInteractTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, PlayerInteractTrigger.TriggerInstance instance, IIngredients ii) {
        
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, PlayerInteractTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 40;
        float swing = 0;
        ItemStack stack = ItemStack.EMPTY;
        DefaultEntityProperties properties = DefaultEntityProperties.DEFAULT;
        if (animationTime < 20) {
            stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID));
        } else {
            properties = new DefaultEntityProperties(null, false, false, false, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)), 0, 0, 0, 0, 0, false, false);
        }
        if (animationTime > 17 && animationTime < 23) {
            swing = (animationTime - 17) / 6f;
        }
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        RenderEntityCache.renderEntity(mc, instance.entity, poseStack, buffer, JeaRender.entityRenderFront(true, 2.6f), properties);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, PlayerInteractTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.entity, RECIPE_WIDTH - 30, SPACE_TOP + 90, JeaRender.normalScale(2.6f), mouseX, mouseY);
    }
}
