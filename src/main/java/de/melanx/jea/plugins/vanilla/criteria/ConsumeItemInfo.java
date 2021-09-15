package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderMisc;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Optional;

public class ConsumeItemInfo implements ICriterionInfo<ConsumeItemTrigger.TriggerInstance> {

    @Override
    public Class<ConsumeItemTrigger.TriggerInstance> criterionClass() {
        return ConsumeItemTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ConsumeItemTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                IngredientUtil.fromItemPredicate(instance.item, Items.BREAD)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ConsumeItemTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, 10, SPACE_TOP + 20, 48, 48, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ConsumeItemTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.BREAD));
        FoodProperties food = stack.getItem().getFoodProperties();
        if (food != null && food.getNutrition() > 0) {
            poseStack.pushPose();
            poseStack.translate(10, SPACE_TOP + 74, 0);
            RenderMisc.renderFood(poseStack, food, 5);
            poseStack.popPose();
            int x = 0;
            int y = 0;
            for (Pair<MobEffectInstance, Float> effect : food.getEffects()) {
                poseStack.pushPose();
                poseStack.translate(63 + (x * 22), SPACE_TOP + 7 + (y * 22), 0);
                RenderMisc.renderMobEffect(poseStack, mc, effect.getFirst().getEffect());
                poseStack.popPose();
                if (x < 3) { x += 1; } else if (y < 3) { x = 0; y += 1; } else { break; }
            }
        } else {
            Component text = new TranslatableComponent("jea.item.tooltip.consume_item.tooltip");
            mc.font.draw(poseStack, text, 5, SPACE_TOP + 74, 0x000000);
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ConsumeItemTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.BREAD));
        FoodProperties food = stack.getItem().getFoodProperties();
        if (food != null) {
            int x = 0;
            int y = 0;
            for (Pair<MobEffectInstance, Float> effect : food.getEffects()) {
                RenderMisc.addMobEffectTooltip(tooltip, effect.getFirst(), Optional.of(effect.getSecond()), 63 + (x * 22), SPACE_TOP + 7 + (y * 22), mouseX, mouseY);
                if (x < 3) { x += 1; } else if (y < 3) { x = 0; y += 1; } else { break; }
            }
        }
    }
}
