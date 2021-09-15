package de.melanx.jea.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class LargeItemIngredientRender implements IIngredientRenderer<ItemStack> {

    public final int size;
    public final float rotationDegrees;

    public LargeItemIngredientRender(int size, float rotationDegrees) {
        this.size = size;
        this.rotationDegrees = rotationDegrees;
    }
    
    @Override
    public void render(@Nonnull PoseStack poseStack, int x, int y, @Nullable ItemStack stack) {
        if (stack != null) {
            poseStack.pushPose();
            int half = this.size / 2;
            poseStack.translate(x + half, y + half, -(this.size  * (this.size / 16d)));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(this.rotationDegrees));
            poseStack.translate(-half, -half, 0);
            poseStack.scale(this.size / 16f, this.size / 16f, this.size / 16f);
            RenderSystem.getModelViewStack().pushPose();
            RenderSystem.getModelViewStack().mulPoseMatrix(poseStack.last().pose());
            RenderSystem.applyModelViewMatrix();
            Minecraft.getInstance().getItemRenderer().renderAndDecorateFakeItem(stack, 0, 0);
            RenderSystem.getModelViewStack().popPose();
            RenderSystem.applyModelViewMatrix();
            poseStack.popPose();
        }
    }

    @Nonnull
    @Override
    public List<Component> getTooltip(@Nonnull ItemStack stack, @Nonnull TooltipFlag flag) {
        return JustEnoughAdvancementsJEIPlugin.runtimeResult(runtime -> runtime.getIngredientManager().getIngredientRenderer(stack)).getTooltip(stack, flag);
    }

    @Nonnull
    @Override
    public Font getFontRenderer(@Nonnull Minecraft mc, @Nonnull ItemStack stack) {
        return JustEnoughAdvancementsJEIPlugin.runtimeResult(runtime -> runtime.getIngredientManager().getIngredientRenderer(stack)).getFontRenderer(mc, stack);
    }
}
