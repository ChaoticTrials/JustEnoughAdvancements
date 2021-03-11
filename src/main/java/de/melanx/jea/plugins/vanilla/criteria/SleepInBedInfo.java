package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.TooltipUtil;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.PositionTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Pose;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class SleepInBedInfo implements ICriterionInfo<PositionTrigger.Instance> {

    @Override
    public Class<PositionTrigger.Instance> criterionClass() {
        return PositionTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, PositionTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, PositionTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, PositionTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(116, SPACE_TOP + 50, 0);
        JeaRender.normalize(matrixStack);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-15));
        JeaRender.transformForEntityRenderFront(matrixStack, true, 2.8f);
        matrixStack.push();
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-90));
        matrixStack.translate(-0.5, -0.6, 0.95);
        int light = LightTexture.packLight(15, 15);
        //noinspection deprecation
        mc.getBlockRendererDispatcher().renderBlock(Blocks.RED_BED.getDefaultState().with(BlockStateProperties.BED_PART, BedPart.FOOT), matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
        matrixStack.pop();
        SteveRender.defaultPose(mc);
        SteveRender.setPose(mc, 0, 16, 0, 0, Pose.SLEEPING, Hand.MAIN_HAND, false, false, 0, false);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, PositionTrigger.Instance instance, double mouseX, double mouseY) {
        if (mouseX > 26 && mouseX < RECIPE_WIDTH - 26 && mouseY > SPACE_TOP + 28 && mouseY < SPACE_TOP + 82) {
            ArrayList<IFormattableTextComponent> list = new ArrayList<>();
            TooltipUtil.addLocationValues(list, instance.location);
            tooltip.addAll(list);
        }
    }
}
