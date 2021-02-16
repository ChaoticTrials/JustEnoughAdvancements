package de.melanx.jea.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IngredientUtil {
    
    public static List<ItemStack> ingredients(@Nullable IItemProvider input, IItemProvider... defaults) {
        if (input == null) {
            return Arrays.stream(defaults).map(item -> new ItemStack(item.asItem())).collect(Collectors.toList());
        } else {
            return ImmutableList.of(new ItemStack(input.asItem()));
        }
    }
    
    public static List<ItemStack> fromItemPredicate(ItemPredicate predicate, IItemProvider... defaults) {
        List<Item> items;
        boolean anyItemTitle = false;
        if (predicate.item != null) {
            items = ImmutableList.of(predicate.item);
        } else if (predicate.tag != null && !predicate.tag.getAllElements().isEmpty()) {
            items = predicate.tag.getAllElements();
        } else {
            anyItemTitle = true;
            items = Arrays.stream(defaults).map(p -> p == null ? null : p.asItem()).collect(Collectors.toList());
        }
        List<IFormattableTextComponent> tooltip = new ArrayList<>();
        if (!predicate.count.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.amount", text(predicate.count)));
        }
        if (!predicate.durability.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.durability", text(predicate.durability)));
        }
        for (EnchantmentPredicate ench : predicate.enchantments) {
            tooltip.add(new TranslationTextComponent(
                    "jea.item.tooltip.enchantment",
                    ench.enchantment.getDisplayName(1),
                    text(ench.levels, level -> new TranslationTextComponent("enchantment.level." + level))
            ));
        }
        for (EnchantmentPredicate ench : predicate.bookEnchantments) {
            tooltip.add(new TranslationTextComponent(
                    "jea.item.tooltip.book_enchantment",
                    ench.enchantment.getDisplayName(1),
                    text(ench.levels, level -> new TranslationTextComponent("enchantment.level." + level))
            ));
        }
        if (predicate.potion != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.potion", new TranslationTextComponent(predicate.potion.getNamePrefixed("item." + Objects.requireNonNull(predicate.potion.getRegistryName()).getNamespace() + ".potion.effect."))));
        }
        ListNBT tooltipNBT = new ListNBT();
        for (IFormattableTextComponent tc : tooltip) {
            tooltipNBT.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(tc.mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.AQUA))));
        }
        
        List<ItemStack> stacks = new ArrayList<>();
        for (Item item : items) {
            ItemStack stack = item == null ? ItemStack.EMPTY.copy() : new ItemStack(item);
            if (anyItemTitle) {
                stack.setDisplayName(new TranslationTextComponent("jea.item.tooltip.any_item").mergeStyle(TextFormatting.GOLD));
            }
            getExampleValue(predicate.count).ifPresent(stack::setCount);
            if (stack.isDamageable()) {
                getExampleValue(predicate.durability).ifPresent(stack::setDamage);
            }
            if (predicate.nbt.tag != null) {
                CompoundNBT nbt = stack.getOrCreateTag();
                nbt.merge(predicate.nbt.tag);
                stack.setTag(nbt);
            }
            if ((stack.getItem() == Items.ENCHANTED_BOOK ? predicate.bookEnchantments : predicate.enchantments).length > 0) {
                CompoundNBT nbt = stack.getOrCreateTag();
                ListNBT list = new ListNBT();
                list.add(new CompoundNBT());
                nbt.put("Enchantments", list);
            }
            CompoundNBT display = stack.getOrCreateChildTag("display");
            display.put("Lore", tooltipNBT);
            stacks.add(stack);
        }
        return stacks;
    }
    
    public static IFormattableTextComponent text(MinMaxBounds<?> bounds) {
        if (bounds.isUnbounded() || (bounds.getMin() == null && bounds.getMax() == null)) {
            return new TranslationTextComponent("jea.range.unbounded");
        } else if (bounds.getMin() == null) {
            return new TranslationTextComponent("jea.range.lower", Objects.requireNonNull(bounds.getMax()).toString());
        } else if (bounds.getMax() == null) {
            return new TranslationTextComponent("jea.range.greater", Objects.requireNonNull(bounds.getMin()).toString());
        } else if (bounds.getMin().equals(bounds.getMax())) {
            return new StringTextComponent(bounds.getMin().toString());
        } else {
            return new TranslationTextComponent("jea.range.bounded", Objects.requireNonNull(bounds.getMin()).toString(), Objects.requireNonNull(bounds.getMax()).toString());
        }
    }
    
    public static <T extends Number> IFormattableTextComponent text(MinMaxBounds<T> bounds, Function<T, IFormattableTextComponent> factory) {
        if (bounds.isUnbounded() || (bounds.getMin() == null && bounds.getMax() == null)) {
            return new TranslationTextComponent("jea.range.unbounded");
        } else if (bounds.getMin() == null) {
            return new TranslationTextComponent("jea.range.lower", factory.apply(bounds.getMax()));
        } else if (bounds.getMax() == null) {
            return new TranslationTextComponent("jea.range.greater", factory.apply(bounds.getMin()));
        } else if (bounds.getMin().equals(bounds.getMax())) {
            return factory.apply(bounds.getMin());
        } else {
            return new TranslationTextComponent("jea.range.bounded", factory.apply(bounds.getMin()), factory.apply(bounds.getMax()));
        }
    }
    
    public static <T extends Number> Optional<T> getExampleValue(MinMaxBounds<T> bounds) {
        if (bounds.getMin() != null) {
            return Optional.of(bounds.getMin());
        } else if (bounds.getMax() != null) {
            return Optional.of(bounds.getMax());
        } else {
            return Optional.empty();
        }
    }
    
    public static String rl(ResourceLocation location) {
        if (location.getNamespace().equals("minecraft")) {
            return location.getPath();
        } else {
            return location.toString();
        }
    }
}
