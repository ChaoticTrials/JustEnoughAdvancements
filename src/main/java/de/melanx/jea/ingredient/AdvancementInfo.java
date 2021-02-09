package de.melanx.jea.ingredient;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AdvancementInfo {
    public final ResourceLocation id;
    public final ItemStack display;
    public final ITextComponent translation;
    public final ItemPredicate tooltip;

    public AdvancementInfo(ResourceLocation id, ItemStack display, ITextComponent translation, ItemPredicate tooltip) {
        this.id = id;
        this.display = display;
        this.translation = translation;
        this.tooltip = tooltip;
    }
}
