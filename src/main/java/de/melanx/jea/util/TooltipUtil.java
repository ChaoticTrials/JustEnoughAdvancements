package de.melanx.jea.util;

import net.minecraft.Util;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TooltipUtil {
    
    public static void addDistanceValuesPlayerRelative(List<? super MutableComponent> tooltip, DistancePredicate predicate) {
        if (!predicate.absolute.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance.absolute", IngredientUtil.text(predicate.absolute)));
        }
        if (!predicate.horizontal.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance.horizontal", IngredientUtil.text(predicate.horizontal)));
        }
        if (!predicate.y.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance.y", IngredientUtil.text(predicate.y)));
        }
        if (!predicate.x.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance.x", IngredientUtil.text(predicate.x)));
        }
        if (!predicate.z.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance.z", IngredientUtil.text(predicate.z)));
        }
    }
    
    public static void addDistanceValues(List<? super MutableComponent> tooltip, DistancePredicate predicate) {
        if (!predicate.absolute.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance2.absolute", IngredientUtil.text(predicate.absolute)));
        }
        if (!predicate.horizontal.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance2.horizontal", IngredientUtil.text(predicate.horizontal)));
        }
        if (!predicate.y.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance2.y", IngredientUtil.text(predicate.y)));
        }
        if (!predicate.x.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance2.x", IngredientUtil.text(predicate.x)));
        }
        if (!predicate.z.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.distance2.z", IngredientUtil.text(predicate.z)));
        }
    }
    
    public static void addLocationValues(List<? super MutableComponent> tooltip, LocationPredicate predicate) {
        if (predicate.biome != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.biome", new TranslatableComponent(Util.makeDescriptionId("biome", predicate.biome.location()))));
        }
        if (predicate.feature != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.feature", predicate.feature.getFeatureName()));
        }
        if (predicate.dimension != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.dimension", IngredientUtil.dim(predicate.dimension.location())));
        }
        if (predicate.smokey != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.smokey", yesNo(predicate.smokey)));
        }
        if (!predicate.light.composite.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.light", IngredientUtil.text(predicate.light.composite)));
        }
        if (predicate.block.blocks != null && !predicate.block.blocks.isEmpty()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.block", listEntries(predicate.block.blocks, Block::getName)));
        } else if (predicate.block.tag instanceof Tag.Named<?> namedTag) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.block_tag", IngredientUtil.rl(namedTag.getName())));
        }
        if (predicate.fluid.fluid != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.fluid", new TranslatableComponent(predicate.fluid.fluid.getAttributes().getTranslationKey())));
        } else if (predicate.fluid.tag instanceof Tag.Named<?> namedTag) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.fluid_tag", IngredientUtil.rl(namedTag.getName())));
        }
        if (!predicate.y.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.y", IngredientUtil.text(predicate.y)));
        }
        if (!predicate.x.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.x", IngredientUtil.text(predicate.x)));
        }
        if (!predicate.z.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.z", IngredientUtil.text(predicate.z)));
        }
    }
    
    public static void addLocationValuesNoIn(List<? super MutableComponent> tooltip, LocationPredicate predicate) {
        if (predicate.biome != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location2.biome", new TranslatableComponent(Util.makeDescriptionId("biome", predicate.biome.location()))));
        }
        if (predicate.feature != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location2.feature", predicate.feature.getFeatureName()));
        }
        if (predicate.dimension != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location2.dimension", IngredientUtil.dim(predicate.dimension.location())));
        }
        if (predicate.smokey != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.smokey", yesNo(predicate.smokey)));
        }
        if (!predicate.light.composite.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.light", IngredientUtil.text(predicate.light.composite)));
        }
        if (predicate.block.blocks != null && !predicate.block.blocks.isEmpty()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location2.block", listEntries(predicate.block.blocks, Block::getName)));
        } else if (predicate.block.tag instanceof Tag.Named<?> namedTag) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location2.block_tag", IngredientUtil.rl(namedTag.getName())));
        }
        if (predicate.fluid.fluid != null) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location2.fluid", new TranslatableComponent(predicate.fluid.fluid.getAttributes().getTranslationKey())));
        } else if (predicate.fluid.tag instanceof Tag.Named<?> namedTag) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location2.fluid_tag", IngredientUtil.rl(namedTag.getName())));
        }
        if (!predicate.y.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.y", IngredientUtil.text(predicate.y)));
        }
        if (!predicate.x.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.x", IngredientUtil.text(predicate.x)));
        }
        if (!predicate.z.isAny()) {
            tooltip.add(new TranslatableComponent("jea.item.tooltip.location.z", IngredientUtil.text(predicate.z)));
        }
    }
    
    public static MutableComponent yesNo(boolean value) {
        return new TranslatableComponent(value ? "jea.item.tooltip.location.yes" : "jea.item.tooltip.location.no");
    }

    public static void addEffectValues(List<? super MutableComponent> tooltip, MobEffectsPredicate predicate) {
        for (Map.Entry<MobEffect, MobEffectsPredicate.MobEffectInstancePredicate> entry : predicate.effects.entrySet()) {
            MobEffect effect = entry.getKey();
            MobEffectsPredicate.MobEffectInstancePredicate instance = entry.getValue();
            tooltip.add(new TranslatableComponent("jea.item.tooltip.mob_effect.name", effect.getDisplayName()));
            if (!instance.amplifier.isAny()) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.mob_effect.amplifier", IngredientUtil.text(instance.amplifier, value -> value == 0 ? new TextComponent("I") : new TranslatableComponent("potion.potency." + value))));
            }
            if (!instance.duration.isAny()) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.mob_effect.duration", IngredientUtil.text(instance.duration, value -> new TextComponent(formatTimeTicks(value)))));
            }
            if (instance.ambient != null) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.mob_effect.ambient", yesNo(instance.ambient)));
            }
            if (instance.visible != null) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.mob_effect.visible", yesNo(instance.visible)));
            }
        }
    }
    
    public static String formatTimeTicks(int ticks) {
        int timeSeconds = Math.round(ticks / 20f);
        int seconds = timeSeconds % 60;
        String secondsString = Integer.toString(seconds);
        if (secondsString.length() < 2) {
            secondsString = "0" + secondsString;
        }
        int minutes = timeSeconds / 60;
        return minutes + ":" + secondsString;
    }
    
    public static String formatChance(double chance) {
        return BigDecimal.valueOf(chance).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + "%";
    }
    
    public static <T extends IForgeRegistryEntry<?>> Component listEntries(Collection<T> elements, Function<T, Component> textFunc) {
        if (elements.isEmpty()) {
            return new TextComponent("");
        } else if (elements.size() == 1) {
            return textFunc.apply(elements.iterator().next());
        } else {
            List<Component> components = elements.stream()
                    .filter(e -> e != null && e.getRegistryName() != null)
                    .sorted(Comparator.comparing(IForgeRegistryEntry::getRegistryName))
                    .map(textFunc).toList();
            MutableComponent cmp = new TextComponent("");
            for (int i = 0; i < components.size(); i++) {
                cmp = cmp.append(components.get(i));
                if (i < components.size() - 1) {
                    cmp = cmp.append(new TextComponent(", "));
                }
            }
            return cmp;
        }
    }
}
