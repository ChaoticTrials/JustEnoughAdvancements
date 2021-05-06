package de.melanx.jea.plugins.botania;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public class BotaniaItems {

    private static final ResourceLocation DREAMWOOD_WAND = new ResourceLocation("mythicbotany", "dreamwood_twig_wand");

    private static final ResourceLocation SHATTERER_ALFSTEEL = new ResourceLocation("mythicbotany", "alfsteel_pick");
    private static final ResourceLocation SHATTERER_AIOT = new ResourceLocation("aiotbotania", "terra_aiot");
    private static final ResourceLocation SHATTERER_AIOT_ALFSTEEL = new ResourceLocation("aiotbotania", "alfsteel_aiot");

    private static final int[] SHATTERER_MANA = new int[]{
            9999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE - 1
    };

    public static List<ItemStack> wands() {
        ImmutableList.Builder<ItemStack> list = ImmutableList.builder();
        ItemStack livingwood = new ItemStack(ModItems.twigWand);
        livingwood.getOrCreateTag().putInt("color1", 5);
        livingwood.getOrCreateTag().putInt("color2", 6);
        list.add(livingwood);
        if (ModList.get().isLoaded("mythicbotany") && ForgeRegistries.ITEMS.containsKey(DREAMWOOD_WAND)) {
            ItemStack dreamwood = new ItemStack(ForgeRegistries.ITEMS.getValue(DREAMWOOD_WAND));
            dreamwood.getOrCreateTag().putInt("color1", 3);
            dreamwood.getOrCreateTag().putInt("color2", 4);
            list.add(dreamwood);
        }
        return list.build();
    }

    public static List<ItemStack> shatterers(int rank, boolean enabled, boolean tipped) {
        List<Item> items = new ArrayList<>();
        items.add(ModItems.terraPick);
        if (ForgeRegistries.ITEMS.containsKey(SHATTERER_ALFSTEEL)) {
            items.add(ForgeRegistries.ITEMS.getValue(SHATTERER_ALFSTEEL));
        }
        if (ForgeRegistries.ITEMS.containsKey(SHATTERER_AIOT)) {
            items.add(ForgeRegistries.ITEMS.getValue(SHATTERER_AIOT));
        }
        if (ModList.get().isLoaded("mythicbotany") && ForgeRegistries.ITEMS.containsKey(SHATTERER_AIOT_ALFSTEEL)) {
            items.add(ForgeRegistries.ITEMS.getValue(SHATTERER_AIOT_ALFSTEEL));
        }
        List<ItemStack> stacks = new ArrayList<>();
        for (Item item : items) {
            ItemStack stack = new ItemStack(item);
            stack.getOrCreateTag().putInt("mana", SHATTERER_MANA[rank]);
            stack.getOrCreateTag().putBoolean("enabled", enabled);
            stack.getOrCreateTag().putBoolean("tipped", tipped);
            ListNBT tooltipNbt = new ListNBT();
            tooltipNbt.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(new TranslationTextComponent("jea.item.tooltip.botania.terra_rank").mergeStyle(TextFormatting.AQUA))));
            stack.getOrCreateChildTag("display").put("Lore", tooltipNbt);
            stacks.add(stack);
        }
        return stacks;
    }
}
