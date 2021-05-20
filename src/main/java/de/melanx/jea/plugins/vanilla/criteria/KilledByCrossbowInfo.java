package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.KilledByCrossbowTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class KilledByCrossbowInfo implements ICriterionInfo<KilledByCrossbowTrigger.Instance> {

    @Override
    public Class<KilledByCrossbowTrigger.Instance> criterionClass() {
        return KilledByCrossbowTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, KilledByCrossbowTrigger.Instance instance, IIngredients ii) {
        ItemStack stack = new ItemStack(Items.CROSSBOW);
        if ((instance.bounds.getMin() != null && instance.bounds.getMin() >= 2) || instance.entities.length >= 2) {
            stack.addEnchantment(Enchantments.PIERCING, MathHelper.clamp(Math.max(instance.bounds.getMin() == null ? 0 :instance.bounds.getMin(), instance.entities.length) - 1, 1, 4));
        }
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(stack)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, KilledByCrossbowTrigger.Instance instance, IIngredients ii) {
        if (instance.entities.length == 0) {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM_FACING_RIGHT, 33, SPACE_TOP + (RECIPE_HEIGHT / 2) - 30, 48, 48, 0, 0);
        } else {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM_FACING_RIGHT, 8, SPACE_TOP + (RECIPE_HEIGHT / 2) - 30, 48, 48, 0, 0);
        }
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, KilledByCrossbowTrigger.Instance instance, double mouseX, double mouseY) {
        if (!instance.bounds.isUnbounded()) {
            IFormattableTextComponent text = new TranslationTextComponent("jea.item.tooltip.crossbow.unique", IngredientUtil.text(instance.bounds));
            int width = mc.fontRenderer.getStringPropertyWidth(text);
            //noinspection IntegerDivisionInFloatingPointContext
            mc.fontRenderer.drawText(matrixStack, text, (RECIPE_WIDTH / 2) - (width / 2), SPACE_TOP + RECIPE_HEIGHT - 2 - mc.fontRenderer.FONT_HEIGHT, 0x000000);
        }
        if (instance.entities.length > 0) {
            int x;
            int space;
            if (instance.entities.length == 1) {
                x = (80 + (RECIPE_WIDTH - 20)) / 2;
                space = 0;
            } else {
                x = 80;
                space = ((RECIPE_WIDTH - 20) - 80) / (instance.entities.length - 1);
            }
            for (EntityPredicate.AndPredicate entity : instance.entities) {
                matrixStack.push();
                matrixStack.translate(x, SPACE_TOP + 65, 0);
                JeaRender.normalize(matrixStack);
                RenderEntityCache.renderEntity(mc, entity, matrixStack, buffer, JeaRender.entityRenderSide(true, 1.8f));
                matrixStack.pop();
                x += space;
            }
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, KilledByCrossbowTrigger.Instance instance, double mouseX, double mouseY) {
        if (instance.entities.length > 0) {
            int x;
            int space;
            if (instance.entities.length == 1) {
                x = (80 + (RECIPE_WIDTH - 20)) / 2;
                space = 0;
            } else {
                x = 80;
                space = ((RECIPE_WIDTH - 20) - 80) / (instance.entities.length - 1);
            }
            for (EntityPredicate.AndPredicate entity : instance.entities) {
                RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, entity, x, SPACE_TOP + 65, JeaRender.normalScale(1.8f), mouseX, mouseY);
                x += space;
            }
        }
    }
}
