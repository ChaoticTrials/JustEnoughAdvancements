package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.ShotCrossbowTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class ShootCrossbowInfo implements ICriterionInfo<ShotCrossbowTrigger.TriggerInstance> {

    public static final ResourceLocation ARROW_TEXTURE = new ResourceLocation("minecraft", "textures/entity/projectiles/arrow.png");
    
    @Override
    public Class<ShotCrossbowTrigger.TriggerInstance> criterionClass() {
        return ShotCrossbowTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ShotCrossbowTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                IngredientUtil.fromItemPredicate(instance.item, Items.CROSSBOW)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ShotCrossbowTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 50, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ShotCrossbowTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, 50, SPACE_TOP + 72);
        float animationTime = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getFrameTime()) % 66;
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.CROSSBOW));
        int useTick;
        if (animationTime < 30) {
            useTick = 0;
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putBoolean("Charged", false);
            stack.setTag(nbt);
        } else if (animationTime < 58) {
            useTick = Math.max(1, 25 - (int) Math.ceil(animationTime - 30));
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putBoolean("Charged", false);
            stack.setTag(nbt);
        } else {
            useTick = 0;
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putBoolean("Charged", true);
            stack.setTag(nbt);
        }
        poseStack.pushPose();
        poseStack.translate(25, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.use(useTick, InteractionHand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        if (animationTime < 10) {
            float arrowDist = animationTime / 10f;
            float arrowDown = 0.4f * (arrowDist * arrowDist);
            poseStack.pushPose();
            poseStack.translate(45 + (80 * arrowDist), SPACE_TOP + 25 + (20 * arrowDown), 0);
            float angle = (float) Math.atan(0.2 * arrowDist);
            poseStack.translate(8, 2.5, 0);
            poseStack.mulPose(Vector3f.ZP.rotation(angle));
            poseStack.translate(-8, -2.5, 0);
            RenderSystem.enableBlend();
            JustEnoughAdvancementsJEIPlugin.getShotArrow().draw(poseStack, 0, 0);
            RenderSystem.disableBlend();
            poseStack.popPose();
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ShotCrossbowTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
