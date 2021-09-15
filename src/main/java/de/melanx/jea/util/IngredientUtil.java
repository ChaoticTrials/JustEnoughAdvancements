package de.melanx.jea.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.*;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IngredientUtil {
    
    public static List<ItemStack> ingredients(@Nullable ItemLike input, ItemLike... defaults) {
        if (input == null) {
            return Arrays.stream(defaults).map(item -> {
                ItemStack stack = new ItemStack(item.asItem());
                stack.setHoverName(new TranslatableComponent("jea.item.tooltip.any_item").withStyle(ChatFormatting.GOLD));
                return stack;
            }).collect(Collectors.toList());
        } else {
            return List.of(new ItemStack(input.asItem()));
        }
    }
    
    public static List<ItemStack> fromItemPredicate(ItemPredicate predicate, ItemLike... defaults) {
        return fromItemPredicate(predicate, false, defaults);
    }
    
    public static List<ItemStack> fromItemPredicate(ItemPredicate predicate, boolean noAny, ItemLike... defaults) {
        return fromItemPredicate(predicate, null, noAny, defaults);
    }
    
    public static List<ItemStack> fromItemPredicate(ItemPredicate predicate, @Nullable MutableComponent additionalLine, boolean noAny, ItemLike... defaults) {
        List<Item> items;
        boolean anyItemTitle = false;
        if (predicate.items != null && !predicate.items.isEmpty()) {
            items = List.copyOf(predicate.items);
        } else if (predicate.tag != null && !predicate.tag.getValues().isEmpty()) {
            items = predicate.tag.getValues();
        } else {
            anyItemTitle = !noAny;
            items = Arrays.stream(defaults).map(p -> p == null ? null : p.asItem()).collect(Collectors.toList());
        }
        List<MutableComponent> tooltip = new ArrayList<>();
        if (!predicate.count.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.amount", text(predicate.count)));
        }
        if (!predicate.durability.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.durability", text(predicate.durability)));
        }
        for (EnchantmentPredicate ench : predicate.enchantments) {
            tooltip.add(new TranslatableComponent(
                    "jea.item.tooltip.enchantment",
                    ench.enchantment.getFullname(1),
                    text(ench.level, level -> new TranslatableComponent("enchantment.level." + level))
            ));
        }
        for (EnchantmentPredicate ench : predicate.storedEnchantments) {
            tooltip.add(new TranslatableComponent(
                    "jea.item.tooltip.book_enchantment",
                    ench.enchantment.getFullname(1),
                    text(ench.level, level -> new TranslatableComponent("enchantment.level." + level))
            ));
        }
        if (predicate.potion != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.potion", new TranslatableComponent(predicate.potion.getName("item." + Objects.requireNonNull(predicate.potion.getRegistryName()).getNamespace() + ".potion.effect."))));
        }
        if (additionalLine != null) {
            tooltip.add(additionalLine);
        }
        ListTag tooltipNBT = new ListTag();
        for (MutableComponent tc : tooltip) {
            tooltipNBT.add(StringTag.valueOf(Component.Serializer.toJson(tc.withStyle(ChatFormatting.RESET).withStyle(ChatFormatting.AQUA))));
        }
        
        List<ItemStack> stacks = new ArrayList<>();
        for (Item item : items) {
            ItemStack stack = item == null ? ItemStack.EMPTY.copy() : new ItemStack(item);
            if (anyItemTitle) {
                stack.setHoverName(new TranslatableComponent("jea.item.tooltip.any_item").withStyle(ChatFormatting.GOLD));
            }
            getExampleValue(predicate.count).ifPresent(stack::setCount);
            if (stack.isDamageableItem()) {
                getExampleValue(predicate.durability).ifPresent(stack::setDamageValue);
            }
            if (predicate.nbt.tag != null) {
                CompoundTag nbt = stack.getOrCreateTag();
                nbt.merge(predicate.nbt.tag);
                stack.setTag(nbt);
            }
            if ((stack.getItem() == Items.ENCHANTED_BOOK ? predicate.storedEnchantments : predicate.enchantments).length > 0) {
                CompoundTag nbt = stack.getOrCreateTag();
                ListTag list = new ListTag();
                list.add(new CompoundTag());
                nbt.put("Enchantments", list);
            }
            CompoundTag display = stack.getOrCreateTagElement("display");
            display.put("Lore", tooltipNBT);
            stacks.add(stack);
        }
        return stacks;
    }
    
    public static MutableComponent text(MinMaxBounds<?> bounds) {
        if (bounds.isAny() || (bounds.getMin() == null && bounds.getMax() == null)) {
            return new TranslatableComponent("jea.range.unbounded");
        } else if (bounds.getMin() == null) {
            return new TranslatableComponent("jea.range.lower", numberToString(Objects.requireNonNull(bounds.getMax())));
        } else if (bounds.getMax() == null) {
            return new TranslatableComponent("jea.range.greater", numberToString(Objects.requireNonNull(bounds.getMin())));
        } else if (bounds.getMin().equals(bounds.getMax())) {
            return new TextComponent(numberToString(bounds.getMin()));
        } else {
            return new TranslatableComponent("jea.range.bounded", numberToString(Objects.requireNonNull(bounds.getMin())), numberToString(Objects.requireNonNull(bounds.getMax())));
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
    
    public static <T extends Number> MutableComponent text(MinMaxBounds<T> bounds, Function<T, MutableComponent> factory) {
        if (bounds.isAny() || (bounds.getMin() == null && bounds.getMax() == null)) {
            return new TranslatableComponent("jea.range.unbounded");
        } else if (bounds.getMin() == null) {
            return new TranslatableComponent("jea.range.lower", factory.apply(bounds.getMax()));
        } else if (bounds.getMax() == null) {
            return new TranslatableComponent("jea.range.greater", factory.apply(bounds.getMin()));
        } else if (bounds.getMin().equals(bounds.getMax())) {
            return factory.apply(bounds.getMin());
        } else {
            return new TranslatableComponent("jea.range.bounded", factory.apply(bounds.getMin()), factory.apply(bounds.getMax()));
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
    
    public static String dim(ResourceLocation location) {
        String key = "dimension." + location.getNamespace() + "." + location.getPath().replace('/', '.') + ".name";
        if (I18n.exists(key)) {
            return I18n.get(key);
        } else {
            return rl(location);
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
            return Blocks.AIR.defaultBlockState();
        } else {
            BlockState state = block.defaultBlockState();
            if (predicate != null) {
                for (StatePropertiesPredicate.PropertyMatcher matcher : predicate.properties) {
                    Property<?> property = state.getProperties().stream().filter(p -> p.getName().equals(matcher.name)).findFirst().orElse(null);
                    if (property != null) {
                        String valueToMatch = null;
                        if (matcher instanceof StatePropertiesPredicate.ExactPropertyMatcher exactMatcher) {
                            valueToMatch = exactMatcher.value;
                        } else if (matcher instanceof StatePropertiesPredicate.RangedPropertyMatcher rangedMatcher) {
                            if (rangedMatcher.minValue != null) {
                                valueToMatch = rangedMatcher.minValue;
                            } else if (rangedMatcher.maxValue != null) {
                                valueToMatch = rangedMatcher.maxValue;
                            }
                        }
                        if (valueToMatch != null) {
                            Object value = property.getValue(valueToMatch).orElse(null);
                            if (value != null) {
                                //noinspection unchecked
                                state = state.setValue((Property<T>) property, (T) value);
                            }
                        }
                    }
                }
            }
            return state;
        }
    }
    
    public static List<ItemStack> getBlockIngredients(BlockPredicate predicate) {
        if (predicate.blocks != null && !predicate.blocks.isEmpty()) {
            return predicate.blocks.stream().map(ItemStack::new).toList();
        } else if (predicate.tag != null && !predicate.tag.getValues().isEmpty()) {
            //noinspection UnstableApiUsage
            return predicate.tag.getValues().stream().map(Block::asItem).map(ItemStack::new).collect(ImmutableList.toImmutableList());
        } else {
            ItemStack stack = new ItemStack(Blocks.COBBLESTONE);
            stack.setHoverName(new TranslatableComponent("jea.item.tooltip.any_block").withStyle(ChatFormatting.GOLD));
            return List.of(stack);
        }
    }
}
