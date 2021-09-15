package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.JustEnoughAdvancements;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.ChanneledLightningTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class ChannelingLightningInfo implements ICriterionInfo<ChanneledLightningTrigger.TriggerInstance> {
    
    public static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "textures/lightning.png");
    private final ItemStack trident;

    public ChannelingLightningInfo() {
        this.trident = new ItemStack(Items.TRIDENT);
        this.trident.enchant(Enchantments.CHANNELING, 1);
    }

    @Override
    public Class<ChanneledLightningTrigger.TriggerInstance> criterionClass() {
        return ChanneledLightningTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ChanneledLightningTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(this.trident.copy())
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ChanneledLightningTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 3, SPACE_TOP + RECIPE_HEIGHT - 21);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ChanneledLightningTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, 3, SPACE_TOP + RECIPE_HEIGHT - 21);
        poseStack.pushPose();
        poseStack.translate(10, SPACE_TOP + 55, 0);
        JeaRender.normalize(poseStack);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(60));
        JeaRender.transformForEntityRenderSide(poseStack, true, 2);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-45));
        SteveRender.setPose(mc, 180, -70, 0, 1, Pose.STANDING, InteractionHand.MAIN_HAND, false, false, 0, false);
        SteveRender.setEquipmentHand(mc, this.trident);
        SteveRender.renderSteveStatic(mc, poseStack, buffer);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 84, SPACE_TOP + RECIPE_HEIGHT - 84 - 2, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        JustEnoughAdvancementsJEIPlugin.getLightning().draw(poseStack);
        RenderSystem.disableBlend();
        poseStack.popPose();
        if (instance.victims.length > 0) {
            poseStack.pushPose();
            poseStack.translate(RECIPE_WIDTH - 24, SPACE_TOP + RECIPE_HEIGHT - 2, 0);
            JeaRender.normalize(poseStack);
            int victimIdx = ((ClientTickHandler.ticksInGame / 20) % instance.victims.length);
            RenderEntityCache.renderEntity(mc, instance.victims[victimIdx], poseStack, buffer, JeaRender.entityRenderFront(true, 1.5f));
            poseStack.popPose();
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ChanneledLightningTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (instance.victims.length > 0) {
            int victimIdx = ((ClientTickHandler.ticksInGame / 20) % instance.victims.length);
            RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.victims[victimIdx], RECIPE_WIDTH - 24, SPACE_TOP + RECIPE_HEIGHT - 2, JeaRender.normalScale(1.5f), mouseX, mouseY);
        }
    }
}
