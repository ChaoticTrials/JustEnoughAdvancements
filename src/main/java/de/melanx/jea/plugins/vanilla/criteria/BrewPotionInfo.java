package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.advancements.criterion.BrewedPotionTrigger;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class BrewPotionInfo implements ICriterionInfo<BrewedPotionTrigger.Instance> {
    
    private final Map<Potion, Integer> potionColors = new HashMap<>();
    
    @SuppressWarnings("UnstableApiUsage")
    private final List<ItemStack> potionCycle = IntStream.range(0, 360)
            .filter(i -> i % 20 == 0).boxed()
            .map(i -> {
                ItemStack stack = new ItemStack(Items.POTION);
                stack.setDisplayName(new TranslationTextComponent("jea.item.tooltip.any_potion").mergeStyle(TextFormatting.GOLD));
                CompoundNBT nbt = stack.getOrCreateTag();
                nbt.putInt("CustomPotionColor", Color.getHSBColor(i / 360f, 1, 1).getRGB());
                stack.setTag(nbt);
                return stack;
            }).collect(ImmutableList.toImmutableList());

    @Override
    public Class<BrewedPotionTrigger.Instance> criterionClass() {
        return BrewedPotionTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.Instance instance, IIngredients ii) {
        if (instance.potion != null) {
            ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), instance.potion);
            ItemStack splash = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), instance.potion);
            ItemStack lingering = PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), instance.potion);
            ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                    ImmutableList.of(potion, splash, lingering)
            ));
        } else {
            ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                    this.potionCycle
            ));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, RECIPE_WIDTH - 58, SPACE_TOP + 35, 48, 48, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.Instance instance, double mouseX, double mouseY) {
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 84;
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
                held = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), instance.potion == null ? Potions.WATER : instance.potion);
            } else {
                hasBottle = true;
                held = ItemStack.EMPTY;
            }
            useTick = 0;
            brewingProgress = -1;
        } else if (animationTime < 74) {
            useTick = Math.round(32 - (animationTime - 42));
            hasBottle = false;
            held = useTick <= 2 ? new ItemStack(Items.GLASS_BOTTLE) : PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), instance.potion == null ? Potions.WATER : instance.potion);
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
        
        matrixStack.push();
        matrixStack.translate(80, SPACE_TOP + 85, 0);
        JeaRender.normalize(matrixStack);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-15));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-90));
        JeaRender.transformForEntityRenderSide(matrixStack, true, 2);
        BlockState state = Blocks.BREWING_STAND.getDefaultState().with(BlockStateProperties.HAS_BOTTLE_0, hasBottle);
        //noinspection deprecation
        mc.getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(18, SPACE_TOP + 85, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.2f);
        SteveRender.defaultPose(mc);
        SteveRender.setEquipmentHand(mc, held);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.use(useTick, Hand.MAIN_HAND);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        if (brewingProgress >= 0) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            if (instance.potion == null) {
                RenderHelper.color(Color.getHSBColor(brewingProgress, 0.7f, 0.9f).getRGB());
            } else if (this.potionColors.containsKey(instance.potion)) {
                RenderHelper.color(this.potionColors.get(instance.potion));
            } else {
                int color = PotionUtils.getPotionColor(instance.potion);
                this.potionColors.put(instance.potion, color);
                RenderHelper.color(color);
            }
            JustEnoughAdvancementsJEIPlugin.getPotionBubbles().draw(matrixStack, 52, SPACE_TOP + 19, Math.round((Math.max(0, 1 - (brewingProgress * 1.1f))) * JustEnoughAdvancementsJEIPlugin.getPotionBubbles().getHeight()), 0, 0, 0);
            RenderHelper.resetColor();
            RenderSystem.disableBlend();
        }
        if (instance.potion != null) {
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH - 25, SPACE_TOP + 5, 0);
            for (EffectInstance effect : instance.potion.getEffects()) {
                RenderMisc.renderMobEffect(matrixStack, mc, effect.getPotion());
                matrixStack.translate(-22, 0, 0);
            }
            matrixStack.pop();
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
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, BrewedPotionTrigger.Instance instance, double mouseX, double mouseY) {
        if (instance.potion != null) {
            int x = RECIPE_WIDTH - 25;
            for (EffectInstance effect : instance.potion.getEffects()) {
                RenderMisc.addMobEffectTooltip(tooltip, effect, Optional.empty(), x, SPACE_TOP + 5, mouseX, mouseY);
                x -= 22;
            }
        }
    }
}
