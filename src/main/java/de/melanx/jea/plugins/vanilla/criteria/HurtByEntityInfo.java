package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DamageUtil;
import de.melanx.jea.render.JeaRender;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class HurtByEntityInfo implements ICriterionInfo<EntityHurtPlayerTrigger.TriggerInstance> {

    @Override
    public Class<EntityHurtPlayerTrigger.TriggerInstance> criterionClass() {
        return EntityHurtPlayerTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, EntityHurtPlayerTrigger.TriggerInstance instance, IIngredients ii) {
        
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, EntityHurtPlayerTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        stack.setHoverName(new TranslatableComponent("jea.item.tooltip.damage.hurt_by_entity").withStyle(ChatFormatting.RED));
        layout.getItemStacks().set(0, stack);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, EntityHurtPlayerTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        DamageUtil.draw(poseStack, buffer, mc, instance.damage, null, false, true);
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, EntityHurtPlayerTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        DamageUtil.addTooltip(tooltip, Minecraft.getInstance(), instance.damage, null, false, true, mouseX, mouseY);
    }
}
