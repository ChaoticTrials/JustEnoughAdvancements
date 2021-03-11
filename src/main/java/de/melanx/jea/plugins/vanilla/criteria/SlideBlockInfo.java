package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.SlideDownBlockTrigger;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class SlideBlockInfo implements ICriterionInfo<SlideDownBlockTrigger.Instance> {

    @Override
    public Class<SlideDownBlockTrigger.Instance> criterionClass() {
        return SlideDownBlockTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, SlideDownBlockTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(instance.block == null ? Items.HONEY_BLOCK : instance.block))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, SlideDownBlockTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, 82, SPACE_TOP + 42, 48, 48, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, SlideDownBlockTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(27, SPACE_TOP + 68, 0);
        JeaRender.transformForEntityRenderFront(matrixStack, false, 1.5f);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-10));
        matrixStack.translate(0, -((ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 25), 0);
        JeaRender.normalize(matrixStack);
        SteveRender.defaultPose(mc);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        BlockState state = IngredientUtil.getState(instance.block == null ? Blocks.HONEY_BLOCK : instance.block, instance.stateCondition);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(27, SPACE_TOP + 68, 0);
        JeaRender.transformForEntityRenderFront(matrixStack, false, 1.5f);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-5));
        JeaRender.normalize(matrixStack);
        matrixStack.translate(-0.5, 1, -1.4);
        //noinspection deprecation
        mc.getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        matrixStack.translate(0, -1, 0);
        //noinspection deprecation
        mc.getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        matrixStack.translate(0, -1, 0);
        //noinspection deprecation
        mc.getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, SlideDownBlockTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
