package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderMisc;
import de.melanx.jea.render.SteveRender;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.BrewedPotionTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class BrewPotionInfo implements ICriterionInfo<BrewedPotionTrigger.TriggerInstance> {
    
    private final Map<Potion, Integer> potionColors = new HashMap<>();
    
    @SuppressWarnings("UnstableApiUsage")
    private final List<ItemStack> potionCycle = IntStream.range(0, 360)
            .filter(i -> i % 20 == 0).boxed()
            .map(i -> {
                ItemStack stack = new ItemStack(Items.POTION);
                stack.setHoverName(new TranslatableComponent("jea.item.tooltip.any_potion").withStyle(ChatFormatting.GOLD));
                CompoundTag nbt = stack.getOrCreateTag();
                nbt.putInt("CustomPotionColor", Color.getHSBColor(i / 360f, 1, 1).getRGB());
                stack.setTag(nbt);
                return stack;
            }).collect(ImmutableList.toImmutableList());

    @Override
    public Class<BrewedPotionTrigger.TriggerInstance> criterionClass() {
        return BrewedPotionTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.TriggerInstance instance, IIngredients ii) {
        if (instance.potion != null) {
            ItemStack potion = PotionUtils.setPotion(new ItemStack(Items.POTION), instance.potion);
            ItemStack splash = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), instance.potion);
            ItemStack lingering = PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), instance.potion);
            ii.setInputLists(VanillaTypes.ITEM, List.of(
                    List.of(potion, splash, lingering)
            ));
        } else {
            ii.setInputLists(VanillaTypes.ITEM, List.of(
                    this.potionCycle
            ));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, RECIPE_WIDTH - 58, SPACE_TOP + 35, 48, 48, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 84;
        boolean hasBottle;
        float brewingProgress;
        ItemStack held;
        float swing;
        int useTick;
        if (animationTime < 32) {
            hasBottle = true;
            held = ItemStack.EMPTY;
            swing = 0;
            useTick = 0;
            brewingProgress = animationTime / 32f;
        } else if (animationTime < 42) {
            swing = getSwingTime(animationTime - 32);
            if (swing > 0.5) {
                hasBottle = false;
                held = PotionUtils.setPotion(new ItemStack(Items.POTION), instance.potion == null ? Potions.WATER : instance.potion);
            } else {
                hasBottle = true;
                held = ItemStack.EMPTY;
            }
            useTick = 0;
            brewingProgress = -1;
        } else if (animationTime < 74) {
            useTick = Math.round(32 - (animationTime - 42));
            hasBottle = false;
            held = useTick <= 2 ? new ItemStack(Items.GLASS_BOTTLE) : PotionUtils.setPotion(new ItemStack(Items.POTION), instance.potion == null ? Potions.WATER : instance.potion);
            swing = 0;
            brewingProgress = -1;
        } else {
            swing = getSwingTime(animationTime - 74);
            if (swing > 0.5) {
                hasBottle = true;
                held = ItemStack.EMPTY;
            } else {
                hasBottle = false;
                held = new ItemStack(Items.GLASS_BOTTLE);
            }
            useTick = 0;
            brewingProgress = -1;
        }
        
        poseStack.pushPose();
        poseStack.translate(80, SPACE_TOP + 85, 0);
        JeaRender.normalize(poseStack);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-15));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
        JeaRender.transformForEntityRenderSide(poseStack, true, 2);
        BlockState state = Blocks.BREWING_STAND.defaultBlockState().setValue(BlockStateProperties.HAS_BOTTLE_0, hasBottle);
        //noinspection deprecation
        mc.getBlockRenderer().renderSingleBlock(state, poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(18, SPACE_TOP + 85, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.2f);
        SteveRender.defaultPose(mc);
        SteveRender.setEquipmentHand(mc, held);
        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
        SteveRender.use(useTick, InteractionHand.MAIN_HAND);
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        if (brewingProgress >= 0) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            if (instance.potion == null) {
                RenderHelper.rgb(Color.getHSBColor(brewingProgress, 0.7f, 0.9f).getRGB());
            } else if (this.potionColors.containsKey(instance.potion)) {
                RenderHelper.rgb(this.potionColors.get(instance.potion));
            } else {
                int color = PotionUtils.getColor(instance.potion);
                this.potionColors.put(instance.potion, color);
                RenderHelper.rgb(color);
            }
            JustEnoughAdvancementsJEIPlugin.getPotionBubbles().draw(poseStack, 52, SPACE_TOP + 19, Math.round((Math.max(0, 1 - (brewingProgress * 1.1f))) * JustEnoughAdvancementsJEIPlugin.getPotionBubbles().getHeight()), 0, 0, 0);
            RenderHelper.resetColor();
            RenderSystem.disableBlend();
        }
        if (instance.potion != null) {
            poseStack.pushPose();
            poseStack.translate(RECIPE_WIDTH - 25, SPACE_TOP + 5, 0);
            for (MobEffectInstance effect : instance.potion.getEffects()) {
                RenderMisc.renderMobEffect(poseStack, mc, effect.getEffect());
                poseStack.translate(-22, 0, 0);
            }
            poseStack.popPose();
        }
    }
    
    private static float getSwingTime(float timeTenTicks) {
        if (timeTenTicks < 2) {
            return 0;
        } else if (timeTenTicks > 8) {
            return 1;
        } else {
            return (timeTenTicks - 2) / 6f;
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (instance.potion != null) {
            int x = RECIPE_WIDTH - 25;
            for (MobEffectInstance effect : instance.potion.getEffects()) {
                RenderMisc.addMobEffectTooltip(tooltip, effect, Optional.empty(), x, SPACE_TOP + 5, mouseX, mouseY);
                x -= 22;
            }
        }
    }
}
