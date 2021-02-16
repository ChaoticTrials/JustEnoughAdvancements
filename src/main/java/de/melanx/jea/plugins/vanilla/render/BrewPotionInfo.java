package de.melanx.jea.plugins.vanilla.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.BrewedPotionTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class BrewPotionInfo implements ICriterionInfo<BrewedPotionTrigger.Instance> {
    
    @SuppressWarnings("UnstableApiUsage")
    private final List<ItemStack> potionCycle = IntStream.range(0, 360)
            .filter(i -> i % 20 == 0).boxed()
            .map(i -> {
                ItemStack stack = new ItemStack(Items.POTION);
                stack.setDisplayName(new TranslationTextComponent("jea.item.tooltip.any_potion").mergeStyle(TextFormatting.GOLD));
                CompoundNBT nbt = stack.getOrCreateTag();
                nbt.putInt("CustomPotionColor", Color.getHSBColor(i / 360f, 1, 1).getRGB());
                stack.setTag(nbt);
                return stack;
            }).collect(ImmutableList.toImmutableList());

    @Override
    public Class<BrewedPotionTrigger.Instance> criterionClass() {
        return BrewedPotionTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.Instance instance, IIngredients ii) {
        if (instance.potion != null) {
            ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), instance.potion);
            ItemStack splash = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), instance.potion);
            ItemStack lingering = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), instance.potion);
            ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                    ImmutableList.of(potion, splash, lingering)
            ));
        } else {
            ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                    this.potionCycle
            ));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) - 24, SPACE_TOP + 20, 48, 48, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.Instance instance, double mouseX, double mouseY) {
        //
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.Instance instance, double mouseX, double mouseY) {
        //
    }
}
