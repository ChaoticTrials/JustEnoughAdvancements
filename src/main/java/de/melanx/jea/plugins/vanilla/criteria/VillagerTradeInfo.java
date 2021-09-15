package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.TradeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class VillagerTradeInfo implements ICriterionInfo<TradeTrigger.TriggerInstance> {

    @Override
    public Class<TradeTrigger.TriggerInstance> criterionClass() {
        return TradeTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, TradeTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(new ItemStack(Items.EMERALD)),
                IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, TradeTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        layout.getItemStacks().init(1, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 24);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, TradeTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        JeaRender.slotAt(poseStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 24);
        poseStack.pushPose();
        //noinspection IntegerDivisionInFloatingPointContext
        poseStack.translate((RECIPE_WIDTH / 2) - 28, SPACE_TOP + 5, 0);
        RenderSystem.enableBlend();
        this.renderArrow(poseStack, false);
        poseStack.translate(0, 20, 0);
        this.renderArrow(poseStack, true);
        RenderSystem.disableBlend();
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(20, SPACE_TOP + 80, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderFront(poseStack, false, 2.2f);
        SteveRender.defaultPose(mc);
        SteveRender.setEquipmentHand(mc, new ItemStack(Items.EMERALD));
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 20, SPACE_TOP + 80, 0);
        JeaRender.normalize(poseStack);
        DefaultEntityProperties properties = new DefaultEntityProperties(EntityType.VILLAGER, false, false, false, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)), 0, 0, 0, 0, 0, false, false);
        RenderEntityCache.renderEntity(mc, instance.villager, poseStack, buffer, JeaRender.entityRenderFront(true, 2.2f), properties);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, TradeTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        DefaultEntityProperties properties = new DefaultEntityProperties(EntityType.VILLAGER, false, false, false, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)), 0, 0, 0, 0, 0, false, false);
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.villager, RECIPE_WIDTH - 20, SPACE_TOP + 80, JeaRender.normalScale(2.2f), properties, mouseX, mouseY);
    }
    
    private void renderArrow(PoseStack poseStack, boolean left) {
        poseStack.pushPose();
        JustEnoughAdvancementsJEIPlugin.getArrow(left).draw(poseStack, 0, 0, 0, 0, 0, 16);
        poseStack.translate(24, 0, 0);
        JustEnoughAdvancementsJEIPlugin.getArrow(left).draw(poseStack, 0, 0, 0, 0, 16, 0);
        poseStack.popPose();
    }
}
