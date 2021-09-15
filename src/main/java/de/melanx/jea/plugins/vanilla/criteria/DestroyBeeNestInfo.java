package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.BeeNestDestroyedTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class DestroyBeeNestInfo implements ICriterionInfo<BeeNestDestroyedTrigger.TriggerInstance> {
    
    @Override
    public Class<BeeNestDestroyedTrigger.TriggerInstance> criterionClass() {
        return BeeNestDestroyedTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, BeeNestDestroyedTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                IngredientUtil.ingredients(instance.block, Blocks.BEEHIVE, Blocks.BEE_NEST),
                IngredientUtil.fromItemPredicate(instance.item, Items.IRON_HOE)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, BeeNestDestroyedTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_BLOCK_BREAK_SLOW, 82, SPACE_TOP + 42, 48, 48, 0, 0);
        layout.getItemStacks().init(1, true, 55, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, BeeNestDestroyedTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, 55, SPACE_TOP + 72);
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.IRON_HOE));
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(((ClientTickHandler.ticksInGame + mc.getFrameTime()) % 6) / 6, InteractionHand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        if (!instance.numBees.isAny()) {
            poseStack.pushPose();
            poseStack.translate(95, SPACE_TOP + 34, 0);
            JeaRender.normalize(poseStack);
            JeaRender.transformForEntityRenderSide(poseStack, false, 1);
            RenderEntityCache.renderPlainEntity(mc, EntityType.BEE, poseStack, buffer);
            poseStack.popPose();
            Component text = new TextComponent(": ").append(IngredientUtil.text(instance.numBees));
            mc.font.draw(poseStack, text, 105, SPACE_TOP + 25, 0x000000);
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, BeeNestDestroyedTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        //
    }
}
