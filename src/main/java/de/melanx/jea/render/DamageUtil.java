package de.melanx.jea.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.LootUtil;
import de.melanx.jea.util.ProjectileUtil;
import de.melanx.jea.util.TooltipUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static de.melanx.jea.api.client.criterion.ICriterionInfo.*;

public class DamageUtil {
    
    public static void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, DamagePredicate predicate, EntityPredicate.Composite targetEntity, boolean sourceIsPlayer, boolean targetIsPlayer) {
        draw(poseStack, buffer, mc, predicate, targetEntity, sourceIsPlayer, targetIsPlayer, null, null);
    }
    
    public static void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, DamagePredicate predicate, EntityPredicate.Composite targetEntity, boolean sourceIsPlayer, boolean targetIsPlayer, @Nullable EntityType<?> forcedSource, @Nullable EntityType<?> forcedTarget) {
        ArrayList<Component> list = new ArrayList<>();
        Triple<EntityInfo, EntityInfo, ItemStack> entities = getInfo(predicate, sourceIsPlayer, targetIsPlayer, list::add);
        EntityInfo source = entities.getLeft();
        EntityInfo target = entities.getMiddle();
        ItemStack projectile = entities.getRight();
        int xValue = RECIPE_WIDTH - 102;
        for (Component text : list) {
            xValue = Math.min(xValue, RECIPE_WIDTH - (mc.font.width(text) + 2));
        }
        int yValue = Math.min(SPACE_TOP + RECIPE_HEIGHT, (SPACE_TOP + RECIPE_HEIGHT + 2) - (list.size() * (mc.font.lineHeight + 2)));
        HealthRender.HeartValues[] hearts = getHearts(predicate);
        if (hearts != null) {
            int amount = 0;
            for (HealthRender.HeartValues values : hearts) {
                amount += values.size();
            }
            yValue -= 10 * Math.ceil(amount / 10d);
            poseStack.pushPose();
            poseStack.translate(xValue, SPACE_TOP + RECIPE_HEIGHT - 12, 0);
            HealthRender.renderHealthBar(poseStack, hearts);
            poseStack.popPose();
        }
        float entityScale = (yValue - SPACE_TOP - 4) / 32f;
        if (forcedSource != null) { source.type = forcedSource; source.forcedType = true; }
        if (forcedTarget != null) { target.type = forcedTarget; target.forcedType = true; }
        if (!projectile.isEmpty()) {
            poseStack.pushPose();
            //noinspection IntegerDivisionInFloatingPointContext
            poseStack.translate(RECIPE_WIDTH / 2, SPACE_TOP + 30, 0);
            poseStack.translate(7 - ((ClientTickHandler.ticksInGame + mc.getFrameTime()) % 20), 0, 0);
            ProjectileUtil.rotateCenter(poseStack, projectile, Vector3f.ZP.rotationDegrees(180));
            ProjectileUtil.renderProjectile(poseStack, projectile);
            poseStack.popPose();
        }
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 30, yValue - 2, 0);
        JeaRender.normalize(poseStack);
        source.render(poseStack, buffer, mc, LootUtil.asLootPredicate(predicate.sourceEntity), JeaRender.entityRenderSide(true, entityScale));
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(30, yValue - 2, 0);
        JeaRender.normalize(poseStack);
        target.render(poseStack, buffer, mc, targetEntity, JeaRender.entityRenderSide(false, entityScale));
        poseStack.popPose();
        for (Component text : list) {
            mc.font.draw(poseStack, text, xValue, yValue, 0x000000);
            yValue += (2 + mc.font.lineHeight);
        }
    }
    
    public static void addTooltip(List<Component> tooltip, Minecraft mc, DamagePredicate predicate, EntityPredicate.Composite targetEntity, boolean sourceIsPlayer, boolean targetIsPlayer, double mouseX, double mouseY) {
        addTooltip(tooltip, mc, predicate, targetEntity, sourceIsPlayer, targetIsPlayer, null, null, mouseX, mouseY);
    }
    
    public static void addTooltip(List<Component> tooltip, Minecraft mc, DamagePredicate predicate, EntityPredicate.Composite targetEntity, boolean sourceIsPlayer, boolean targetIsPlayer, @Nullable EntityType<?> forcedSource, @Nullable EntityType<?> forcedTarget, double mouseX, double mouseY) {
        ArrayList<Component> list = new ArrayList<>();
        Triple<EntityInfo, EntityInfo, ItemStack> entities = getInfo(predicate, sourceIsPlayer, targetIsPlayer, list::add);
        EntityInfo source = entities.getLeft();
        EntityInfo target = entities.getMiddle();
        int xValue = RECIPE_WIDTH - 102;
        for (Component text : list) {
            xValue = Math.min(xValue, RECIPE_WIDTH - (mc.font.width(text) + 2));
        }
        int yValue = (SPACE_TOP + RECIPE_HEIGHT) - (list.size() * (mc.font.lineHeight + 2));
        HealthRender.HeartValues[] hearts = getHearts(predicate);
        if (hearts != null && HealthRender.isInHealthBarBox(10, SPACE_TOP + 30, mouseX, mouseY, hearts)) {
            int amount = 0;
            for (HealthRender.HeartValues values : hearts) {
                amount += values.size();
            }
            yValue -= 10 * Math.ceil(amount / 10d);
            if (!predicate.dealtDamage.isAny()) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.damage.dealt", IngredientUtil.text(predicate.dealtDamage)).withStyle(ChatFormatting.RED));
            }
            if (!predicate.takenDamage.isAny()) {
                tooltip.add(new TranslatableComponent("jea.item.tooltip.damage.taken", IngredientUtil.text(predicate.takenDamage)).withStyle(ChatFormatting.RED));
            }
        }
        float entityScale = (yValue - SPACE_TOP - 4) / 32f;
        if (forcedSource != null) { source.type = forcedSource; source.forcedType = true; }
        if (forcedTarget != null) { target.type = forcedTarget; target.forcedType = true; }
        source.addTooltip(tooltip, mc, LootUtil.asLootPredicate(predicate.sourceEntity), RECIPE_WIDTH - 30, yValue - 2, JeaRender.normalScale(entityScale), mouseX, mouseY);
        target.addTooltip(tooltip, mc, targetEntity, 30, yValue - 2, JeaRender.normalScale(entityScale), mouseX, mouseY);
    }
    
    private static HealthRender.HeartValues[] getHearts(DamagePredicate predicate) {
        double dealt = IngredientUtil.getExampleValue(predicate.dealtDamage).orElse(0d);
        double taken = IngredientUtil.getExampleValue(predicate.takenDamage).orElse(0d);
        double blocked = Math.max(0, dealt - taken);
        if (Math.round(blocked) +  Math.round(taken) > 0) {
            return new HealthRender.HeartValues[]{
                    HealthRender.HeartEffect.NORMAL.create((int) Math.round(blocked), (int) Math.round(taken), 10)
            };
        } else {
            return null;
        }
    }
    
    private static Triple<EntityInfo, EntityInfo, ItemStack> getInfo(DamagePredicate predicate, boolean sourceIsPlayer, boolean targetIsPlayer, Consumer<Component> lines) {
        EntityInfo source = new EntityInfo();
        EntityInfo target = new EntityInfo();
        ItemStack projectile = ItemStack.EMPTY;
        target.type = EntityType.ZOMBIE; // Poor zombies are the default target
        target.hurt = true;
        if (predicate.blocked != null) {
            if (lines != null) lines.accept(new TranslatableComponent(predicate.blocked ? "jea.item.tooltip.damage.blocked" : "jea.item.tooltip.damage.non_blocked"));
            if (predicate.blocked && target.held.isEmpty()) {
                target.held = new ItemStack(Items.SHIELD);
                target.usingStack = true;
                target.swing = 0.73f;
                target.hurt = false;
            }
        }
        if (predicate.type.isFire != null) {
            if (lines != null) lines.accept(new TranslatableComponent("jea.item.tooltip.damage.fire", TooltipUtil.yesNo(predicate.type.isFire)));
            if (predicate.type.isFire) {
                target.fire = true;
                if (source.type == null) {
                    source.type = EntityType.BLAZE;
                }
            }
        }
        if (predicate.type.isProjectile != null) {
            if (lines != null) lines.accept(new TranslatableComponent("jea.item.tooltip.damage.projectile", TooltipUtil.yesNo(predicate.type.isProjectile)));
            if (predicate.type.isProjectile) {
                if (source.type == null) {
                    source.type = EntityType.SKELETON;
                    source.held = ProjectileUtil.getHeldItemForProjectile(predicate);
                    projectile = JeaRender.cycle(ProjectileUtil.getProjectileStack(Minecraft.getInstance(), predicate.type.directEntity));
                    if (source.held.getItem() == Items.TRIDENT) {
                        target.type = EntityType.DROWNED;
                    }
                    source.usingStack = true;
                }
            }
        }
        if (predicate.type.isExplosion != null) {
            if (lines != null) lines.accept(new TranslatableComponent("jea.item.tooltip.damage.explosion", TooltipUtil.yesNo(predicate.type.isExplosion)));
            if (predicate.type.isExplosion) {
                if (source.type == null) {
                    source.type = EntityType.CREEPER;
                } else if (source.held.isEmpty()) {
                    source.held = new ItemStack(Items.TNT);
                    float swingTicker = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getFrameTime()) % 30;
                    if (swingTicker <= 6) {
                        source.swing = swingTicker / 6f;
                    }
                }
            }
        }
        if (predicate.type.isMagic != null) {
            if (lines != null) lines.accept(new TranslatableComponent("jea.item.tooltip.damage.magic", TooltipUtil.yesNo(predicate.type.isMagic)));
            if (predicate.type.isMagic) {
                if (source.type == null) {
                    source.type = EntityType.WITCH;
                }
                if (source.held.isEmpty()) {
                    source.held = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.POISON);
                    if (source.type != EntityType.WITCH) {
                        float swingTicker = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getFrameTime()) % 30;
                        if (swingTicker <= 6) {
                            source.swing = swingTicker / 6f;
                        }
                    }
                }
            }
        }
        if (predicate.type.isLightning != null) {
            if (lines != null) lines.accept(new TranslatableComponent("jea.item.tooltip.damage.lightning", TooltipUtil.yesNo(predicate.type.isLightning)));
        }
        if (predicate.type.bypassesArmor != null) {
            if (lines != null) lines.accept(new TranslatableComponent("jea.item.tooltip.damage.bypass_armor", TooltipUtil.yesNo(predicate.type.bypassesArmor)));
        }
        if (predicate.type.bypassesMagic != null) {
            if (lines != null) lines.accept(new TranslatableComponent("jea.item.tooltip.damage.bypass_magic", TooltipUtil.yesNo(predicate.type.bypassesMagic)));
        }
        if (predicate.type.bypassesInvulnerability != null) {
            if (lines != null) lines.accept(new TranslatableComponent("jea.item.tooltip.damage.bypass_invulnerability", TooltipUtil.yesNo(predicate.type.bypassesInvulnerability)));
        }
        if (sourceIsPlayer) {
            if (source.held.isEmpty()) {
                source.held = new ItemStack(Items.IRON_SWORD);
            }
            source.type = EntityType.PLAYER;
        }
        if (targetIsPlayer) {
            target.type = EntityType.PLAYER;
        }
        return Triple.of(source, target, projectile);
    }
    
    private static class EntityInfo {
        
        @Nullable
        public EntityType<?> type = null;
        public ItemStack held = ItemStack.EMPTY;
        public boolean usingStack = false;
        public boolean fire = false;
        public boolean hurt = false;
        public float swing = 0;
        public boolean forcedType = false;
        
        public void render(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, @Nullable EntityPredicate.Composite entity, EntityTransformation transformation) {
            if (this.type != null && this.type == EntityType.PLAYER) {
                transformation.applyForEntity(poseStack);
                SteveRender.defaultPose(mc);
                SteveRender.use(this.usingStack ? this.held.getUseDuration() : 0, InteractionHand.MAIN_HAND);
                SteveRender.swing(this.swing, InteractionHand.MAIN_HAND);
                SteveRender.fireTicks(this.fire ? 10 : 0);
                SteveRender.hurtTime(this.hurt ? 10 : 0);
                SteveRender.setEquipmentHand(mc, this.held);
                SteveRender.renderSteve(mc, poseStack, buffer);
            } else {
                DefaultEntityProperties properties = new DefaultEntityProperties(this.type, true, this.fire, false, this.held, this.swing + 0.5f, 0, this.usingStack ? 1 : 0, this.hurt ? 10 : 0, 0, false, this.forcedType);
                RenderEntityCache.renderEntity(mc, entity == null ? EntityPredicate.Composite.ANY : entity, poseStack, buffer, transformation, properties);
            }
        }
        
        public void addTooltip(List<Component> tooltip, Minecraft mc, @Nullable EntityPredicate.Composite entity, double absoluteX, double absoluteY, double totalScale, double mouseX, double mouseY) {
            if (this.type != EntityType.PLAYER) {
                DefaultEntityProperties properties = new DefaultEntityProperties(this.type, true, this.fire, false, this.held, this.swing, 0, this.usingStack ? this.held.getUseDuration() : 0, this.hurt ? 10 : 0, 0, false, this.forcedType);
                RenderEntityCache.addTooltipForEntity(mc, tooltip, entity == null ? EntityPredicate.Composite.ANY : entity, absoluteX, absoluteY, totalScale, properties, mouseX, mouseY);
            }
        }
    }
}
