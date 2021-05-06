package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.LootUtil;
import de.melanx.jea.util.TooltipUtil;
import de.melanx.jea.util.Util;
import io.github.noeppi_noeppi.libx.annotation.Model;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderEntityCache {
    
    @Model("special/unknown_entity")
    public static IBakedModel UNKNOWN_ENTITY = null;
    
    private static final double HEIGHT = 1.8;
    private static final double WIDTH = 0.8;
    private static final AxisAlignedBB UNKNOWN_ENTITY_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    private static final ResourceLocation BOTANIA_GAIA = new ResourceLocation("botania", "doppleganger");
    
    private static final Map<EntityType<?>, Entity> CACHE = new HashMap<>();
    
    public static <T extends Entity> T getRenderEntity(Minecraft mc, EntityType<T> type) {
        if (!type.isSummonable()) {
            return null;
        } else if (CACHE.containsKey(type) && CACHE.get(type).world == mc.world) {
            //noinspection unchecked
            return (T) CACHE.get(type);
        } else {
            @SuppressWarnings("ConstantConditions")
            T entity = type.create(mc.world);
            if (entity != null) {
                entity.setSilent(true);
                if (entity instanceof MobEntity) {
                    ((MobEntity) entity).setNoAI(true);
                }
                // The game crashes if this is not there. But it does not crash in our render code
                // but when trying to render a chunk. Very weird.
                if (entity instanceof SnowGolemEntity) {
                    ((SnowGolemEntity) entity).setPumpkinEquipped(false);
                }
                if (entity instanceof WaterMobEntity) {
                    entity.inWater = true;
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
        } else if (predicate.type instanceof EntityTypePredicate.TypePredicate
                && ((EntityTypePredicate.TypePredicate) predicate.type).type != null) {
            entity = getRenderEntity(mc, ((EntityTypePredicate.TypePredicate) predicate.type).type);
        } else if (predicate.type instanceof EntityTypePredicate.TagPredicate
                && ((EntityTypePredicate.TagPredicate) predicate.type).tag != null
                && !((EntityTypePredicate.TagPredicate) predicate.type).tag.getAllElements().isEmpty()) {
            List<EntityType<?>> list = ((EntityTypePredicate.TagPredicate) predicate.type).tag.getAllElements();
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

    public static void renderPlainEntity(Minecraft mc, EntityType<?> type, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        Entity entity = getRenderEntity(mc, type);
        if (entity != null) {
            entity.forceFireTicks(0);
            entity.setSneaking(false);
            entity.setSprinting(false);
            entity.setSwimming(false);
            if (entity instanceof AgeableEntity) {
                ((AgeableEntity) entity).setGrowingAge(10);
            } else if (entity instanceof ZombieEntity) {
                ((ZombieEntity) entity).setChild(false);
            }
            entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
            entity.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
            entity.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
            entity.setItemStackToSlot(EquipmentSlotType.CHEST, ItemStack.EMPTY);
            entity.setItemStackToSlot(EquipmentSlotType.LEGS, ItemStack.EMPTY);
            entity.setItemStackToSlot(EquipmentSlotType.FEET, ItemStack.EMPTY);
            if (entity instanceof CatEntity) {
                ((CatEntity) entity).setCatType(0);
            }
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).swingProgress = 0;
                ((LivingEntity) entity).swingingHand = Hand.MAIN_HAND;
                ((LivingEntity) entity).limbSwing = 0;
                ((LivingEntity) entity).setActiveHand(Hand.MAIN_HAND);
                ((LivingEntity) entity).activeItemStackUseCount = 0;
                ((LivingEntity) entity).hurtTime = 0;
                ((LivingEntity) entity).deathTime = 0;
            }
            if (entity instanceof TameableEntity) {
                ((TameableEntity) entity).setTamed(false);
                ((TameableEntity) entity).setOwnerId(null);
                // setSitting
                ((TameableEntity) entity).func_233687_w_(false);
                // Whoever named the sitting method for th data manger 'sleeping'...
                ((TameableEntity) entity).setSleeping(false);
            }
            if (entity instanceof AbstractHorseEntity) {
                ((AbstractHorseEntity) entity).setHorseTamed(false);
            }
            //noinspection unchecked
            EntityRenderer<Entity> render = (EntityRenderer<Entity>) mc.getRenderManager().renderers.get(entity.getType());
            if (render != null) {
                matrixStack.push();
                AxisAlignedBB bb = entity.getRenderBoundingBox();
                float scale = (float) Math.min(Math.min(WIDTH / bb.getXSize(), HEIGHT / bb.getYSize()), WIDTH / bb.getZSize());
                matrixStack.scale(scale, scale, scale);
                entity.ticksExisted = ClientTickHandler.ticksInGame;
                render.render(entity, 0, 0, matrixStack, buffer, LightTexture.packLight(15, 15));
                matrixStack.pop();
            }
        }
    }
    
    public static void renderEntity(Minecraft mc, EntityPredicate.AndPredicate entityPredicate, MatrixStack matrixStack, IRenderTypeBuffer buffer, EntityTransformation transform) {
        renderEntity(mc, entityPredicate, matrixStack, buffer, transform, DefaultEntityProperties.DEFAULT);
    }
    
    public static void renderEntity(Minecraft mc, EntityPredicate.AndPredicate entityPredicate, MatrixStack matrixStack, IRenderTypeBuffer buffer, EntityTransformation transform, DefaultEntityProperties properties) {
        EntityPredicate predicate = LootUtil.asEntity(entityPredicate);
        Entity entity = getRenderEntity(mc, predicate, properties);
        if (entity == null) {
            matrixStack.push();
            transform.applyForMissing(matrixStack);
            matrixStack.scale(0.7f, 0.7f, 0.7f);
            matrixStack.translate(0, 0.5, 0);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(22.5f));
            matrixStack.translate(0, -0.5, 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees((ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 360));
            matrixStack.translate(-0.5, 0.25, -0.5);
            IVertexBuilder vertex = buffer.getBuffer(RenderType.getSolid());
            //noinspection deprecation
            Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
                    .renderModelBrightnessColor(matrixStack.getLast(), vertex, null,
                            UNKNOWN_ENTITY, 1, 1, 1,
                            LightTexture.packLight(15, 15),
                            OverlayTexture.NO_OVERLAY);
            matrixStack.pop();
        } else {
            entity.forceFireTicks((predicate.flags.onFire == null ? properties.fire : predicate.flags.onFire) ? 10 : 0);
            entity.setSneaking(predicate.flags.sneaking == Boolean.valueOf(true));
            entity.setSprinting(predicate.flags.sprinting == Boolean.valueOf(true));
            entity.setSwimming(predicate.flags.swimming == Boolean.valueOf(true));
            boolean baby = properties.baby;
            if (predicate.flags.baby != null) {
                baby = predicate.flags.baby;
            }
            if (entity instanceof AgeableEntity) {
                ((AgeableEntity) entity).setGrowingAge(baby ? -10 : 10);
            } else if (entity instanceof ZombieEntity) {
                ((ZombieEntity) entity).setChild(baby);
            }
            if (IngredientUtil.fromItemPredicate(predicate.equipment.mainHand).isEmpty() && !properties.getHeld().isEmpty()) {
                entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, properties.getHeld());
            } else {
                entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.mainHand, (IItemProvider) null)));
            }
            entity.setItemStackToSlot(EquipmentSlotType.OFFHAND, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.offHand, (IItemProvider) null)));
            entity.setItemStackToSlot(EquipmentSlotType.HEAD, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.head, (IItemProvider) null)));
            entity.setItemStackToSlot(EquipmentSlotType.CHEST, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.chest, (IItemProvider) null)));
            entity.setItemStackToSlot(EquipmentSlotType.LEGS, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.legs, (IItemProvider) null)));
            entity.setItemStackToSlot(EquipmentSlotType.FEET, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.feet, (IItemProvider) null)));
            if (entity instanceof CatEntity) {
                if (predicate.catType != null) {
                    ((CatEntity) entity).setCatType(CatEntity.TEXTURE_BY_ID.entrySet().stream().filter(entry -> entry.getValue().equals(predicate.catType)).findFirst().map(Map.Entry::getKey).orElse(0));
                } else {
                    ((CatEntity) entity).setCatType((ClientTickHandler.ticksInGame / 10) % CatEntity.TEXTURE_BY_ID.size());
                }
            }
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).swingProgress = properties.swing;
                ((LivingEntity) entity).swingingHand = Hand.MAIN_HAND;
                ((LivingEntity) entity).limbSwing = properties.limbSwing;
                ((LivingEntity) entity).setActiveHand(Hand.MAIN_HAND);
                ((LivingEntity) entity).activeItemStackUseCount = properties.useTick;
                ((LivingEntity) entity).hurtTime = properties.hurtTime;
                ((LivingEntity) entity).deathTime = properties.deathTime;
            }
            if (entity instanceof TameableEntity) {
                ((TameableEntity) entity).setTamed(properties.tamed);
                ((TameableEntity) entity).setOwnerId(properties.tamed ? Util.PLACEHOLDER_UUID : null);
                // setSitting
                ((TameableEntity) entity).func_233687_w_(properties.tamed);
                // Whoever named the sitting method for th data manger 'sleeping'...
                ((TameableEntity) entity).setSleeping(properties.tamed);
            }
            if (entity instanceof AbstractHorseEntity) {
                ((AbstractHorseEntity) entity).setHorseTamed(properties.tamed);
            }
            //noinspection unchecked
            EntityRenderer<Entity> render = (EntityRenderer<Entity>) mc.getRenderManager().renderers.get(entity.getType());
            if (render != null) {
                matrixStack.push();
                transform.applyForEntity(matrixStack);
                float scale = getEntityScale(entity, baby);
                matrixStack.scale(scale, scale, scale);
                if (entity instanceof EnderDragonEntity) {
                    matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
                }
                matrixStack.translate(0, horizontalOffset(entity), 0);
                entity.ticksExisted = ClientTickHandler.ticksInGame;
                render.render(entity, 0, 0, matrixStack, buffer, LightTexture.packLight(15, 15));
                matrixStack.pop();
            }
        }
    }

    public static void addTooltipForEntity(Minecraft mc, List<ITextComponent> tooltip, EntityPredicate.AndPredicate entityPredicate, double absoluteX, double absoluteY, double totalScale, double mouseX, double mouseY) {
        addTooltipForEntity(mc, tooltip, entityPredicate, absoluteX, absoluteY, totalScale, DefaultEntityProperties.DEFAULT, mouseX, mouseY);
    }
    
    public static void addTooltipForEntity(Minecraft mc, List<ITextComponent> tooltip, EntityPredicate.AndPredicate entityPredicate, double absoluteX, double absoluteY, double totalScale, DefaultEntityProperties properties, double mouseX, double mouseY) {
        EntityPredicate predicate = LootUtil.asEntity(entityPredicate);
        Entity entity = getRenderEntity(mc, predicate, properties);
        AxisAlignedBB bb;
        float entityScale;
        if (entity == null) {
            bb = UNKNOWN_ENTITY_AABB;
            entityScale = 0.7f;
        } else {
            boolean baby = properties.baby;
            if (predicate.flags.baby != null) {
                baby = predicate.flags.baby;
            }
            // We also need to set the entity a baby here as it changes the bounding box.
            if (entity instanceof AgeableEntity) {
                ((AgeableEntity) entity).setGrowingAge(baby ? -10 : 10);
            } else if (entity instanceof ZombieEntity) {
                ((ZombieEntity) entity).setChild(baby);
            }
            bb = entity.getRenderBoundingBox();
            entityScale = getEntityScale(entity, baby);
        }
        double scale = totalScale * entityScale;
        // Animal entities often have a head outside the bounding box and a multiplier of 1.6 ist the best match there.
        boolean insideBox = (mouseX > absoluteX - (0.7 * scale * bb.getXSize()) || mouseX > absoluteX - (0.7 * scale * bb.getZSize()))
                && (mouseX < absoluteX + (0.7 * scale * bb.getXSize()) || mouseX < absoluteX + (0.7 * scale * bb.getZSize()))
                && mouseY > (absoluteY + HEIGHT - horizontalTooltipScale(entity) * scale * bb.getYSize() - (horizontalOffset(entity) * scale))
                && mouseY < (absoluteY - (horizontalOffset(entity) * scale));
        if (insideBox) {
            if (properties.forcedType && properties.type != null) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.entity.type.type", properties.type.getName()).mergeStyle(TextFormatting.GOLD));
            } else if (predicate.type instanceof EntityTypePredicate.TypePredicate
                    && ((EntityTypePredicate.TypePredicate) predicate.type).type != null) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.entity.type.type", ((EntityTypePredicate.TypePredicate) predicate.type).type.getName()).mergeStyle(TextFormatting.GOLD));
            } else if (predicate.type instanceof EntityTypePredicate.TagPredicate
                    && ((EntityTypePredicate.TagPredicate) predicate.type).tag != null
                    && !(((EntityTypePredicate.TagPredicate) predicate.type).tag instanceof ITag.INamedTag)) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.entity.type.type", ((ITag.INamedTag<EntityType<?>>) ((EntityTypePredicate.TagPredicate) predicate.type).tag).getName().toString()).mergeStyle(TextFormatting.GOLD));
            } else if (properties.type != null && !properties.typeDisplayOnly) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.entity.type.type", properties.type.getName()).mergeStyle(TextFormatting.GOLD));
            } else if (predicate.catType != null && entity instanceof CatEntity) {
                // Minecraft does not specify a type when setting the catType predicate
                // However it must be a cat to work
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.entity.type.type", EntityType.CAT.getName()).mergeStyle(TextFormatting.GOLD));
            } else {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.any_entity").mergeStyle(TextFormatting.GOLD));
            }
            
            List<IFormattableTextComponent> additional = new ArrayList<>();
            
            TooltipUtil.addEffectValues(additional, predicate.effects);
            TooltipUtil.addDistanceValuesPlayerRelative(additional, predicate.distance);
            TooltipUtil.addLocationValues(additional, predicate.location);
            
            if (predicate.catType != null) {
                additional.add(new TranslationTextComponent("jea.item.tooltip.entity.type.cat", IngredientUtil.rlFile(predicate.catType)));
            }
            
            if (entity != null && BOTANIA_GAIA.equals(entity.getType().getRegistryName())) {
                CompoundNBT nbt = predicate.nbt.tag;
                if (nbt != null && nbt.contains("hardMode")) {
                    additional.add(new TranslationTextComponent("jea.item.tooltip.botania.gaia.hardmode", TooltipUtil.yesNo(nbt.getBoolean("hardMode"))));
                }
            }

            for (IFormattableTextComponent tc : additional) {
                tooltip.add(tc.mergeStyle(TextFormatting.AQUA));
            }
        }
    }

    private static float getEntityScale(Entity entity, boolean baby) {
        AxisAlignedBB bb = entity.getRenderBoundingBox();
        float scale = (float) Math.min(Math.min(WIDTH / bb.getXSize(), HEIGHT / bb.getYSize()), WIDTH / bb.getZSize());
        if (baby) {
            scale *= 0.75f;
        }
        if (entity instanceof ChickenEntity) {
            scale *= 0.6;
        }
        if (entity instanceof PandaEntity) {
            scale *= 1.4;
        }
        if (entity instanceof LlamaEntity) {
            scale *= 0.9;
        }
        if (entity instanceof WitchEntity) {
            scale *= 0.8;
        }
        if (entity instanceof SlimeEntity) {
            scale *= 2.5;
        }
        if (entity instanceof CaveSpiderEntity) {
            scale *= 0.85;
        }
        if (entity instanceof CreeperEntity) {
            scale *= 0.95;
        }
        if (entity instanceof SilverfishEntity) {
            scale *= 0.6;
        }
        if (entity instanceof EndermiteEntity) {
            scale *= 0.7;
        }
        if (entity instanceof EnderDragonEntity) {
            scale *= 2.5;
        }
        if (entity instanceof LivingEntity && ((LivingEntity) entity).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof BannerItem) {
            scale *= 0.6;
        }
        if (entity instanceof CatEntity && ((CatEntity) entity).isEntitySleeping()) {
            // Again someone called the sitting methods sleeping for cats...
            // Sitting cats are really large compared to standing cats so we
            // scale them down a bit.
            scale *= 0.9;
        }
        return scale;
    }
    
    private static float horizontalTooltipScale(Entity entity) {
        // A method to best guess whether this entity has a head that sticks out of the hitbox.
        // In that case the tooltip must be calculated differently
        if (entity instanceof CatEntity && !((CatEntity) entity).isEntitySleeping()) {
            // The sitting methods are called sleeping for cats...
            // Cats are really weird with their bounding box but only if they're not sitting...
            return 0.8f;
        } else if (entity instanceof AnimalEntity && !(entity instanceof LlamaEntity)) {
            // Mostly only animals have their head stick out.
            // Llamas are exceptions
            return 1.6f;
        } else {
            // All other animals are probably fine
            return 1.1f;
        }
    }
    
    private static double horizontalOffset(Entity entity) {
        if (entity instanceof GhastEntity) {
            return 3;
        } else if (entity instanceof SquidEntity) {
            return 1.5;
        } else {
            return 0;
        }
    }
}
