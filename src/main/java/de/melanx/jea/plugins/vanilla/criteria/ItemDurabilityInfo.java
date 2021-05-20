package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.ItemDurabilityTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.Items;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class ItemDurabilityInfo implements ICriterionInfo<ItemDurabilityTrigger.Instance> {

    @Override
    public Class<ItemDurabilityTrigger.Instance> criterionClass() {
        return ItemDurabilityTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.fromItemPredicate(instance.item, Items.CARROT_ON_A_STICK)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.Instance instance, IIngredients ii) {
        if (instance.durability.isUnbounded() && instance.delta.isUnbounded()) {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) - 24, SPACE_TOP + (RECIPE_HEIGHT / 2) - 18, 48, 48, 0, 0);
        } else {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) - 24, SPACE_TOP + 8, 48, 48, 0, 0);
        }
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.Instance instance, double mouseX, double mouseY) {
        if (instance.durability.isUnbounded() && instance.delta.isUnbounded()) {
            IFormattableTextComponent text = new TranslationTextComponent("jea.item.tooltip.damage.any");
            int width = mc.fontRenderer.getStringPropertyWidth(text);
            //noinspection IntegerDivisionInFloatingPointContext
            mc.fontRenderer.drawText(matrixStack, text, (RECIPE_WIDTH / 2) - (width / 2), SPACE_TOP + 9, 0x000000);
        } else {
            MinMaxBounds.IntBound durability = new MinMaxBounds.IntBound(2, 5);
            MinMaxBounds.IntBound delta = new MinMaxBounds.IntBound(0, 0);
            
            int y = SPACE_TOP + 65;
            if (!durability.isUnbounded()) {
                IFormattableTextComponent text = new TranslationTextComponent("jea.item.tooltip.damage.total", IngredientUtil.text(durability));
                mc.fontRenderer.drawText(matrixStack, text, 5, y, 0x000000);
                y += (2 + mc.fontRenderer.FONT_HEIGHT);
            }
            if (!delta.isUnbounded()) {
                IFormattableTextComponent text;
                if (((delta.getMin() == null || delta.getMin() >= 0) && (delta.getMax() == null || delta.getMax() > 0)) || ((delta.getMin() == null || delta.getMin() > 0) && (delta.getMax() == null || delta.getMax() >= 0))) {
                    text = new TranslationTextComponent("jea.item.tooltip.damage.delta.add", IngredientUtil.text(delta));
                } else if (((delta.getMin() == null || delta.getMin() <= 0) && (delta.getMax() == null || delta.getMax() < 0)) || ((delta.getMin() == null || delta.getMin() < 0) && (delta.getMax() == null || delta.getMax() <= 0))) {
                    text = new TranslationTextComponent("jea.item.tooltip.damage.delta.remove", IngredientUtil.text(delta, number -> new StringTextComponent(Integer.toString(-number))));
                } else {
                    text = new TranslationTextComponent("jea.item.tooltip.damage.delta.any", IngredientUtil.text(delta));
                }
                mc.fontRenderer.drawText(matrixStack, text, 5, y, 0x000000);
            }
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
