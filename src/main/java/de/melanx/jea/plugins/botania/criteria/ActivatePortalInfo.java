//package de.melanx.jea.plugins.botania.criteria;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.plugins.botania.AlfPortalRenderer;
//import de.melanx.jea.plugins.botania.BotaniaItems;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.SteveRender;
//import de.melanx.jea.util.TooltipUtil;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.network.chat.Component;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.TranslatableComponent;
//import vazkii.botania.common.advancements.AlfPortalTrigger;
//import vazkii.botania.common.block.ModBlocks;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ActivatePortalInfo implements ICriterionInfo<AlfPortalTrigger.Instance> {
//
//    @Override
//    public Class<AlfPortalTrigger.Instance> criterionClass() {
//        return AlfPortalTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, AlfPortalTrigger.Instance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                BotaniaItems.wands(),
//                List.of(new ItemStack(ModBlocks.alfPortal))
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, AlfPortalTrigger.Instance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, 35, SPACE_TOP + 72);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, AlfPortalTrigger.Instance instance, double mouseX, double mouseY) {
//        JeaRender.slotAt(poseStack, 35, SPACE_TOP + 72);
//        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 60;
//        boolean activated = animationTime > 30;
//        float swing = animationTime >= 27 && animationTime <= 33 ? (animationTime - 27) / 6f : 0;
//
//        poseStack.pushPose();
//        poseStack.translate(21, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
//        SteveRender.defaultPose(mc);
//        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
//        SteveRender.setEquipmentHand(mc, JeaRender.cycle(BotaniaItems.wands()));
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//
//        poseStack.pushPose();
//        poseStack.translate(RECIPE_WIDTH - 25, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, true, 0.9f);
//        AlfPortalRenderer.renderAlfPortal(poseStack, buffer, mc, activated);
//        poseStack.popPose();
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, AlfPortalTrigger.Instance instance, double mouseX, double mouseY) {
//        if (mouseX > RECIPE_WIDTH - 48 && mouseX < RECIPE_WIDTH - 3 && mouseY > SPACE_TOP + 17 && mouseY < SPACE_TOP + 91) {
//            List<MutableComponent> list = new ArrayList<>();
//            TooltipUtil.addLocationValues(list, instance.getPos());
//            if (!list.isEmpty()) {
//                tooltip.add(new TranslatableComponent("jea.item.tooltip.botania.alf_portal.location").withStyle(ChatFormatting.GOLD));
//                tooltip.addAll(list);
//            }
//        }
//    }
//}
