package de.melanx.jea.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.TooltipUtil;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Food;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.*;

public class RenderMisc {
    
    private static final Map<Effect, ResourceLocation> potionTextures = new HashMap<>();
    private static final List<Block> smokeSources = ImmutableList.of(Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
    
    public static void renderFood(MatrixStack matrixStack, Food food, int minIcons) {
        int value = MathHelper.clamp(food.getHealing(), 0, 20);
        float saturation = MathHelper.clamp(food.getSaturation() * food.getHealing(), 0, value);
        int icons = MathHelper.clamp((int) Math.ceil(value / 2d), minIcons, 10);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrixStack.push();
        for (int i = 0; i < icons; i++) {
            JustEnoughAdvancementsJEIPlugin.getHungerEmpty().draw(matrixStack, 9 * i, 0);
        }
        matrixStack.translate(0, 0, 10);
        for (int i = 0; i < (value / 2); i++) {
            JustEnoughAdvancementsJEIPlugin.getHungerFull().draw(matrixStack, 9 * (icons - i - 1), 0);
        }
        if (value % 2 != 0) {
            JustEnoughAdvancementsJEIPlugin.getHungerHalf().draw(matrixStack, 9 * (icons - (value / 2) - 1), 0);
        }
        matrixStack.translate(0, 0, 10);
        for (int i = 0; i < (int) Math.floor(saturation / 2); i++) {
            JustEnoughAdvancementsJEIPlugin.getHungerSaturation().draw(matrixStack, 9 * (icons - i - 1), 0);
        }
        float left = (saturation / 2) - (float) Math.floor(saturation / 2);
        int cutPixels = 9 - MathHelper.clamp(Math.round(9 * left), 0, 9);
        if (cutPixels < 9) {
            JustEnoughAdvancementsJEIPlugin.getHungerSaturation().draw(matrixStack, 9 * (icons - (int) Math.floor(saturation / 2) - 1), 0, 0, 0, cutPixels, 0);
        }
        matrixStack.pop();
        RenderSystem.disableBlend();
    }
    
    public static void renderMobEffect(MatrixStack matrixStack, Minecraft mc, Effect effect) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrixStack.push();
        JustEnoughAdvancementsJEIPlugin.getEffectSlot().draw(matrixStack, 0, 0);
        matrixStack.translate(2, 2, 10);
        matrixStack.scale(16/18f, 16/18f, 1);
        ResourceLocation potionTexture;
        if (potionTextures.containsKey(effect)) {
            potionTexture = potionTextures.get(effect);
        } else {
            potionTexture = new ResourceLocation(Objects.requireNonNull(effect.getRegistryName()).getNamespace(), "textures/mob_effect/" + Objects.requireNonNull(effect.getRegistryName()).getPath() + ".png");
            potionTextures.put(effect, potionTexture);
        }
        mc.getTextureManager().bindTexture(potionTexture);
        AbstractGui.blit(matrixStack, 0, 0, 0, 0, 18, 18, 18, 18);
        matrixStack.pop();
        RenderSystem.disableBlend();
    }
    
    public static void addMobEffectTooltip(List<ITextComponent> tooltip, EffectInstance effect, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Float> chance, int x, int y, double mouseX, double mouseY) {
        addMobEffectTooltip(tooltip, effect, chance, x, y, 20, mouseX, mouseY);
    }
    
    public static void addMobEffectTooltip(List<ITextComponent> tooltip, EffectInstance effect, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Float> chance, int x, int y, int size, double mouseX, double mouseY) {
        if (mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size) {
            tooltip.add(effect.getPotion().getDisplayName().deepCopy().append(new StringTextComponent(" ")).append(new TranslationTextComponent("potion.potency." + effect.getAmplifier())).mergeStyle(effect.getPotion().getEffectType().getColor()));
            chance.ifPresent(c -> tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.chance", TooltipUtil.formatChance(c))));
            if (!effect.getPotion().isInstant() && effect.getDuration() != 0) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.duration", TooltipUtil.formatTimeTicks(effect.getDuration())));
            }
            if (effect.isAmbient()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.ambient"));
            }
            if (!effect.doesShowParticles()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.invisible"));
            }
        }
    }
    
