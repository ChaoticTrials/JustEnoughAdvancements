package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;

public class HealthRender {
    
    public static void renderHealthBar(MatrixStack matrixStack, HeartValues... values) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int x = 0;
        int y = 0;
        for (HeartValues hearts : values) {
            for (int i = 0; i < hearts.size(); i++) {
                hearts.drawHeart(matrixStack, i, x * 10, -(y * 10));
                x += 1;
                if (x >= 10) {
                    x = 0;
                    y += 1;
                }
            }
        }
        RenderSystem.disableBlend();
        matrixStack.pop();
    }
    
    public static boolean isInHealthBarBox(int x, int y, double mouseX, double mouseY, HeartValues... values) {
        int amount = 0;
        for (HeartValues hearts : values) {
            amount += hearts.size();
        }
        //noinspection IntegerDivisionInFloatingPointContext
        if (amount >= 10 && mouseX >= x && mouseX <= x + 99 && mouseY <= y + 9 && mouseY >= y - (10 * ((amount / 10) - 1))) {
            return true;
        }
        //noinspection IntegerDivisionInFloatingPointContext
        if (amount % 10 > 0 && mouseX >= x && mouseX <= x + ((10 * (amount % 10)) - 1) && y >= mouseY + (10 * ((amount / 10) - 1)) && y <= mouseY + (10 * (amount / 10))) {
            return true;
        }
        return false;
    }
    
    public enum HeartEffect {
        NORMAL(52, true),
        POISON(88, true),
        WITHER(124, true),
        ABSORPTION(160, false);

        private static IDrawableStatic background;
        private static IDrawableStatic backgroundDamaging;
        
        private final int xValue;
        private final boolean hasDamaging;
        
        private IDrawableStatic normal;
        private IDrawableStatic half;
        private IDrawableStatic damaging;
        private IDrawableStatic damagingHalf;
        
        HeartEffect(int xValue, boolean hasDamaging) {
            this.xValue = xValue;
            this.hasDamaging = hasDamaging;
        }

        public static void init(IGuiHelper guiHelper) {
            background = guiHelper.createDrawable(JustEnoughAdvancementsJEIPlugin.ICONS_TEXTURE, 16, 0, 9, 9);
            backgroundDamaging = guiHelper.createDrawable(JustEnoughAdvancementsJEIPlugin.ICONS_TEXTURE, 25, 0, 9, 9);
            for (HeartEffect heart : values()) {
                heart.normal = guiHelper.createDrawable(JustEnoughAdvancementsJEIPlugin.ICONS_TEXTURE, heart.xValue, 0, 9, 9);
                heart.half = guiHelper.createDrawable(JustEnoughAdvancementsJEIPlugin.ICONS_TEXTURE, heart.xValue + 9, 0, 9, 9);
                if (heart.hasDamaging) {
                    heart.damaging = guiHelper.createDrawable(JustEnoughAdvancementsJEIPlugin.ICONS_TEXTURE, heart.xValue + 18, 0, 9, 9);
                    heart.damagingHalf = guiHelper.createDrawable(JustEnoughAdvancementsJEIPlugin.ICONS_TEXTURE, heart.xValue + 27, 0, 9, 9);
                } else {
                    heart.damaging = heart.normal;
                    heart.damagingHalf = heart.half;
                }
            }
        }

        public HeartValues create(int health, int damaging, int minHearts) {
            return new HeartValues(this, health, damaging, minHearts);
        }
    }
    
    public static class HeartValues {

        public final HeartEffect heart;
        public final int health;
        public final int damaging;
        public final int minHearts;

        private HeartValues(HeartEffect heart, int health, int damaging, int minHearts) {
            this.heart = heart;
            this.health = health;
            this.damaging = damaging;
            this.minHearts = minHearts;
        }
        
        public int size() {
            return Math.max(this.minHearts, (int) Math.ceil((this.health + this.damaging) / 2f));
        }
        
        public void drawHeart(MatrixStack matrixStack, int idx, int x, int y) {
            if (2 * (idx + 1) <= this.health) {
                HeartEffect.background.draw(matrixStack, x, y);
                matrixStack.translate(0, 0, 10);
                this.heart.normal.draw(matrixStack, x, y);
                matrixStack.translate(0, 0, -10);
            } else if ((2 * (idx + 1)) - 1 <= this.health) {
                if (this.damaging > 0) {
                    HeartEffect.background.draw(matrixStack, x, y, 0, 0, 0, 4);
                    HeartEffect.backgroundDamaging.draw(matrixStack, x, y, 0, 0, 5, 0);
                    matrixStack.translate(0, 0, 10);
                    this.heart.half.draw(matrixStack, x, y);
                    this.heart.damaging.draw(matrixStack, x, y, 0, 0, 5, 0);
                    matrixStack.translate(0, 0, -10);
                } else {
                    HeartEffect.background.draw(matrixStack, x, y, 0, 0, 0, 0);
                    matrixStack.translate(0, 0, 10);
                    this.heart.half.draw(matrixStack, x, y);
                    matrixStack.translate(0, 0, -10);
                }
            } else if (2 * (idx + 1) <= this.health + this.damaging) {
                HeartEffect.backgroundDamaging.draw(matrixStack, x, y);
                matrixStack.translate(0, 0, 10);
                this.heart.damaging.draw(matrixStack, x, y);
                matrixStack.translate(0, 0, -10);
            } else if ((2 * (idx + 1)) - 1 <= this.health + this.damaging) {
                HeartEffect.backgroundDamaging.draw(matrixStack, x, y, 0, 0, 0, 0);
                matrixStack.translate(0, 0, 10);
                this.heart.damagingHalf.draw(matrixStack, x, y, 0, 0, 0, 0);
                matrixStack.translate(0, 0, -10);
            } else {
                HeartEffect.background.draw(matrixStack, x, y, 0, 0, 0, 0);
            }
        }
    }
}
