package de.melanx.jea.recipe;

import com.mojang.blaze3d.vertex.PoseStack;

public class AdvancementRecipeRendererTiny extends AdvancementRecipeRenderer {

    @Override
    protected void transform(PoseStack poseStack) {
        poseStack.scale(16 / 26f, 16 / 26f, 1);
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
