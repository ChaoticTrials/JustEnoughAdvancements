package de.melanx.jea.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectileUtil {
    
    public static List<ItemStack> getProjectileStack(Minecraft mc, EntityPredicate predicate) {
        List<EntityType<?>> types = List.of();
        if (predicate.entityType instanceof EntityTypePredicate.TypePredicate typePredicate && typePredicate.type != null) {
            types = List.of(typePredicate.type);
        } else if (predicate.entityType instanceof EntityTypePredicate.TagPredicate tagPredicate && tagPredicate.tag != null && !tagPredicate.tag.getValues().isEmpty()) {
            types = tagPredicate.tag.getValues();
        }
        if (types.isEmpty()) {
            ItemStack stack = new ItemStack(Items.ARROW);
            stack.setHoverName(new TranslatableComponent("jea.item.tooltip.any_projectile").withStyle(ChatFormatting.GOLD));
            return List.of(stack);
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
                    if (entity instanceof ItemSupplier supplier) {
                        return supplier.getItem();
                    } else {
                        return new ItemStack(Items.ARROW);
                    }
                }
            }).collect(Collectors.toList());
        }
    }
    
    public static void rotateCenter(PoseStack poseStack, ItemStack projectile, Quaternion quaternion) {
        if (projectile.getItem() instanceof ArrowItem) {
            poseStack.translate(8, 2.5, 0);
            poseStack.mulPose(quaternion);
            poseStack.translate(-8, -2.5, 0);
        } else {
            poseStack.translate(4, 4, 0);
            poseStack.mulPose(quaternion);
            poseStack.translate(-4, -4, 0);
        }
    }
    
    public static void renderProjectile(PoseStack poseStack, ItemStack projectile) {
        if (projectile.getItem() instanceof ArrowItem) {
            RenderSystem.enableBlend();
            JustEnoughAdvancementsJEIPlugin.getShotArrow().draw(poseStack, 0, 0);
            RenderSystem.disableBlend();
        } else {
            poseStack.pushPose();
            poseStack.translate(10, -2, 0);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(8, 8, 0);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(55));
            poseStack.translate(-8, -8, 0);
            RenderSystem.getModelViewStack().pushPose();
            RenderSystem.getModelViewStack().mulPoseMatrix(poseStack.last().pose());
            RenderSystem.applyModelViewMatrix();
            Minecraft.getInstance().getItemRenderer().renderAndDecorateFakeItem(projectile, 0, 0);
            RenderSystem.getModelViewStack().popPose();
            RenderSystem.applyModelViewMatrix();
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            RenderHelper.resetColor();
            poseStack.popPose();
        }
    }
    
    public static ItemStack getHeldItemForProjectile(DamagePredicate damage) {
        List<EntityType<?>> types = List.of();
        if (damage.type.directEntity.entityType instanceof EntityTypePredicate.TypePredicate typePredicate && typePredicate.type != null) {
            types = List.of(typePredicate.type);
        } else if (damage.type.directEntity.entityType instanceof EntityTypePredicate.TagPredicate tagPredicate && tagPredicate.tag != null && !tagPredicate.tag.getValues().isEmpty()) {
            types = tagPredicate.tag.getValues();
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
