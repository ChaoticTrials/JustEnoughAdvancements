package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderMisc;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.TooltipUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.LocationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BellBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WinRaidInfo implements ICriterionInfo<LocationTrigger.TriggerInstance> {

    private final BellBlockEntity tile = new BellBlockEntity(JeaRender.BELOW_WORLD, Blocks.BELL.defaultBlockState());
    private BlockEntityRenderer<BellBlockEntity> tileRender = null;
    
    @Override
    public Class<LocationTrigger.TriggerInstance> criterionClass() {
        return LocationTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(35, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderFront(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteveStatic(mc, poseStack, buffer);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 70, SPACE_TOP + 82, 0);
        JeaRender.normalize(poseStack);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-15));
        JeaRender.transformForEntityRenderFront(poseStack, false, 2.8f);
        //noinspection deprecation
        mc.getBlockRenderer().renderSingleBlock(Blocks.BELL.defaultBlockState(), poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        this.tile.setLevel(Objects.requireNonNull(mc.level));
        this.tile.blockState = Blocks.BELL.defaultBlockState();
        this.tile.clickDirection = Direction.SOUTH;
        this.tile.ticks = Math.max(0, (ClientTickHandler.ticksInGame % 60) - 10);
        this.tile.shaking = this.tile.ticks > 0;
        if (this.tileRender == null) {
            this.tileRender = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            this.tileRender.render(this.tile, mc.getFrameTime(), poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 58, SPACE_TOP + 5, 0);
        poseStack.scale(1.5f, 1.5f, 1.5f);
        RenderMisc.renderMobEffect(poseStack, mc, MobEffects.HERO_OF_THE_VILLAGE);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (mouseY > SPACE_TOP + 6 && mouseY < SPACE_TOP + 90 && mouseX > 17 && mouseX < 53) {
            ArrayList<MutableComponent> list = new ArrayList<>();
            TooltipUtil.addLocationValues(list, instance.location);
            tooltip.addAll(list);
        }
        RenderMisc.addMobEffectTooltip(tooltip, new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 0, 0), Optional.empty(), RECIPE_WIDTH - 58, SPACE_TOP + 5, (int) (1.5f * 20), mouseX, mouseY);
    }
}
