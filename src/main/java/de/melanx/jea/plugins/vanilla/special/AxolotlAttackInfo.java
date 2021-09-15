package de.melanx.jea.plugins.vanilla.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class AxolotlAttackInfo implements ICriterionInfo<EffectsChangedTrigger.TriggerInstance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("minecraft", "husbandry/kill_axolotl_target");
    public static final String CRITERION = "kill_axolotl_target";
    
    @Override
    public Class<EffectsChangedTrigger.TriggerInstance> criterionClass() {
        return EffectsChangedTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, EffectsChangedTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, EffectsChangedTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, EffectsChangedTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(Math.max(0, (((ClientTickHandler.ticksInGame + mc.getFrameTime()) % 24) / 6) - 3), InteractionHand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, new ItemStack(Items.IRON_SWORD));
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        
        poseStack.pushPose();
        poseStack.translate(46, SPACE_TOP + RECIPE_HEIGHT - 6, 70);
        JeaRender.normalize(poseStack);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-10));
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.2f);
        RenderEntityCache.renderPlainEntity(mc, EntityType.AXOLOTL, poseStack, buffer);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, EffectsChangedTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
