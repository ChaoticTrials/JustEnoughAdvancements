package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

import java.util.List;

public class CureZombieVillagerInfo implements ICriterionInfo<CuredZombieVillagerTrigger.TriggerInstance> {

    @Override
    public Class<CuredZombieVillagerTrigger.TriggerInstance> criterionClass() {
        return CuredZombieVillagerTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, CuredZombieVillagerTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.WEAKNESS)),
                List.of(new ItemStack(Items.GOLDEN_APPLE))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, CuredZombieVillagerTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 20, SPACE_TOP + 47);
        layout.getItemStacks().init(1, true, (RECIPE_WIDTH / 2) + 2, SPACE_TOP + 47);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, CuredZombieVillagerTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, (RECIPE_WIDTH / 2) - 20, SPACE_TOP + 47);
        JeaRender.slotAt(poseStack, (RECIPE_WIDTH / 2) + 2, SPACE_TOP + 47);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        JustEnoughAdvancementsJEIPlugin.getArrow(false).draw(poseStack, (RECIPE_WIDTH / 2) - 16, SPACE_TOP + 29);
        RenderSystem.disableBlend();
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 80, 0);
        JeaRender.normalize(poseStack);
        RenderEntityCache.renderEntity(mc, instance.zombie, poseStack, buffer, JeaRender.entityRenderFront(false, 2), DefaultEntityProperties.ZOMBIE_VILLAGER);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 80, 0);
        JeaRender.normalize(poseStack);
        RenderEntityCache.renderEntity(mc, instance.villager, poseStack, buffer, JeaRender.entityRenderFront(true, 2), DefaultEntityProperties.VILLAGER);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, CuredZombieVillagerTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.zombie, 30, SPACE_TOP + 80, JeaRender.normalScale(2), DefaultEntityProperties.ZOMBIE_VILLAGER, mouseX, mouseY);
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.villager, RECIPE_WIDTH - 30, SPACE_TOP + 80, JeaRender.normalScale(2), DefaultEntityProperties.VILLAGER, mouseX, mouseY);
    }
}
