package de.melanx.jea.util;

import de.melanx.jea.ingredient.AdvancementInfo;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

public class ClientAdvancementInfo {
    private static final Map<ResourceLocation, Triple<ItemStack, ITextComponent, ItemPredicate>> CACHE = new HashMap<>();

    public static ItemStack getDisplay(ResourceLocation id) {
        if (CACHE.containsKey(id)) {
            return CACHE.get(id).getLeft();
        } else {
            return new ItemStack(Items.BARRIER);
        }
    }

    public static ITextComponent getTranslation(ResourceLocation id) {
        if (CACHE.containsKey(id)) {
            return CACHE.get(id).getMiddle();
        } else {
            return new TranslationTextComponent("jea.advancement.invalid");
        }
    }

    public static Optional<ItemStack> getTooltipItem(ResourceLocation id) {
        if (CACHE.containsKey(id)) {
            return Optional.of(CACHE.get(id).getLeft());
        } else {
            return Optional.empty();
        }
    }

    public static Collection<AdvancementInfo> getAdvancements() {
        Set<AdvancementInfo> advancements = new HashSet<>();
        CACHE.forEach((id, data) -> {
            advancements.add(new AdvancementInfo(id, data.getLeft(), data.getMiddle(), data.getRight()));
        });
        return advancements;
    }

    public static void updateAdvancementInfo(ResourceLocation id, ItemStack display, ITextComponent translation, ItemPredicate tooltipItem) {
        CACHE.put(id, Triple.of(display, translation, tooltipItem));
    }
}
