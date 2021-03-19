package de.melanx.jea.util;

import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ITag;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class TooltipUtil {
    
    public static void addDistanceValuesPlayerRelative(List<IFormattableTextComponent> tooltip, DistancePredicate predicate) {
        if (!predicate.absolute.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance.absolute", IngredientUtil.text(predicate.absolute)));
        }
        if (!predicate.horizontal.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance.horizontal", IngredientUtil.text(predicate.horizontal)));
        }
        if (!predicate.y.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance.y", IngredientUtil.text(predicate.y)));
        }
        if (!predicate.x.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance.x", IngredientUtil.text(predicate.x)));
        }
        if (!predicate.z.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance.z", IngredientUtil.text(predicate.z)));
        }
    }
    
    public static void addDistanceValues(List<IFormattableTextComponent> tooltip, DistancePredicate predicate) {
        if (!predicate.absolute.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance2.absolute", IngredientUtil.text(predicate.absolute)));
        }
        if (!predicate.horizontal.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance2.horizontal", IngredientUtil.text(predicate.horizontal)));
        }
        if (!predicate.y.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance2.y", IngredientUtil.text(predicate.y)));
        }
        if (!predicate.x.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance2.x", IngredientUtil.text(predicate.x)));
        }
        if (!predicate.z.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.distance2.z", IngredientUtil.text(predicate.z)));
        }
    }
    
    public static void addLocationValues(List<IFormattableTextComponent> tooltip, LocationPredicate predicate) {
        if (predicate.biome != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.biome", new TranslationTextComponent(Util.makeTranslationKey("biome", predicate.biome.getLocation()))));
        }
        if (predicate.feature != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.feature", predicate.feature.getStructureName()));
        }
        if (predicate.dimension != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.dimension", IngredientUtil.rl(predicate.dimension.getLocation())));
        }
        if (predicate.smokey != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.smokey", yesNo(predicate.smokey)));
        }
        if (!predicate.light.bounds.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.light", IngredientUtil.text(predicate.light.bounds)));
        }
        if (predicate.block.block != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.block", new TranslationTextComponent(predicate.block.block.getTranslationKey())));
        } else if (predicate.block.tag instanceof ITag.INamedTag<?>) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.block_tag", IngredientUtil.rl(((ITag.INamedTag<Block>) predicate.block.tag).getName())));
        }
        if (predicate.fluid.fluid != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.fluid", new TranslationTextComponent(predicate.fluid.fluid.getAttributes().getTranslationKey())));
        } else if (predicate.fluid.fluidTag instanceof ITag.INamedTag<?>) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.fluid_tag", IngredientUtil.rl(((ITag.INamedTag<Fluid>) predicate.fluid.fluidTag).getName())));
        }
        if (!predicate.y.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.y", IngredientUtil.text(predicate.y)));
        }
        if (!predicate.x.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.x", IngredientUtil.text(predicate.x)));
        }
        if (!predicate.z.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.z", IngredientUtil.text(predicate.z)));
        }
    }
    
    public static void addLocationValuesNoIn(List<IFormattableTextComponent> tooltip, LocationPredicate predicate) {
        if (predicate.biome != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location2.biome", new TranslationTextComponent(Util.makeTranslationKey("biome", predicate.biome.getLocation()))));
        }
        if (predicate.feature != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location2.feature", predicate.feature.getStructureName()));
        }
        if (predicate.dimension != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location2.dimension", IngredientUtil.rl(predicate.dimension.getLocation())));
        }
        if (predicate.smokey != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.smokey", yesNo(predicate.smokey)));
        }
        if (!predicate.light.bounds.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.light", IngredientUtil.text(predicate.light.bounds)));
        }
        if (predicate.block.block != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location2.block", new TranslationTextComponent(predicate.block.block.getTranslationKey())));
        } else if (predicate.block.tag instanceof ITag.INamedTag<?>) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location2.block_tag", IngredientUtil.rl(((ITag.INamedTag<Block>) predicate.block.tag).getName())));
        }
        if (predicate.fluid.fluid != null) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location2.fluid", new TranslationTextComponent(predicate.fluid.fluid.getAttributes().getTranslationKey())));
        } else if (predicate.fluid.fluidTag instanceof ITag.INamedTag<?>) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location2.fluid_tag", IngredientUtil.rl(((ITag.INamedTag<Fluid>) predicate.fluid.fluidTag).getName())));
        }
        if (!predicate.y.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.y", IngredientUtil.text(predicate.y)));
        }
        if (!predicate.x.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.x", IngredientUtil.text(predicate.x)));
        }
        if (!predicate.z.isUnbounded()) {
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.z", IngredientUtil.text(predicate.z)));
        }
    }
    
    public static IFormattableTextComponent yesNo(boolean value) {
        return new TranslationTextComponent(value ? "jea.item.tooltip.location.yes" : "jea.item.tooltip.location.no");
    }

    public static void addEffectValues(List<IFormattableTextComponent> tooltip, MobEffectsPredicate predicate) {
        for (Map.Entry<Effect, MobEffectsPredicate.InstancePredicate> entry : predicate.effects.entrySet()) {
            Effect effect = entry.getKey();
            MobEffectsPredicate.InstancePredicate instance = entry.getValue();
            tooltip.add(new TranslationTextComponent("jea.item.tooltip.mob_effect.name", effect.getDisplayName()));
            if (!instance.amplifier.isUnbounded()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.mob_effect.amplifier", IngredientUtil.text(instance.amplifier, value -> value == 0 ? new StringTextComponent("I") : new TranslationTextComponent("potion.potency." + value))));
            }
            if (!instance.duration.isUnbounded()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.mob_effect.duration", IngredientUtil.text(instance.duration, value -> new StringTextComponent(formatTimeTicks(value)))));
            }
            if (instance.ambient != null) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.mob_effect.ambient", yesNo(instance.ambient)));
            }
            if (instance.visible != null) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.mob_effect.visible", yesNo(instance.visible)));
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
}
