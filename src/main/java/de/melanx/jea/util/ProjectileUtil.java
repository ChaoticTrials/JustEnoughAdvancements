package de.melanx.jea.util;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import net.minecraft.advancements.criterion.DamagePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectileUtil {
    
    public static List<ItemStack> getProjectileStack(Minecraft mc, EntityPredicate predicate) {
        List<EntityType<?>> types = ImmutableList.of();
        if (predicate.type instanceof EntityTypePredicate.TypePredicate
                && ((EntityTypePredicate.TypePredicate) predicate.type).type != null) {
            types = ImmutableList.of(((EntityTypePredicate.TypePredicate) predicate.type).type);
        } else if (predicate.type instanceof EntityTypePredicate.TagPredicate
                && ((EntityTypePredicate.TagPredicate) predicate.type).tag != null
                && !((EntityTypePredicate.TagPredicate) predicate.type).tag.getAllElements().isEmpty()) {
            types = ((EntityTypePredicate.TagPredicate) predicate.type).tag.getAllElements();
        }
        if (types.isEmpty()) {
            ItemStack stack = new ItemStack(Items.ARROW);
            stack.setDisplayName(new TranslationTextComponent("jea.item.tooltip.any_projectile").mergeStyle(TextFormatting.GOLD));
            return ImmutableList.of(stack);
        } else {
            return types.stream().map(type -> {
                if (type == null || type == EntityType.ARROW || type == EntityType.SPECTRAL_ARROW) {
                    return new ItemStack(Items.ARROW);
                } else if (type == EntityType.WITHER_SKULL) {
                    return new ItemStack(Items.WITHER_SKELETON_SKULL);
                } else if (type == EntityType.TRIDENT) {
                    return new ItemStack(Items.TRIDENT);
                } else {
                    Entity entity = RenderEntityCache.getRenderEntity(mc, type);
                    if (entity instanceof IRendersAsItem) {
                        return ((IRendersAsItem) entity).getItem();
                    } else {
                        return new ItemStack(Items.ARROW);
                    }
                }
            }).collect(Collectors.toList());
        }
    }
    
    public static void rotateCenter(MatrixStack matrixStack, ItemStack projectile, Quaternion quaternion) {
        if (projectile.getItem() instanceof ArrowItem) {
            matrixStack.translate(8, 2.5, 0);
            matrixStack.rotate(quaternion);
            matrixStack.translate(-8, -2.5, 0);
        } else {
            matrixStack.translate(4, 4, 0);
            matrixStack.rotate(quaternion);
            matrixStack.translate(-4, -4, 0);
        }
    }
    
    public static void renderProjectile(MatrixStack matrixStack, ItemStack projectile) {
        if (projectile.getItem() instanceof ArrowItem) {
            RenderSystem.enableBlend();
            JustEnoughAdvancementsJEIPlugin.getShotArrow().draw(matrixStack, 0, 0);
            RenderSystem.disableBlend();
        } else {
            matrixStack.push();
            matrixStack.translate(10, -2, 0);
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            matrixStack.translate(8, 8, 0);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(55));
            matrixStack.translate(-8, -8, 0);
            //noinspection deprecation
            RenderSystem.pushMatrix();
            //noinspection deprecation
            RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
            Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(projectile, 0, 0);
            //noinspection deprecation
            RenderSystem.popMatrix();
            Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish();
            RenderHelper.resetColor();
            matrixStack.pop();
        }
    }
    
    public static ItemStack getHeldItemForProjectile(DamagePredicate damage) {
        List<EntityType<?>> types = ImmutableList.of();
        if (damage.type.directEntity.type instanceof EntityTypePredicate.TypePredicate
                && ((EntityTypePredicate.TypePredicate) damage.type.directEntity.type).type != null) {
            types = ImmutableList.of(((EntityTypePredicate.TypePredicate) damage.type.directEntity.type).type);
        } else if (damage.type.directEntity.type instanceof EntityTypePredicate.TagPredicate
                && ((EntityTypePredicate.TagPredicate) damage.type.directEntity.type).tag != null
                && !((EntityTypePredicate.TagPredicate) damage.type.directEntity.type).tag.getAllElements().isEmpty()) {
            types = ((EntityTypePredicate.TagPredicate) damage.type.directEntity.type).tag.getAllElements();
        }
        if (types.isEmpty()) {
            return new ItemStack(Items.BOW);
        } else {
            EntityType<?> type = JeaRender.cycle(types);
            if (type == EntityType.ARROW || type == EntityType.SPECTRAL_ARROW || type == null) {
                return new ItemStack(Items.BOW);
            } else if (type == EntityType.TRIDENT) {
                return new ItemStack(Items.TRIDENT);
            } else {
                return ItemStack.EMPTY;
            }
        }
    }
}
