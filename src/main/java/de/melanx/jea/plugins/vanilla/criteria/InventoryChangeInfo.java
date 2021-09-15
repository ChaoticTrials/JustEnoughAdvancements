package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryChangeInfo implements ICriterionInfo<InventoryChangeTrigger.TriggerInstance> {

    @Override
    public Class<InventoryChangeTrigger.TriggerInstance> criterionClass() {
        return InventoryChangeTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, Arrays.stream(instance.predicates).map(predicate -> IngredientUtil.fromItemPredicate(predicate, Items.STRUCTURE_VOID)).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.TriggerInstance instance, IIngredients ii) {
        if (instance.predicates.length == 1) {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) - 24, SPACE_TOP + 36, 48, 48, 0, 0);
        } else {
            for (int i = 0; i < instance.predicates.length; i++) {
                layout.getItemStacks().init(i, true, 5 + ((i % 9) * 18), SPACE_TOP + 24 + ((i / 9) * 18));
            }
        }
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (instance.predicates.length >= 2) {
            for (int i = 0; i < instance.predicates.length; i++) {
                JeaRender.slotAt(poseStack, 5 + ((i % 9) * 18), SPACE_TOP + 24 + ((i / 9) * 18));
            }
        }
        if (instance.predicates.length > 0) {
            MutableComponent text = new TranslatableComponent(instance.predicates.length == 1 ? "jea.item.tooltip.inventory.one" : "jea.item.tooltip.inventory.multiple");
            int width = mc.font.width(text);
            //noinspection IntegerDivisionInFloatingPointContext
            mc.font.draw(poseStack, text, (RECIPE_WIDTH / 2) - (width / 2), SPACE_TOP + 9, 0x000000);
        }
        int y = SPACE_TOP + RECIPE_HEIGHT - 5 - mc.font.lineHeight;
        if (!instance.slotsFull.isAny()) {
            MutableComponent text = new TranslatableComponent("jea.item.tooltip.inventory.full", IngredientUtil.text(instance.slotsFull));
            mc.font.draw(poseStack, text, 5, y, 0x000000);
            y -= (mc.font.lineHeight + 2);
        }
        if (!instance.slotsOccupied.isAny()) {
            MutableComponent text = new TranslatableComponent("jea.item.tooltip.inventory.occupied", IngredientUtil.text(instance.slotsOccupied));
            mc.font.draw(poseStack, text, 5, y, 0x000000);
            y -= (mc.font.lineHeight + 2);
        }
        if (!instance.slotsEmpty.isAny()) {
            MutableComponent text = new TranslatableComponent("jea.item.tooltip.inventory.empty", IngredientUtil.text(instance.slotsEmpty));
            mc.font.draw(poseStack, text, 5, y, 0x000000);
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        //
    }
}
