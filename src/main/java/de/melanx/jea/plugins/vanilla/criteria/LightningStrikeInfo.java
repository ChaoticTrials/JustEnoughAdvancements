package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.LightningStrikeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class LightningStrikeInfo implements ICriterionInfo<LightningStrikeTrigger.TriggerInstance> {

    @Override
    public Class<LightningStrikeTrigger.TriggerInstance> criterionClass() {
        return LightningStrikeTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, LightningStrikeTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(new ItemStack(Items.LIGHTNING_ROD))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, LightningStrikeTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, 15, SPACE_TOP + 42, 48, 48, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, LightningStrikeTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        poseStack.pushPose();
        poseStack.translate(1, SPACE_TOP + RECIPE_HEIGHT - 84 - 2, 0);
        poseStack.scale(0.62f, 0.62f, 0.62f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        JustEnoughAdvancementsJEIPlugin.getLightning().draw(poseStack);
        RenderSystem.disableBlend();
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 24, SPACE_TOP + RECIPE_HEIGHT - 2, 0);
        JeaRender.normalize(poseStack);
        RenderEntityCache.renderEntity(mc, instance.bystander, poseStack, buffer, JeaRender.entityRenderFront(true, 1.5f));
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, LightningStrikeTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.bystander, RECIPE_WIDTH - 24, SPACE_TOP + RECIPE_HEIGHT - 2, JeaRender.normalScale(1.5f), mouseX, mouseY);
    }
}
