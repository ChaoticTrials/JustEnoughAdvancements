package de.melanx.jea.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.LootUtil;
import de.melanx.jea.util.TooltipUtil;
import de.melanx.jea.util.Util;
import io.github.noeppi_noeppi.libx.annotation.model.Model;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderEntityCache {
    
    @Model("special/unknown_entity")
    public static BakedModel UNKNOWN_ENTITY = null;
    
    private static final double HEIGHT = 1.8;
    private static final double WIDTH = 0.8;
    private static final AABB UNKNOWN_ENTITY_AABB = new AABB(0, 0, 0, 1, 1, 1);
    private static final ResourceLocation BOTANIA_GAIA = new ResourceLocation("botania", "doppleganger");
    
    private static final Map<EntityType<?>, Entity> CACHE = new HashMap<>();
    
    public static <T extends Entity> T getRenderEntity(Minecraft mc, EntityType<T> type) {
        if (!type.canSummon()) {
            return null;
        } else if (CACHE.containsKey(type) && CACHE.get(type).level == mc.level) {
            //noinspection unchecked
            return (T) CACHE.get(type);
        } else {
            @SuppressWarnings("ConstantConditions")
            T entity = type.create(mc.level);
            if (entity != null) {
                entity.setSilent(true);
                if (entity instanceof Mob mob) {
                    mob.setNoAi(true);
                }
                // The game crashes if this is not there. But it does not crash in our render code
                // but when trying to render a chunk. Very weird.
                if (entity instanceof SnowGolem golem) {
                    golem.setPumpkin(false);
                }
                if (entity instanceof WaterAnimal) {
                    entity.wasTouchingWater = true;
                }
            }
            CACHE.put(type, entity);
            return entity;
        }
    }

    @Nullable
    public static Entity getRenderEntity(Minecraft mc, EntityPredicate predicate, DefaultEntityProperties properties) {
        Entity entity = null;
        if (properties.forcedType && properties.type != null) {
            entity = getRenderEntity(mc, properties.type);
        } else if (predicate.entityType instanceof EntityTypePredicate.TypePredicate typePredicate && typePredicate.type != null) {
            entity = getRenderEntity(mc, typePredicate.type);
        } else if (predicate.entityType instanceof EntityTypePredicate.TagPredicate tagPredicate && tagPredicate.tag != null && !tagPredicate.tag.getValues().isEmpty()) {
            List<EntityType<?>> list = tagPredicate.tag.getValues();
            entity = getRenderEntity(mc, list.get((ClientTickHandler.ticksInGame / 60) % list.size()));
        } else if (properties.type != null) {
            entity = getRenderEntity(mc, properties.type);
        } else if (predicate.catType != null) {
            // Minecraft does not give an entity type when giving a cat type
            // It is still always a cat though
            entity = getRenderEntity(mc, EntityType.CAT);
        }
        return entity;
    }

    public static void renderPlainEntity(Minecraft mc, EntityType<?> type, PoseStack poseStack, MultiBufferSource buffer) {
        Entity entity = getRenderEntity(mc, type);
        if (entity != null) {
            entity.setRemainingFireTicks(0);
            entity.setShiftKeyDown(false);
            entity.setSprinting(false);
            entity.setSwimming(false);
            if (entity instanceof AgeableMob ageable) {
                ageable.setAge(10);
            } else if (entity instanceof Zombie zombie) {
                zombie.setBaby(false);
            }
            entity.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            entity.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            entity.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            entity.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            entity.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
            entity.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
            if (entity instanceof Cat cat) {
                cat.setCatType(0);
            }
            if (entity instanceof LivingEntity living) {
                living.attackAnim = 0;
                living.swingingArm = InteractionHand.MAIN_HAND;
                living.animationPosition = 0;
                living.startUsingItem(InteractionHand.MAIN_HAND);
                living.useItemRemaining = 0;
                living.hurtTime = 0;
                living.deathTime = 0;
            }
            if (entity instanceof TamableAnimal tameable) {
                tameable.setTame(false);
                tameable.setOwnerUUID(null);
                tameable.setOrderedToSit(false);
                tameable.setInSittingPose(false);
            }
            if (entity instanceof AbstractHorse horse) {
                horse.setTamed(false);
            }
            //noinspection unchecked
            EntityRenderer<Entity> render = (EntityRenderer<Entity>) mc.getEntityRenderDispatcher().renderers.get(entity.getType());
            if (render != null) {
                poseStack.pushPose();
                AABB bb = entity.getBoundingBoxForCulling();
                float scale = getEntityScale(entity, false);
                poseStack.scale(scale, scale, scale);
                entity.tickCount = ClientTickHandler.ticksInGame;
                render.render(entity, 0, 0, poseStack, buffer, LightTexture.pack(15, 15));
                poseStack.popPose();
            }
        }
    }
    
    public static void renderEntity(Minecraft mc, EntityPredicate.Composite entityPredicate, PoseStack poseStack, MultiBufferSource buffer, EntityTransformation transform) {
        renderEntity(mc, entityPredicate, poseStack, buffer, transform, DefaultEntityProperties.DEFAULT);
    }
    
    public static void renderEntity(Minecraft mc, EntityPredicate.Composite entityPredicate, PoseStack poseStack, MultiBufferSource buffer, EntityTransformation transform, DefaultEntityProperties properties) {
        EntityPredicate predicate = LootUtil.asEntity(entityPredicate);
        Entity entity = getRenderEntity(mc, predicate, properties);
        if (entity == null) {
            poseStack.pushPose();
            transform.applyForMissing(poseStack);
            poseStack.scale(0.7f, 0.7f, 0.7f);
            poseStack.translate(0, 0.5, 0);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(22.5f));
            poseStack.translate(0, -0.5, 0);
            poseStack.mulPose(Vector3f.YP.rotationDegrees((ClientTickHandler.ticksInGame + mc.getFrameTime()) % 360));
            poseStack.translate(-0.5, 0.25, -0.5);
            VertexConsumer vertex = buffer.getBuffer(RenderType.solid());
            //noinspection deprecation
            Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                    .renderModel(poseStack.last(), vertex, null,
                            UNKNOWN_ENTITY, 1, 1, 1,
                            LightTexture.pack(15, 15),
                            OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        } else {
            entity.setRemainingFireTicks((predicate.flags.isOnFire == null ? properties.fire : predicate.flags.isOnFire) ? 10 : 0);
            entity.setShiftKeyDown(predicate.flags.isCrouching == Boolean.valueOf(true));
            entity.setSprinting(predicate.flags.isSprinting == Boolean.valueOf(true));
            entity.setSwimming(predicate.flags.isSwimming == Boolean.valueOf(true));
            boolean baby = properties.baby;
            if (predicate.flags.isBaby != null) {
                baby = predicate.flags.isBaby;
            }
            if (entity instanceof AgeableMob ageable) {
                ageable.setAge(baby ? -10 : 10);
            } else if (entity instanceof Zombie zombie) {
                zombie.setBaby(baby);
            }
            if (IngredientUtil.fromItemPredicate(predicate.equipment.mainhand).isEmpty() && !properties.getHeld().isEmpty()) {
                entity.setItemSlot(EquipmentSlot.MAINHAND, properties.getHeld());
            } else {
                entity.setItemSlot(EquipmentSlot.MAINHAND, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.mainhand, (ItemLike) null)));
            }
            entity.setItemSlot(EquipmentSlot.OFFHAND, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.offhand, (ItemLike) null)));
            entity.setItemSlot(EquipmentSlot.HEAD, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.head, (ItemLike) null)));
            entity.setItemSlot(EquipmentSlot.CHEST, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.chest, (ItemLike) null)));
            entity.setItemSlot(EquipmentSlot.LEGS, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.legs, (ItemLike) null)));
            entity.setItemSlot(EquipmentSlot.FEET, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.feet, (ItemLike) null)));
            if (entity instanceof Cat cat) {
                if (predicate.catType != null) {
                    cat.setCatType(Cat.TEXTURE_BY_TYPE.entrySet().stream().filter(entry -> entry.getValue().equals(predicate.catType)).findFirst().map(Map.Entry::getKey).orElse(0));
                } else {
                    cat.setCatType((ClientTickHandler.ticksInGame / 10) % Cat.TEXTURE_BY_TYPE.size());
                }
            }
            if (entity instanceof LivingEntity living) {
                living.attackAnim = properties.swing;
                living.swingingArm = InteractionHand.MAIN_HAND;
                living.animationPosition = properties.limbSwing;
                living.startUsingItem(InteractionHand.MAIN_HAND);
                living.useItemRemaining = properties.useTick;
                living.hurtTime = properties.hurtTime;
                living.deathTime = properties.deathTime;
            }
            if (entity instanceof TamableAnimal tameable) {
                tameable.setTame(properties.tamed);
                tameable.setOwnerUUID(properties.tamed ? Util.PLACEHOLDER_UUID : null);
                tameable.setOrderedToSit(properties.tamed);
                tameable.setInSittingPose(properties.tamed);
            }
            if (entity instanceof AbstractHorse horse) {
                horse.setTamed(properties.tamed);
            }
            //noinspection unchecked
            EntityRenderer<Entity> render = (EntityRenderer<Entity>) mc.getEntityRenderDispatcher().renderers.get(entity.getType());
            if (render != null) {
                poseStack.pushPose();
                transform.applyForEntity(poseStack);
                float scale = getEntityScale(entity, baby);
                poseStack.scale(scale, scale, scale);
                if (entity instanceof EnderDragon) {
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                }
                poseStack.translate(0, horizontalOffset(entity), 0);
                entity.tickCount = ClientTickHandler.ticksInGame;
                render.render(entity, 0, 0, poseStack, buffer, LightTexture.pack(15, 15));
                poseStack.popPose();
            }
        }
    }

    public static void addTooltipForEntity(Minecraft mc, List<Component> tooltip, EntityPredicate.Composite entityPredicate, double absoluteX, double absoluteY, double totalScale, double mouseX, double mouseY) {
        addTooltipForEntity(mc, tooltip, entityPredicate, absoluteX, absoluteY, totalScale, DefaultEntityProperties.DEFAULT, mouseX, mouseY);
    }
    
    public static void addTooltipForEntity(Minecraft mc, List<Component> tooltip, EntityPredicate.Composite entityPredicate, double absoluteX, double absoluteY, double totalScale, DefaultEntityProperties properties, double mouseX, double mouseY) {
        EntityPredicate predicate = LootUtil.asEntity(entityPredicate);
        Entity entity = getRenderEntity(mc, predicate, properties);
        AABB bb;
        float entityScale;
        if (entity == null) {
            bb = UNKNOWN_ENTITY_AABB;
            entityScale = 0.7f;
        } else {
            boolean baby = properties.baby;
            if (predicate.flags.isBaby != null) {
                baby = predicate.flags.isBaby;
            }
            // We also need to set the entity a baby here as it changes the bounding box.
            if (entity instanceof AgeableMob ageable) {
                ageable.setAge(baby ? -10 : 10);
            } else if (entity instanceof Zombie zombie) {
                zombie.setBaby(baby);
            }
            bb = entity.getBoundingBoxForCulling();
            entityScale = getEntityScale(entity, baby);
        }
        double scale = totalScale * entityScale;
        // Animal entities often have a head outside the bounding box and a multiplier of 1.6 ist the best match there.
        boolean insideBox = (mouseX > absoluteX - (0.7 * scale * bb.getXsize()) || mouseX > absoluteX - (0.7 * scale * bb.getZsize()))
                && (mouseX < absoluteX + (0.7 * scale * bb.getXsize()) || mouseX < absoluteX + (0.7 * scale * bb.getZsize()))
                && mouseY > (absoluteY + HEIGHT - horizontalTooltipScale(entity) * scale * bb.getYsize() - (horizontalOffset(entity) * scale))
                && mouseY < (absoluteY - (horizontalOffset(entity) * scale));
        if (insideBox) {
            if (properties.forcedType && properties.type != null) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.entity.type.type", properties.type.getDescription()).withStyle(ChatFormatting.GOLD));
            } else if (predicate.entityType instanceof EntityTypePredicate.TypePredicate typePredicate && typePredicate.type != null) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.entity.type.type", typePredicate.type.getDescription()).withStyle(ChatFormatting.GOLD));
            } else if (predicate.entityType instanceof EntityTypePredicate.TagPredicate tagPredicate && tagPredicate.tag != null && !(tagPredicate.tag instanceof Tag.Named)) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.entity.type.type", ((Tag.Named<EntityType<?>>) ((EntityTypePredicate.TagPredicate) predicate.entityType).tag).getName().toString()).withStyle(ChatFormatting.GOLD));
            } else if (properties.type != null && !properties.typeDisplayOnly) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.entity.type.type", properties.type.getDescription()).withStyle(ChatFormatting.GOLD));
            } else if (predicate.catType != null && entity instanceof Cat) {
                // Minecraft does not specify a type when setting the catType predicate
                // However it must be a cat to work
                tooltip.add(new TranslatableComponent("jea.item.tooltip.entity.type.type", EntityType.CAT.getDescription()).withStyle(ChatFormatting.GOLD));
            } else {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.any_entity").withStyle(ChatFormatting.GOLD));
            }
            
            List<MutableComponent> additional = new ArrayList<>();
            
            TooltipUtil.addEffectValues(additional, predicate.effects);
            TooltipUtil.addDistanceValuesPlayerRelative(additional, predicate.distanceToPlayer);
            TooltipUtil.addLocationValues(additional, predicate.location);
            
            if (predicate.catType != null) {
                additional.add(new TranslatableComponent("jea.item.tooltip.entity.type.cat", IngredientUtil.rlFile(predicate.catType)));
            }
            
            if (entity != null && BOTANIA_GAIA.equals(entity.getType().getRegistryName())) {
                CompoundTag nbt = predicate.nbt.tag;
                if (nbt != null && nbt.contains("hardMode")) {
                    additional.add(new TranslatableComponent("jea.item.tooltip.botania.gaia.hardmode", TooltipUtil.yesNo(nbt.getBoolean("hardMode"))));
                }
            }

            for (MutableComponent tc : additional) {
                tooltip.add(tc.withStyle(ChatFormatting.AQUA));
            }
        }
    }

    private static float getEntityScale(Entity entity, boolean baby) {
        AABB bb = entity.getBoundingBoxForCulling();
        float scale = (float) Math.min(Math.min(WIDTH / bb.getXsize(), HEIGHT / bb.getYsize()), WIDTH / bb.getZsize());
        if (baby) {
            scale *= 0.75f;
        }
        if (entity instanceof Chicken) {
            scale *= 0.6;
        }
        if (entity instanceof Panda) {
            scale *= 1.4;
        }
        if (entity instanceof Llama) {
            scale *= 0.9;
        }
        if (entity instanceof Witch) {
            scale *= 0.8;
        }
        if (entity instanceof Slime) {
            scale *= 2.5;
        }
        if (entity instanceof CaveSpider) {
            scale *= 0.85;
        }
        if (entity instanceof Creeper) {
            scale *= 0.95;
        }
        if (entity instanceof Silverfish) {
            scale *= 0.6;
        }
        if (entity instanceof Endermite) {
            scale *= 0.7;
        }
        if (entity instanceof EnderDragon) {
            scale *= 2.5;
        }
        if (entity instanceof LivingEntity living && living.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof BannerItem) {
            scale *= 0.6;
        }
        if (entity instanceof Cat cat && cat.isInSittingPose()) {
            // Sitting cats are really large compared to standing cats, so we
            // scale them down a bit.
            scale *= 0.9;
        }
        return scale;
    }
    
    private static float horizontalTooltipScale(Entity entity) {
        // A method to best guess whether this entity has a head that sticks out of the hitbox.
        // In that case the tooltip must be calculated differently
        if (entity instanceof Cat && !((Cat) entity).isInSittingPose()) {
            // The sitting methods are called sleeping for cats...
            // Cats are really weird with their bounding box but only if they're not sitting...
            return 0.8f;
        } else if (entity instanceof Animal && !(entity instanceof Llama)) {
            // Mostly only animals have their head stick out.
            // Llamas are exceptions
            return 1.6f;
        } else {
            // All other animals are probably fine
            return 1.1f;
        }
    }
    
    private static double horizontalOffset(Entity entity) {
        if (entity instanceof Ghast) {
            return 3;
        } else if (entity instanceof Squid) {
            return 1.5;
        } else {
            return 0;
        }
    }
}
