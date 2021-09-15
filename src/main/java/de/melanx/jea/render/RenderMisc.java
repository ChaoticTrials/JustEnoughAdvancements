package de.melanx.jea.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.TooltipUtil;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.*;

public class RenderMisc {
    
    private static final Map<MobEffect, ResourceLocation> potionTextures = new HashMap<>();
    private static final List<Block> smokeSources = List.of(Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
    
    public static void renderFood(PoseStack poseStack, FoodProperties food, int minIcons) {
        int value = Mth.clamp(food.getNutrition(), 0, 20);
        float saturation = Mth.clamp(food.getSaturationModifier() * food.getNutrition(), 0, value);
        int icons = Mth.clamp((int) Math.ceil(value / 2d), minIcons, 10);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        poseStack.pushPose();
        for (int i = 0; i < icons; i++) {
            JustEnoughAdvancementsJEIPlugin.getHungerEmpty().draw(poseStack, 9 * i, 0);
        }
        poseStack.translate(0, 0, 10);
        for (int i = 0; i < (value / 2); i++) {
            JustEnoughAdvancementsJEIPlugin.getHungerFull().draw(poseStack, 9 * (icons - i - 1), 0);
        }
        if (value % 2 != 0) {
            JustEnoughAdvancementsJEIPlugin.getHungerHalf().draw(poseStack, 9 * (icons - (value / 2) - 1), 0);
        }
        poseStack.translate(0, 0, 10);
        for (int i = 0; i < (int) Math.floor(saturation / 2); i++) {
            JustEnoughAdvancementsJEIPlugin.getHungerSaturation().draw(poseStack, 9 * (icons - i - 1), 0);
        }
        float left = (saturation / 2) - (float) Math.floor(saturation / 2);
        int cutPixels = 9 - Mth.clamp(Math.round(9 * left), 0, 9);
        if (cutPixels < 9) {
            JustEnoughAdvancementsJEIPlugin.getHungerSaturation().draw(poseStack, 9 * (icons - (int) Math.floor(saturation / 2) - 1), 0, 0, 0, cutPixels, 0);
        }
        poseStack.popPose();
        RenderSystem.disableBlend();
    }
    
    public static void renderMobEffect(PoseStack poseStack, Minecraft mc, MobEffect effect) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        poseStack.pushPose();
        JustEnoughAdvancementsJEIPlugin.getEffectSlot().draw(poseStack, 0, 0);
        poseStack.translate(2, 2, 10);
        poseStack.scale(16/18f, 16/18f, 1);
        ResourceLocation potionTexture;
        if (potionTextures.containsKey(effect)) {
            potionTexture = potionTextures.get(effect);
        } else {
            potionTexture = new ResourceLocation(Objects.requireNonNull(effect.getRegistryName()).getNamespace(), "textures/mob_effect/" + Objects.requireNonNull(effect.getRegistryName()).getPath() + ".png");
            potionTextures.put(effect, potionTexture);
        }
        RenderSystem.setShaderTexture(0, potionTexture);
        GuiComponent.blit(poseStack, 0, 0, 0, 0, 18, 18, 18, 18);
        poseStack.popPose();
        RenderSystem.disableBlend();
    }
    
    public static void addMobEffectTooltip(List<Component> tooltip, MobEffectInstance effect, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Float> chance, int x, int y, double mouseX, double mouseY) {
        addMobEffectTooltip(tooltip, effect, chance, x, y, 20, mouseX, mouseY);
    }
    
