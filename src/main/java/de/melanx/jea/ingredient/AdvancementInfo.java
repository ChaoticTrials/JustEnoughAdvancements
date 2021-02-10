package de.melanx.jea.ingredient;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Predicate;

public class AdvancementInfo {
    public final ResourceLocation id;
    public final ItemStack display;
    public final ITextComponent title;
    public final ITextComponent desc;
    public final ItemPredicate tooltip;

    public AdvancementInfo(ResourceLocation id, ItemStack display, ITextComponent title, ITextComponent desc, ItemPredicate tooltip) {
        this.id = id;
        this.display = display;
        this.title = title;
        this.desc = desc;
        this.tooltip = tooltip;
    }
}
