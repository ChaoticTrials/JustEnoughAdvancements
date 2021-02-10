package de.melanx.jea.util;

import de.melanx.jea.ingredient.AdvancementInfo;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.*;

public class ClientAdvancementInfo {
    private static final Map<ResourceLocation, ClientAdvancementInfo> CACHE = new HashMap<>();
    private final ItemStack display;
    private final ITextComponent title;
    private final ITextComponent desc;
    private final ItemPredicate tooltip;

    public ClientAdvancementInfo(ItemStack display, ITextComponent title, ITextComponent desc, ItemPredicate tooltip) {
        this.display = display;
        this.title = title;
        this.desc = desc;
        this.tooltip = tooltip;
    }

    public static ItemStack getDisplay(ResourceLocation id) {
        if (CACHE.containsKey(id)) {
            return CACHE.get(id).display;
        } else {
            return new ItemStack(Items.BARRIER);
        }
    }

    public static ITextComponent getTitle(ResourceLocation id) {
        if (CACHE.containsKey(id)) {
            return CACHE.get(id).title;
        } else {
            return new TranslationTextComponent("jea.advancement.invalid");
        }
    }

    public static ITextComponent getDescription(ResourceLocation id) {
        if (CACHE.containsKey(id)) {
            return CACHE.get(id).desc;
        } else {
            return new TranslationTextComponent("jea.advancement.invalid");
        }
    }

    public static Optional<ItemStack> getTooltipItem(ResourceLocation id) {
        if (CACHE.containsKey(id)) {
            return Optional.of(CACHE.get(id).display);
        } else {
            return Optional.empty();
        }
    }

    public static Collection<AdvancementInfo> getAdvancements() {
        Set<AdvancementInfo> advancements = new HashSet<>();
        CACHE.forEach((id, info) -> {
            advancements.add(new AdvancementInfo(id, info.display, info.title, info.desc, info.tooltip));
        });
        return advancements;
    }

    public static void updateAdvancementInfo(ResourceLocation id, ItemStack display, ITextComponent title, ITextComponent desc, ItemPredicate tooltipItem) {
        CACHE.put(id, new ClientAdvancementInfo(display, title, desc, tooltipItem));
    }
}
