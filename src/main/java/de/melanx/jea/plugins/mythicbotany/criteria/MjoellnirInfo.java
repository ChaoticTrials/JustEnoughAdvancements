package de.melanx.jea.plugins.mythicbotany.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mythicbotany.ModBlocks;
import mythicbotany.advancement.MjoellnirTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class MjoellnirInfo implements ICriterionInfo<MjoellnirTrigger.Instance> {

    @Override
    public Class<MjoellnirTrigger.Instance> criterionClass() {
        return MjoellnirTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, MjoellnirTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.fromItemPredicate(instance.item, true, ModBlocks.mjoellnir)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, MjoellnirTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, RECIPE_WIDTH - 23, SPACE_TOP + 6);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, MjoellnirTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, RECIPE_WIDTH - 23, SPACE_TOP + 6);
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, true, ModBlocks.mjoellnir));
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 100;
        float swing;
        boolean holdsHammer = animationTime < 10 || animationTime > 90;
        double hammerX;
        boolean hammerFlying;
        float lightningTicks;
        if (animationTime >= 7 && animationTime < 13) {
            swing = (animationTime - 7) / 6f;
        } else if (animationTime >= 87 && animationTime < 93) {
            swing = (animationTime - 87) / 6f;
        } else {
            swing = 0;
        }
        if (animationTime >= 10 && animationTime < 50) {
            hammerFlying = true;
            hammerX = (animationTime - 10) / 40f;
        } else if (animationTime >= 50 && animationTime < 90) {
            hammerX = 1 - ((animationTime - 50) / 40f);
            hammerFlying = false;
        } else {
            hammerX = -1;
            hammerFlying = false;
        }
        if (animationTime >= 50 && animationTime <= 58) {
            lightningTicks = (animationTime - 50) / 8f;
        } else {
            lightningTicks = 0;
        }
        double hammerY;
        double hammerAngle;
        if (hammerX >= 0) {
            if (hammerFlying) {
                double q = (hammerX - (1 - (MathHelper.sqrt(2) / 2)));
                hammerY = (2 * q * q) - 1;
                double hammerM = (2 * MathHelper.sqrt(2)) + (4 * hammerX) - 4;
                hammerAngle = Math.atan(hammerM) + Math.toRadians(225);
            } else {
                hammerY = 0;
                hammerAngle = Math.toRadians(315);
            }
        } else {
            hammerY = -1;
            hammerAngle = 0;
        }
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 1.9f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        if (holdsHammer) {
            SteveRender.setEquipmentHand(mc, stack);
        } else {
            SteveRender.clearEquipment(mc);
        }
        SteveRender.renderSteveStatic(mc, matrixStack, buffer);
        matrixStack.pop();

        if (lightningTicks > 0) {
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH - 84, SPACE_TOP + RECIPE_HEIGHT - 84 - 2, 0);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            IDrawableStatic lightning = JustEnoughAdvancementsJEIPlugin.getLightningStatic();
            lightning.draw(matrixStack, 0, 0, 0, Math.round((1 - lightningTicks) * lightning.getHeight()), 0, 0);
            RenderSystem.disableBlend();
            matrixStack.pop();
        }

        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 24, SPACE_TOP + RECIPE_HEIGHT - 2, 0);
        JeaRender.normalize(matrixStack);
        RenderEntityCache.renderEntity(mc, instance.entity, matrixStack, buffer, JeaRender.entityRenderFront(true, 1.6f));
        matrixStack.pop();
        
        if (hammerX >= 0) {
            matrixStack.push();
            matrixStack.translate(48 + (hammerX * 74), SPACE_TOP + 80 + (hammerY * 40), 0);
            JeaRender.normalize(matrixStack);
            matrixStack.rotate(Vector3f.ZP.rotation((float) hammerAngle));
            if (!hammerFlying) {
                matrixStack.scale(0.8f, 0.8f, 0.8f);
            }
            mc.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY, matrixStack, buffer);
            matrixStack.pop();
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, MjoellnirTrigger.Instance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.entity, RECIPE_WIDTH - 24, SPACE_TOP + RECIPE_HEIGHT - 2, JeaRender.normalScale(1.6f), mouseX, mouseY);
    }
}