    public static void addMobEffectTooltip(List<Component> tooltip, MobEffectInstance effect, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Float> chance, int x, int y, int size, double mouseX, double mouseY) {
        if (mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size) {
            tooltip.add(effect.getEffect().getDisplayName().copy().append(new TextComponent(" ")).append(new TranslatableComponent("potion.potency." + effect.getAmplifier())).withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
            chance.ifPresent(c -> tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.chance", TooltipUtil.formatChance(c))));
            if (!effect.getEffect().isInstantenous() && effect.getDuration() != 0) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.duration", TooltipUtil.formatTimeTicks(effect.getDuration())));
            }
            if (effect.isAmbient()) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.ambient"));
            }
            if (!effect.isVisible()) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.invisible"));
            }
        }
    }
    
    public static void addMobEffectTooltip(List<Component> tooltip, MobEffect effect, MobEffectsPredicate.MobEffectInstancePredicate instance, int x, int y, double mouseX, double mouseY) {
        addMobEffectTooltip(tooltip, effect, instance, x, y, 20, mouseX, mouseY);
    }
    
    public static void addMobEffectTooltip(List<Component> tooltip, MobEffect effect, MobEffectsPredicate.MobEffectInstancePredicate instance, int x, int y, int size, double mouseX, double mouseY) {
        if (mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size) {
            tooltip.add(effect.getDisplayName().copy().withStyle(effect.getCategory().getTooltipFormatting()));
            if (!instance.amplifier.isAny()) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.amplifier", IngredientUtil.text(instance.amplifier, value -> value == 0 ? new TextComponent("I") : new TranslatableComponent("potion.potency." + value))));
            }
            if (!instance.duration.isAny()) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.duration", IngredientUtil.text(instance.duration, value -> new TextComponent(TooltipUtil.formatTimeTicks(value)))));
            }
            if (instance.ambient != null) {
                if (instance.ambient) {
                    tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.ambient"));
                } else {
                    tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.no_ambient"));
                }
            }
            if (instance.visible != null) {
                if (instance.visible) {
                    tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.no_invisible"));
                } else {
                    tooltip.add(new TranslatableComponent("jea.item.tooltip.effect.invisible"));
                }
            }
        }
    }
    
    public static void renderLocationPredicate(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, LocationPredicate predicate, @Nullable BlockState currentCycle) {
        renderLocationPredicate(poseStack, buffer, mc, predicate, currentCycle, null, null);
    }
    
    public static <T extends Comparable<T>> void renderLocationPredicate(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, LocationPredicate predicate, @Nullable BlockState currentCycle, @Nullable Property<T> additionalProperty, @CheckForNull T value) {
        BlockState state = null;
        if (currentCycle != null) {
            state = currentCycle;
        } else {
            if (predicate.block.blocks != null && !predicate.block.blocks.isEmpty()) {
                state = IngredientUtil.getState(JeaRender.cycle(predicate.block.blocks.stream().sorted(Comparator.nullsFirst(Comparator.comparing(Block::getRegistryName))).toList()), predicate.block.properties);
            } else if (predicate.block.tag != null && !predicate.block.tag.getValues().isEmpty()) {
                state = IngredientUtil.getState(JeaRender.cycle(predicate.block.tag.getValues()), predicate.block.properties);
            }
        }
        FluidState fluid = null;
        if (predicate.fluid.fluid != null) {
            fluid = predicate.fluid.fluid.defaultFluidState();
        } else if (predicate.fluid.tag != null && !predicate.fluid.tag.getValues().isEmpty()) {
            fluid = JeaRender.cycle(predicate.fluid.tag.getValues()).defaultFluidState();
        }
        if (fluid != null) {
            if (state == null) {
                state = fluid.createLegacyBlock();
            } else if (Fluids.WATER.isSame(fluid.getType()) && state.hasProperty(BlockStateProperties.WATERLOGGED)) {
                state = state.setValue(BlockStateProperties.WATERLOGGED, true);
            }
        }
        if (state == null) {
            state = Blocks.COBBLESTONE.defaultBlockState();
        }
        
        if (additionalProperty != null && value != null && state.hasProperty(additionalProperty)) {
            state = state.setValue(additionalProperty, value);
        }
        
        boolean campfire = predicate.smokey != null && predicate.smokey;
        poseStack.pushPose();
        
        if (campfire) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0, 0.5);
            poseStack.scale(0.8f, 0.8f, 0.8f);
            poseStack.translate(-0.5, 0, -0.5);
            //noinspection deprecation
            mc.getBlockRenderer().renderSingleBlock(JeaRender.cycle(smokeSources).defaultBlockState(), poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
            poseStack.translate(0, 0.7, 0);
        }
        //noinspection deprecation
        mc.getBlockRenderer().renderSingleBlock(state, poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
    
    public static double getYOffset(LocationPredicate predicate) {
        return predicate.smokey != null && predicate.smokey ? 0.48 : 0;
    }
}
