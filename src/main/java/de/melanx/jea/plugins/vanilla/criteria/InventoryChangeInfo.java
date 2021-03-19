package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.Items;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryChangeInfo implements ICriterionInfo<InventoryChangeTrigger.Instance> {

    @Override
    public Class<InventoryChangeTrigger.Instance> criterionClass() {
        return InventoryChangeTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, Arrays.stream(instance.items).map(predicate -> IngredientUtil.fromItemPredicate(predicate, Items.STRUCTURE_VOID)).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.Instance instance, IIngredients ii) {
        if (instance.items.length == 1) {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) - 24, SPACE_TOP + 36, 48, 48, 0, 0);
        } else {
            for (int i = 0; i < instance.items.length; i++) {
                layout.getItemStacks().init(i, true, 5 + ((i % 9) * 18), SPACE_TOP + 24 + ((i / 9) * 18));
            }
        }
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.Instance instance, double mouseX, double mouseY) {
        if (instance.items.length >= 2) {
            for (int i = 0; i < instance.items.length; i++) {
                JeaRender.slotAt(matrixStack, 5 + ((i % 9) * 18), SPACE_TOP + 24 + ((i / 9) * 18));
            }
        }
        if (instance.items.length > 0) {
            IFormattableTextComponent text = new TranslationTextComponent(instance.items.length == 1 ? "jea.item.tooltip.inventory.one" : "jea.item.tooltip.inventory.multiple");
            int width = mc.fontRenderer.getStringPropertyWidth(text);
            //noinspection IntegerDivisionInFloatingPointContext
            mc.fontRenderer.func_243248_b(matrixStack, text, (RECIPE_WIDTH / 2) - (width / 2), SPACE_TOP + 9, 0x000000);
        }
        int y = SPACE_TOP + RECIPE_HEIGHT - 5 - mc.fontRenderer.FONT_HEIGHT;
        if (!instance.full.isUnbounded()) {
            IFormattableTextComponent text = new TranslationTextComponent("jea.item.tooltip.inventory.full", IngredientUtil.text(instance.full));
            mc.fontRenderer.func_243248_b(matrixStack, text, 5, y, 0x000000);
            y -= (mc.fontRenderer.FONT_HEIGHT + 2);
        }
        if (!instance.occupied.isUnbounded()) {
            IFormattableTextComponent text = new TranslationTextComponent("jea.item.tooltip.inventory.occupied", IngredientUtil.text(instance.occupied));
            mc.fontRenderer.func_243248_b(matrixStack, text, 5, y, 0x000000);
            y -= (mc.fontRenderer.FONT_HEIGHT + 2);
        }
        if (!instance.empty.isUnbounded()) {
            IFormattableTextComponent text = new TranslationTextComponent("jea.item.tooltip.inventory.empty", IngredientUtil.text(instance.empty));
            mc.fontRenderer.func_243248_b(matrixStack, text, 5, y, 0x000000);
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.Instance instance, double mouseX, double mouseY) {
        //
    }
}
