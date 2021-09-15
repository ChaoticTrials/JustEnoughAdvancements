package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Items;

import java.util.List;

public class ItemDurabilityInfo implements ICriterionInfo<ItemDurabilityTrigger.TriggerInstance> {

    @Override
    public Class<ItemDurabilityTrigger.TriggerInstance> criterionClass() {
        return ItemDurabilityTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                IngredientUtil.fromItemPredicate(instance.item, Items.CARROT_ON_A_STICK)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.TriggerInstance instance, IIngredients ii) {
        if (instance.durability.isAny() && instance.delta.isAny()) {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) - 24, SPACE_TOP + (RECIPE_HEIGHT / 2) - 18, 48, 48, 0, 0);
        } else {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) - 24, SPACE_TOP + 8, 48, 48, 0, 0);
        }
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (instance.durability.isAny() && instance.delta.isAny()) {
            MutableComponent text = new TranslatableComponent("jea.item.tooltip.damage.any");
            int width = mc.font.width(text);
            //noinspection IntegerDivisionInFloatingPointContext
            mc.font.draw(poseStack, text, (RECIPE_WIDTH / 2) - (width / 2), SPACE_TOP + 9, 0x000000);
        } else {
            MinMaxBounds.Ints durability = new MinMaxBounds.Ints(2, 5);
            MinMaxBounds.Ints delta = new MinMaxBounds.Ints(0, 0);
            
            int y = SPACE_TOP + 65;
            if (!durability.isAny()) {
                MutableComponent text = new TranslatableComponent("jea.item.tooltip.damage.total", IngredientUtil.text(durability));
                mc.font.draw(poseStack, text, 5, y, 0x000000);
                y += (2 + mc.font.lineHeight);
            }
            if (!delta.isAny()) {
                MutableComponent text;
                if (((delta.getMin() == null || delta.getMin() >= 0) && (delta.getMax() == null || delta.getMax() > 0)) || ((delta.getMin() == null || delta.getMin() > 0) && (delta.getMax() == null || delta.getMax() >= 0))) {
                    text = new TranslatableComponent("jea.item.tooltip.damage.delta.add", IngredientUtil.text(delta));
                } else if (((delta.getMin() == null || delta.getMin() <= 0) && (delta.getMax() == null || delta.getMax() < 0)) || ((delta.getMin() == null || delta.getMin() < 0) && (delta.getMax() == null || delta.getMax() <= 0))) {
                    text = new TranslatableComponent("jea.item.tooltip.damage.delta.remove", IngredientUtil.text(delta, number -> new TextComponent(Integer.toString(-number))));
                } else {
                    text = new TranslatableComponent("jea.item.tooltip.damage.delta.any", IngredientUtil.text(delta));
                }
                mc.font.draw(poseStack, text, 5, y, 0x000000);
            }
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
