package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
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
import net.minecraft.advancements.critereon.SlideDownBlockTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SlideBlockInfo implements ICriterionInfo<SlideDownBlockTrigger.TriggerInstance> {

    @Override
    public Class<SlideDownBlockTrigger.TriggerInstance> criterionClass() {
        return SlideDownBlockTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, SlideDownBlockTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(new ItemStack(instance.block == null ? Items.HONEY_BLOCK : instance.block))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, SlideDownBlockTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, 82, SPACE_TOP + 42, 48, 48, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, SlideDownBlockTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(27, SPACE_TOP + 68, 0);
        JeaRender.transformForEntityRenderFront(poseStack, false, 1.5f);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-10));
        poseStack.translate(0, -((ClientTickHandler.ticksInGame + mc.getFrameTime()) % 25), 0);
        JeaRender.normalize(poseStack);
        SteveRender.defaultPose(mc);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteve(mc, poseStack, buffer);
        BlockState state = IngredientUtil.getState(instance.block == null ? Blocks.HONEY_BLOCK : instance.block, instance.state);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(27, SPACE_TOP + 68, 0);
        JeaRender.transformForEntityRenderFront(poseStack, false, 1.5f);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-5));
        JeaRender.normalize(poseStack);
        poseStack.translate(-0.5, 1, -1.4);
        //noinspection deprecation
        mc.getBlockRenderer().renderSingleBlock(state, poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        poseStack.translate(0, -1, 0);
        //noinspection deprecation
        mc.getBlockRenderer().renderSingleBlock(state, poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        poseStack.translate(0, -1, 0);
        //noinspection deprecation
        mc.getBlockRenderer().renderSingleBlock(state, poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, SlideDownBlockTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
