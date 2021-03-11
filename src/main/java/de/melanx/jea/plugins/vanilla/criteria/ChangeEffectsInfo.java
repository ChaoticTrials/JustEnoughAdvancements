package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.RenderMisc;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.EffectsChangedTrigger;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.Map;

public class ChangeEffectsInfo implements ICriterionInfo<EffectsChangedTrigger.Instance> {

    @Override
    public Class<EffectsChangedTrigger.Instance> criterionClass() {
        return EffectsChangedTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, EffectsChangedTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, EffectsChangedTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, EffectsChangedTrigger.Instance instance, double mouseX, double mouseY) {
        int paddingX;
        int paddingY;
        int rows;
        int columns;
        float scale;
        int size;
        if (instance.effects.effects.size() > 18) {
            paddingX = 7;
            paddingY = 11;
            rows = 4;
            columns = 8;
            scale = 0.75f;
            size = 15;
        } else {
            paddingX = 9;
            paddingY = 12;
            rows = 3;
            columns = 6;
            scale = 1;
            size = 20;
        }
        int x = 0;
        int y = 0;
        for (Map.Entry<Effect, MobEffectsPredicate.InstancePredicate> effect : instance.effects.effects.entrySet()) {
            matrixStack.push();
            matrixStack.translate(paddingX + (x * (size + 2)), SPACE_TOP + paddingY + (y * (size + 2)), 0);
            matrixStack.scale(scale, scale, 1);
            RenderMisc.renderMobEffect(matrixStack, mc, effect.getKey());
            matrixStack.pop();
            if (x < (columns - 1)) { x += 1; } else if (y < (rows - 1)) { x = 0; y += 1; } else { break; }
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, EffectsChangedTrigger.Instance instance, double mouseX, double mouseY) {
        int paddingX;
        int paddingY;
        int rows;
        int columns;
        int size;
        if (instance.effects.effects.size() > 18) {
            paddingX = 7;
            paddingY = 11;
            rows = 4;
            columns = 8;
            size = 15;
        } else {
            paddingX = 9;
            paddingY = 12;
            rows = 3;
            columns = 6;
            size = 20;
        }
        int x = 0;
        int y = 0;
        for (Map.Entry<Effect, MobEffectsPredicate.InstancePredicate> effect : instance.effects.effects.entrySet()) {
            RenderMisc.addMobEffectTooltip(tooltip, effect.getKey(), effect.getValue(), paddingX + (x * (size + 2)), SPACE_TOP + paddingY + (y * (size + 2)), size, mouseX, mouseY);
            if (x < (columns - 1)) { x += 1; } else if (y < (rows - 1)) { x = 0; y += 1; } else { break; }
        }
    }
}
