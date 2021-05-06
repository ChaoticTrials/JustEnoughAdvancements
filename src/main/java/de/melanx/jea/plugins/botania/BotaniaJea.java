package de.melanx.jea.plugins.botania;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.item.ItemStack;

public class BotaniaJea {

    public static final IIngredientRenderer<ItemStack> TINY_POTATO = new TinyPotatoRender();
    public static final IIngredientRenderer<ItemStack> LUMINIZER = new LuminizerRender();
}
