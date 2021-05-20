package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.util.LootUtil;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.ProjectileUtil;
import de.melanx.jea.util.TooltipUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.advancements.criterion.DamagePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static de.melanx.jea.api.client.criterion.ICriterionInfo.*;

public class DamageUtil {
    
    public static void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, DamagePredicate predicate, EntityPredicate.AndPredicate targetEntity, boolean sourceIsPlayer, boolean targetIsPlayer) {
        draw(matrixStack, buffer, mc, predicate, targetEntity, sourceIsPlayer, targetIsPlayer, null, null);
    }
    
    public static void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, DamagePredicate predicate, EntityPredicate.AndPredicate targetEntity, boolean sourceIsPlayer, boolean targetIsPlayer, @Nullable EntityType<?> forcedSource, @Nullable EntityType<?> forcedTarget) {
        ArrayList<ITextComponent> list = new ArrayList<>();
        Triple<EntityInfo, EntityInfo, ItemStack> entities = getInfo(predicate, sourceIsPlayer, targetIsPlayer, list::add);
        EntityInfo source = entities.getLeft();
        EntityInfo target = entities.getMiddle();
        ItemStack projectile = entities.getRight();
        int xValue = RECIPE_WIDTH - 102;
        for (ITextComponent text : list) {
            xValue = Math.min(xValue, RECIPE_WIDTH - (mc.fontRenderer.getStringPropertyWidth(text) + 2));
        }
        int yValue = Math.min(SPACE_TOP + RECIPE_HEIGHT, (SPACE_TOP + RECIPE_HEIGHT + 2) - (list.size() * (mc.fontRenderer.FONT_HEIGHT + 2)));
        HealthRender.HeartValues[] hearts = getHearts(predicate);
        if (hearts != null) {
            int amount = 0;
            for (HealthRender.HeartValues values : hearts) {
                amount += values.size();
            }
            yValue -= 10 * Math.ceil(amount / 10d);
            matrixStack.push();
            matrixStack.translate(xValue, SPACE_TOP + RECIPE_HEIGHT - 12, 0);
            HealthRender.renderHealthBar(matrixStack, hearts);
            matrixStack.pop();
        }
        float entityScale = (yValue - SPACE_TOP - 4) / 32f;
        if (forcedSource != null) { source.type = forcedSource; source.forcedType = true; }
        if (forcedTarget != null) { target.type = forcedTarget; target.forcedType = true; }
        if (!projectile.isEmpty()) {
            matrixStack.push();
            //noinspection IntegerDivisionInFloatingPointContext
            matrixStack.translate(RECIPE_WIDTH / 2, SPACE_TOP + 30, 0);
            matrixStack.translate(7 - ((ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 20), 0, 0);
            ProjectileUtil.rotateCenter(matrixStack, projectile, Vector3f.ZP.rotationDegrees(180));
            ProjectileUtil.renderProjectile(matrixStack, projectile);
            matrixStack.pop();
        }
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 30, yValue - 2, 0);
        JeaRender.normalize(matrixStack);
        source.render(matrixStack, buffer, mc, LootUtil.asLootPredicate(predicate.sourceEntity), JeaRender.entityRenderSide(true, entityScale));
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(30, yValue - 2, 0);
        JeaRender.normalize(matrixStack);
        target.render(matrixStack, buffer, mc, targetEntity, JeaRender.entityRenderSide(false, entityScale));
        matrixStack.pop();
        for (ITextComponent text : list) {
            mc.fontRenderer.drawText(matrixStack, text, xValue, yValue, 0x000000);
            yValue += (2 + mc.fontRenderer.FONT_HEIGHT);
        }
    }
    
    public static void addTooltip(List<ITextComponent> tooltip, Minecraft mc, DamagePredicate predicate, EntityPredicate.AndPredicate targetEntity, boolean sourceIsPlayer, boolean targetIsPlayer, double mouseX, double mouseY) {
        addTooltip(tooltip, mc, predicate, targetEntity, sourceIsPlayer, targetIsPlayer, null, null, mouseX, mouseY);
    }
    
    public static void addTooltip(List<ITextComponent> tooltip, Minecraft mc, DamagePredicate predicate, EntityPredicate.AndPredicate targetEntity, boolean sourceIsPlayer, boolean targetIsPlayer, @Nullable EntityType<?> forcedSource, @Nullable EntityType<?> forcedTarget, double mouseX, double mouseY) {
        ArrayList<ITextComponent> list = new ArrayList<>();
        Triple<EntityInfo, EntityInfo, ItemStack> entities = getInfo(predicate, sourceIsPlayer, targetIsPlayer, list::add);
        EntityInfo source = entities.getLeft();
        EntityInfo target = entities.getMiddle();
        int xValue = RECIPE_WIDTH - 102;
        for (ITextComponent text : list) {
            xValue = Math.min(xValue, RECIPE_WIDTH - (mc.fontRenderer.getStringPropertyWidth(text) + 2));
        }
        int yValue = (SPACE_TOP + RECIPE_HEIGHT) - (list.size() * (mc.fontRenderer.FONT_HEIGHT + 2));
        HealthRender.HeartValues[] hearts = getHearts(predicate);
        if (hearts != null && HealthRender.isInHealthBarBox(10, SPACE_TOP + 30, mouseX, mouseY, hearts)) {
            int amount = 0;
            for (HealthRender.HeartValues values : hearts) {
                amount += values.size();
            }
            yValue -= 10 * Math.ceil(amount / 10d);
            if (!predicate.dealt.isUnbounded()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.damage.dealt", IngredientUtil.text(predicate.dealt)).mergeStyle(TextFormatting.RED));
            }
            if (!predicate.taken.isUnbounded()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.damage.taken", IngredientUtil.text(predicate.taken)).mergeStyle(TextFormatting.RED));
            }
        }
        float entityScale = (yValue - SPACE_TOP - 4) / 32f;
        if (forcedSource != null) { source.type = forcedSource; source.forcedType = true; }
        if (forcedTarget != null) { target.type = forcedTarget; target.forcedType = true; }
        source.addTooltip(tooltip, mc, LootUtil.asLootPredicate(predicate.sourceEntity), RECIPE_WIDTH - 30, yValue - 2, JeaRender.normalScale(entityScale), mouseX, mouseY);
        target.addTooltip(tooltip, mc, targetEntity, 30, yValue - 2, JeaRender.normalScale(entityScale), mouseX, mouseY);
    }
    
    private static HealthRender.HeartValues[] getHearts(DamagePredicate predicate) {
        float dealt = IngredientUtil.getExampleValue(predicate.dealt).orElse(0f);
        float taken = IngredientUtil.getExampleValue(predicate.taken).orElse(0f);
        float blocked = Math.max(0, dealt - taken);
        if (Math.round(blocked) +  Math.round(taken) > 0) {
            return new HealthRender.HeartValues[]{
                    HealthRender.HeartEffect.NORMAL.create(Math.round(blocked), Math.round(taken), 10)
            };
        } else {
            return null;
        }
    }
    
    private static Triple<EntityInfo, EntityInfo, ItemStack> getInfo(DamagePredicate predicate, boolean sourceIsPlayer, boolean targetIsPlayer, Consumer<ITextComponent> lines) {
        EntityInfo source = new EntityInfo();
        EntityInfo target = new EntityInfo();
        ItemStack projectile = ItemStack.EMPTY;
        target.type = EntityType.ZOMBIE; // Poor zombies are the default target
        target.hurt = true;
        if (predicate.blocked != null) {
            if (lines != null) lines.accept(new TranslationTextComponent(predicate.blocked ? "jea.item.tooltip.damage.blocked" : "jea.item.tooltip.damage.non_blocked"));
            if (predicate.blocked && target.held.isEmpty()) {
                target.held = new ItemStack(Items.SHIELD);
                target.usingStack = true;
                target.swing = 0.73f;
                target.hurt = false;
            }
        }
        if (predicate.type.isFire != null) {
            if (lines != null) lines.accept(new TranslationTextComponent("jea.item.tooltip.damage.fire", TooltipUtil.yesNo(predicate.type.isFire)));
            if (predicate.type.isFire) {
                target.fire = true;
                if (source.type == null) {
                    source.type = EntityType.BLAZE;
                }
            }
        }
        if (predicate.type.isProjectile != null) {
            if (lines != null) lines.accept(new TranslationTextComponent("jea.item.tooltip.damage.projectile", TooltipUtil.yesNo(predicate.type.isProjectile)));
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
            if (lines != null) lines.accept(new TranslationTextComponent("jea.item.tooltip.damage.explosion", TooltipUtil.yesNo(predicate.type.isExplosion)));
            if (predicate.type.isExplosion) {
                if (source.type == null) {
                    source.type = EntityType.CREEPER;
                } else if (source.held.isEmpty()) {
                    source.held = new ItemStack(Items.TNT);
                    float swingTicker = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()) % 30;
                    if (swingTicker <= 6) {
                        source.swing = swingTicker / 6f;
                    }
                }
            }
        }
        if (predicate.type.isMagic != null) {
            if (lines != null) lines.accept(new TranslationTextComponent("jea.item.tooltip.damage.magic", TooltipUtil.yesNo(predicate.type.isMagic)));
            if (predicate.type.isMagic) {
                if (source.type == null) {
                    source.type = EntityType.WITCH;
                }
                if (source.held.isEmpty()) {
                    source.held = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.POISON);
                    if (source.type != EntityType.WITCH) {
                        float swingTicker = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()) % 30;
                        if (swingTicker <= 6) {
                            source.swing = swingTicker / 6f;
                        }
                    }
                }
            }
        }
        if (predicate.type.isLightning != null) {
            if (lines != null) lines.accept(new TranslationTextComponent("jea.item.tooltip.damage.lightning", TooltipUtil.yesNo(predicate.type.isLightning)));
        }
        if (predicate.type.bypassesArmor != null) {
            if (lines != null) lines.accept(new TranslationTextComponent("jea.item.tooltip.damage.bypass_armor", TooltipUtil.yesNo(predicate.type.bypassesArmor)));
        }
        if (predicate.type.bypassesMagic != null) {
            if (lines != null) lines.accept(new TranslationTextComponent("jea.item.tooltip.damage.bypass_magic", TooltipUtil.yesNo(predicate.type.bypassesMagic)));
        }
        if (predicate.type.bypassesInvulnerability != null) {
            if (lines != null) lines.accept(new TranslationTextComponent("jea.item.tooltip.damage.bypass_invulnerability", TooltipUtil.yesNo(predicate.type.bypassesInvulnerability)));
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
        
        public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, @Nullable EntityPredicate.AndPredicate entity, EntityTransformation transformation) {
            if (this.type != null && this.type == EntityType.PLAYER) {
                // Minecraft renders trident mirrored so we rotate steve and turn his head
                boolean rotateForTrident = this.usingStack && this.held.getItem() == Items.TRIDENT;
                transformation.applyForEntity(matrixStack);
                if (rotateForTrident) matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
                SteveRender.defaultPose(mc);
                if (rotateForTrident) SteveRender.rotationHead(180, 0);
                SteveRender.use(this.usingStack ? this.held.getUseDuration() : 0, Hand.MAIN_HAND);
                SteveRender.swing(this.swing, Hand.MAIN_HAND);
                SteveRender.fireTicks(this.fire ? 10 : 0);
                SteveRender.hurtTime(this.hurt ? 10 : 0);
                SteveRender.setEquipmentHand(mc, this.held);
                SteveRender.renderSteve(mc, matrixStack, buffer);
            } else {
                DefaultEntityProperties properties = new DefaultEntityProperties(this.type, true, this.fire, false, this.held, this.swing + 0.5f, 0, this.usingStack ? 1 : 0, this.hurt ? 10 : 0, 0, false, this.forcedType);
                RenderEntityCache.renderEntity(mc, entity == null ? EntityPredicate.AndPredicate.ANY_AND : entity, matrixStack, buffer, transformation, properties);
            }
        }
        
        public void addTooltip(List<ITextComponent> tooltip, Minecraft mc, @Nullable EntityPredicate.AndPredicate entity, double absoluteX, double absoluteY, double totalScale, double mouseX, double mouseY) {
            if (this.type != EntityType.PLAYER) {
                DefaultEntityProperties properties = new DefaultEntityProperties(this.type, true, this.fire, false, this.held, this.swing, 0, this.usingStack ? this.held.getUseDuration() : 0, this.hurt ? 10 : 0, 0, false, this.forcedType);
                RenderEntityCache.addTooltipForEntity(mc, tooltip, entity == null ? EntityPredicate.AndPredicate.ANY_AND : entity, absoluteX, absoluteY, totalScale, properties, mouseX, mouseY);
            }
        }
    }
}
