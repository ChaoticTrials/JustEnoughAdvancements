package de.melanx.jea.ingredient;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.recipe.AdvancementRecipeRenderer;

public class AdvancementIngredientRenderer extends AdvancementRecipeRenderer {

    @Override
    protected void transform(PoseStack poseStack) {
        poseStack.scale(16 / 24f, 16 / 24f, 1);
        poseStack.translate(-1, -1, 0);
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }
}
