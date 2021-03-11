package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.Optional;

public class ConsumeItemInfo implements ICriterionInfo<ConsumeItemTrigger.Instance> {

    @Override
    public Class<ConsumeItemTrigger.Instance> criterionClass() {
        return ConsumeItemTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ConsumeItemTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.fromItemPredicate(instance.item, Items.BREAD)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ConsumeItemTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, 10, SPACE_TOP + 20, 48, 48, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ConsumeItemTrigger.Instance instance, double mouseX, double mouseY) {
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.BREAD));
        Food food = stack.getItem().getFood();
        if (food != null) {
            matrixStack.push();
            matrixStack.translate(10, SPACE_TOP + 74, 0);
            RenderMisc.renderFood(matrixStack, food, 5);
            matrixStack.pop();
            int x = 0;
            int y = 0;
            for (Pair<EffectInstance, Float> effect : food.getEffects()) {
                matrixStack.push();
                matrixStack.translate(63 + (x * 22), SPACE_TOP + 7 + (y * 22), 0);
                RenderMisc.renderMobEffect(matrixStack, mc, effect.getFirst().getPotion());
                matrixStack.pop();
                if (x < 3) { x += 1; } else if (y < 3) { x = 0; y += 1; } else { break; }
            }
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ConsumeItemTrigger.Instance instance, double mouseX, double mouseY) {
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.BREAD));
        Food food = stack.getItem().getFood();
        if (food != null) {
            int x = 0;
            int y = 0;
            for (Pair<EffectInstance, Float> effect : food.getEffects()) {
                RenderMisc.addMobEffectTooltip(tooltip, effect.getFirst(), Optional.of(effect.getSecond()), 63 + (x * 22), SPACE_TOP + 7 + (y * 22), mouseX, mouseY);
                if (x < 3) { x += 1; } else if (y < 3) { x = 0; y += 1; } else { break; }
            }
        }
    }
}