    public static void addMobEffectTooltip(List<ITextComponent> tooltip, Effect effect, MobEffectsPredicate.InstancePredicate instance, int x, int y, double mouseX, double mouseY) {
        addMobEffectTooltip(tooltip, effect, instance, x, y, 20, mouseX, mouseY);
    }
    
    public static void addMobEffectTooltip(List<ITextComponent> tooltip, Effect effect, MobEffectsPredicate.InstancePredicate instance, int x, int y, int size, double mouseX, double mouseY) {
        if (mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size) {
            tooltip.add(effect.getDisplayName().deepCopy().mergeStyle(effect.getEffectType().getColor()));
            if (!instance.amplifier.isUnbounded()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.amplifier", IngredientUtil.text(instance.amplifier, value -> value == 0 ? new StringTextComponent("I") : new TranslationTextComponent("potion.potency." + value))));
            }
            if (!instance.duration.isUnbounded()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.duration", IngredientUtil.text(instance.duration, value -> new StringTextComponent(TooltipUtil.formatTimeTicks(value)))));
            }
            if (instance.ambient != null) {
                if (instance.ambient) {
                    tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.ambient"));
                } else {
                    tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.no_ambient"));
                }
            }
            if (instance.visible != null) {
                if (instance.visible) {
                    tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.no_invisible"));
                } else {
                    tooltip.add(new TranslationTextComponent("jea.item.tooltip.effect.invisible"));
                }
            }
        }
    }
    
    public static void renderLocationPredicate(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, LocationPredicate predicate, @Nullable BlockState currentCycle) {
        renderLocationPredicate(matrixStack, buffer, mc, predicate, currentCycle, null, null);
    }
    
    public static <T extends Comparable<T>> void renderLocationPredicate(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, LocationPredicate predicate, @Nullable BlockState currentCycle, @Nullable Property<T> additionalProperty, @CheckForNull T value) {
        BlockState state = null;
        if (currentCycle != null) {
            state = currentCycle;
        } else {
            if (predicate.block.block != null) {
                state = IngredientUtil.getState(predicate.block.block, predicate.block.statePredicate);
            } else if (predicate.block.tag != null && !predicate.block.tag.getAllElements().isEmpty()) {
                state = IngredientUtil.getState(JeaRender.cycle(predicate.block.tag.getAllElements()), predicate.block.statePredicate);
            }
        }
        FluidState fluid = null;
        if (predicate.fluid.fluid != null) {
            fluid = predicate.fluid.fluid.getDefaultState();
        } else if (predicate.fluid.fluidTag != null && !predicate.fluid.fluidTag.getAllElements().isEmpty()) {
            fluid = JeaRender.cycle(predicate.fluid.fluidTag.getAllElements()).getDefaultState();
        }
        if (fluid != null) {
            if (state == null) {
                state = fluid.getBlockState();
            } else if (Fluids.WATER.isEquivalentTo(fluid.getFluid()) && state.hasProperty(BlockStateProperties.WATERLOGGED)) {
                state = state.with(BlockStateProperties.WATERLOGGED, true);
            }
        }
        if (state == null) {
            state = Blocks.COBBLESTONE.getDefaultState();
        }
        
        if (additionalProperty != null && value != null && state.hasProperty(additionalProperty)) {
            state = state.with(additionalProperty, value);
        }
        
        boolean campfire = predicate.smokey != null && predicate.smokey;
        matrixStack.push();
        
        if (campfire) {
            matrixStack.push();
            matrixStack.translate(0.5, 0, 0.5);
            matrixStack.scale(0.8f, 0.8f, 0.8f);
            matrixStack.translate(-0.5, 0, -0.5);
            //noinspection deprecation
            mc.getBlockRendererDispatcher().renderBlock(JeaRender.cycle(smokeSources).getDefaultState(), matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
            matrixStack.pop();
            matrixStack.translate(0, 0.7, 0);
        }
        //noinspection deprecation
        mc.getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        matrixStack.pop();
    }
    
    public static double getYOffset(LocationPredicate predicate) {
        return predicate.smokey != null && predicate.smokey ? 0.48 : 0;
    }
}
