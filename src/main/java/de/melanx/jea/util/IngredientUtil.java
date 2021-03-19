package de.melanx.jea.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.criterion.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.state.Property;
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
            return Arrays.stream(defaults).map(item -> {
                ItemStack stack = new ItemStack(item.asItem());
                stack.setDisplayName(new TranslationTextComponent("jea.item.tooltip.any_item").mergeStyle(TextFormatting.GOLD));
                return stack;
            }).collect(Collectors.toList());
        } else {
            return ImmutableList.of(new ItemStack(input.asItem()));
        }
    }
    
    public static List<ItemStack> fromItemPredicate(ItemPredicate predicate, IItemProvider... defaults) {
        return fromItemPredicate(predicate, false, defaults);
    }
    
    public static List<ItemStack> fromItemPredicate(ItemPredicate predicate, boolean noAny, IItemProvider... defaults) {
        List<Item> items;
        boolean anyItemTitle = false;
        if (predicate.item != null) {
            items = ImmutableList.of(predicate.item);
        } else if (predicate.tag != null && !predicate.tag.getAllElements().isEmpty()) {
            items = predicate.tag.getAllElements();
        } else {
            anyItemTitle = !noAny;
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
            return new TranslationTextComponent("jea.range.lower", numberToString(Objects.requireNonNull(bounds.getMax())));
        } else if (bounds.getMax() == null) {
            return new TranslationTextComponent("jea.range.greater", numberToString(Objects.requireNonNull(bounds.getMin())));
        } else if (bounds.getMin().equals(bounds.getMax())) {
            return new StringTextComponent(numberToString(bounds.getMin()));
        } else {
            return new TranslationTextComponent("jea.range.bounded", numberToString(Objects.requireNonNull(bounds.getMin())), numberToString(Objects.requireNonNull(bounds.getMax())));
        }
    }
    
    private static String numberToString(Number number) {
        String string = number.toString();
        if (string.endsWith(".0")) {
            return string.substring(0, string.length() - 2);
        } else {
            return string;
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
    
    // Sometimes minecraft uses a ResourceLocation with a file name as path.
    // We cut that off here
    public static String rlFile(ResourceLocation location) {
        String pathStr = location.getPath();
        if (pathStr.contains("/")) {
            pathStr = pathStr.substring(pathStr.lastIndexOf('/') + 1);
        }
        if (pathStr.contains(".") && pathStr.lastIndexOf(".") < pathStr.length()) {
            pathStr = pathStr.substring(0, pathStr.lastIndexOf('.'));
        }
        if (location.getNamespace().equals("minecraft")) {
            return pathStr;
        } else {
            return location.getNamespace() + ":" + pathStr;
        }
    }
    
    // T exists because Comparable must hav itself as a type parameter so a Property can not be cast to Property<Comparable<?>>
    public static <T extends Comparable<T>> BlockState getState(@Nullable Block block, @Nullable StatePropertiesPredicate predicate) {
        if (block == null) {
            return Blocks.AIR.getDefaultState();
        } else {
            BlockState state = block.getDefaultState();
            if (predicate != null) {
                for (StatePropertiesPredicate.Matcher matcher : predicate.matchers) {
                    Property<?> property = state.getProperties().stream().filter(p -> p.getName().equals(matcher.propertyName)).findFirst().orElse(null);
                    if (property != null) {
                        String valueToMatch = null;
                        if (matcher instanceof StatePropertiesPredicate.ExactMatcher) {
                            valueToMatch = ((StatePropertiesPredicate.ExactMatcher) matcher).valueToMatch;
                        } else if (matcher instanceof StatePropertiesPredicate.RangedMacher) {
                            if (((StatePropertiesPredicate.RangedMacher) matcher).minimum != null) {
                                valueToMatch = ((StatePropertiesPredicate.RangedMacher) matcher).minimum;
                            } else if (((StatePropertiesPredicate.RangedMacher) matcher).maximum != null) {
                                valueToMatch = ((StatePropertiesPredicate.RangedMacher) matcher).maximum;
                            }
                        }
                        if (valueToMatch != null) {
                            Object value = property.parseValue(valueToMatch).orElse(null);
                            if (value != null) {
                                //noinspection unchecked
                                state = state.with((Property<T>) property, (T) value);
                            }
                        }
                    }
                }
            }
            return state;
        }
    }
    
    public static List<ItemStack> getBlockIngredients(BlockPredicate predicate) {
        if (predicate.block != null) {
            return ImmutableList.of(new ItemStack(predicate.block.asItem()));
        } else if (predicate.tag != null && !predicate.tag.getAllElements().isEmpty()) {
            //noinspection UnstableApiUsage
            return predicate.tag.getAllElements().stream().map(Block::asItem).map(ItemStack::new).collect(ImmutableList.toImmutableList());
        } else {
            ItemStack stack = new ItemStack(Blocks.COBBLESTONE);
            stack.setDisplayName(new TranslationTextComponent("jea.item.tooltip.any_block").mergeStyle(TextFormatting.GOLD));
            return ImmutableList.of(stack);
        }
    }
}
