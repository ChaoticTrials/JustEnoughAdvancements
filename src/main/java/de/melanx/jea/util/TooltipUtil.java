package de.melanx.jea.util;

import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.block.Block;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.fluid.Fluid;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ITag;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Map;

public class TooltipUtil {
    
    public static void addDistanceValues(List<IFormattableTextComponent> tooltip, DistancePredicate predicate) {
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
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.mob_effect.duration", IngredientUtil.text(instance.duration, value -> new StringTextComponent(Math.round(value / 20f) + "s"))));
            }
            if (instance.ambient != null) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.mob_effect.ambient", yesNo(instance.ambient)));
            }
            if (instance.visible != null) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.mob_effect.visible", yesNo(instance.visible)));
            }
        }
    }
}
