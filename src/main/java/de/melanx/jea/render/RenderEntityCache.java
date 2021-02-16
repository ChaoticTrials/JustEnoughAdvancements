package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.melanx.jea.LootUtil;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.TooltipUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.*;

public class RenderEntityCache {
    
    private static final double HEIGHT = 1.8;
    private static final double WIDTH = 0.8;
    private static final AxisAlignedBB UNKNOWN_ENTITY_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    
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
            }
            CACHE.put(type, entity);
            return entity;
        }
    }

    @Nullable
    public static Entity getRenderEntity(Minecraft mc, EntityPredicate predicate) {
        Entity entity = null;
        if (predicate.type instanceof EntityTypePredicate.TypePredicate
                && ((EntityTypePredicate.TypePredicate) predicate.type).type != null) {
            entity = getRenderEntity(mc, ((EntityTypePredicate.TypePredicate) predicate.type).type);
        } else if (predicate.type instanceof EntityTypePredicate.TagPredicate
                && ((EntityTypePredicate.TagPredicate) predicate.type).tag != null
                && !((EntityTypePredicate.TagPredicate) predicate.type).tag.getAllElements().isEmpty()) {
            List<EntityType<?>> list = ((EntityTypePredicate.TagPredicate) predicate.type).tag.getAllElements();
            entity = getRenderEntity(mc, list.get((ClientTickHandler.ticksInGame / 60) % list.size()));
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
            //noinspection unchecked
            EntityRenderer<Entity> render = (EntityRenderer<Entity>) mc.getRenderManager().renderers.get(entity.getType());
            if (render != null) {
                AxisAlignedBB bb = entity.getRenderBoundingBox();
                float scale = (float) Math.min(Math.min(WIDTH / bb.getXSize(), HEIGHT / bb.getYSize()), WIDTH / bb.getZSize());
                matrixStack.scale(scale, scale, scale);
                entity.ticksExisted = ClientTickHandler.ticksInGame;
                render.render(entity, 0, 0, matrixStack, buffer, LightTexture.packLight(15, 15));
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
        if (entity instanceof LlamaEntity) {
            scale *= 0.9;
        }
        if (entity instanceof PandaEntity) {
            scale *= 1.4;
        }
        return scale;
    }
    
    public static void renderEntity(Minecraft mc, EntityPredicate.AndPredicate entityPredicate, MatrixStack matrixStack, IRenderTypeBuffer buffer, EntityTransformation transform) {
        renderEntity(mc, entityPredicate, matrixStack, buffer, transform, DefaultEntityProperties.DEFAULT);
    }
    
    public static void renderEntity(Minecraft mc, EntityPredicate.AndPredicate entityPredicate, MatrixStack matrixStack, IRenderTypeBuffer buffer, EntityTransformation transform, DefaultEntityProperties properties) {
        EntityPredicate predicate = LootUtil.asEntity(entityPredicate);
        Entity entity = getRenderEntity(mc, predicate);
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
                            SpecialModels.getModel(SpecialModels.UNKNOWN_ENTITY),
                            1, 1, 1,
                            LightTexture.packLight(15, 15),
                            OverlayTexture.NO_OVERLAY);
            matrixStack.pop();
        } else {
            entity.forceFireTicks((predicate.flags.onFire == null ? properties.fire : predicate.flags.onFire) ? 1 : 0);
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
            entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, JeaRender.cycle(IngredientUtil.fromItemPredicate(predicate.equipment.mainHand, (IItemProvider) null)));
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
            //noinspection unchecked
            EntityRenderer<Entity> render = (EntityRenderer<Entity>) mc.getRenderManager().renderers.get(entity.getType());
            if (render != null) {
                matrixStack.push();
                transform.applyForEntity(matrixStack);
                float scale = getEntityScale(entity, baby);
                matrixStack.scale(scale, scale, scale);
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
        Entity entity = getRenderEntity(mc, predicate);
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
        boolean insideBox = (mouseX > absoluteX - (0.7 * scale * bb.getXSize()) || mouseX > absoluteX - (0.7 * scale * bb.getZSize()))
                && (mouseX < absoluteX + (0.7 * scale * bb.getXSize()) || mouseX < absoluteX + (0.7 * scale * bb.getZSize()))
                && mouseY > absoluteY + HEIGHT - (1.8 * scale * bb.getYSize())
                && mouseY < absoluteY;
        if (insideBox) {
            if (predicate.type instanceof EntityTypePredicate.TypePredicate
                    && ((EntityTypePredicate.TypePredicate) predicate.type).type != null) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.entity.type.type", ((EntityTypePredicate.TypePredicate) predicate.type).type.getName()).mergeStyle(TextFormatting.GOLD));
            } else if (predicate.type instanceof EntityTypePredicate.TagPredicate
                    && ((EntityTypePredicate.TagPredicate) predicate.type).tag != null
                    && !(((EntityTypePredicate.TagPredicate) predicate.type).tag instanceof ITag.INamedTag)) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.entity.type.type", ((ITag.INamedTag<EntityType<?>>) ((EntityTypePredicate.TagPredicate) predicate.type).tag).getName().toString()).mergeStyle(TextFormatting.GOLD));
            } else {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.any_entity").mergeStyle(TextFormatting.GOLD));
            }
            
            List<IFormattableTextComponent> additional = new ArrayList<>();
            
            TooltipUtil.addEffectValues(additional, predicate.effects);
            TooltipUtil.addDistanceValues(additional, predicate.distance);
            TooltipUtil.addLocationValues(additional, predicate.location);
            
            for (IFormattableTextComponent tc : additional) {
                tooltip.add(tc.mergeStyle(TextFormatting.AQUA));
            }
        }
    }
}
