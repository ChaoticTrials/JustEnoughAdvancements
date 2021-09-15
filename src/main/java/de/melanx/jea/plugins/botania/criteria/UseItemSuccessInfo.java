//package de.melanx.jea.plugins.botania.criteria;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.Jea;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.SteveRender;
//import de.melanx.jea.util.IngredientUtil;
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
//import vazkii.botania.common.advancements.UseItemSuccessTrigger;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UseItemSuccessInfo implements ICriterionInfo<UseItemSuccessTrigger.Instance> {
//
//    @Override
//    public Class<UseItemSuccessTrigger.Instance> criterionClass() {
//        return UseItemSuccessTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                IngredientUtil.fromItemPredicate(instance.getItem())
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) + 10, SPACE_TOP + 36, 48, 48, 0, 0);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, double mouseX, double mouseY) {
//        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 30;
//        float swing = animationTime < 6 ? animationTime / 6f : 0;
//        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.getItem()));
//        poseStack.pushPose();
//        poseStack.translate(30, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderFront(poseStack, false, 2.7f);
//        SteveRender.defaultPose(mc);
//        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
//        SteveRender.setEquipmentHand(mc, stack);
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, double mouseX, double mouseY) {
//        if (mouseX > 10 && mouseX < 51 && mouseY > SPACE_TOP + 7 && mouseY < SPACE_TOP + 90) {
//            List<MutableComponent> list = new ArrayList<>();
//            TooltipUtil.addLocationValues(list, instance.getLocation());
//            if (!list.isEmpty()) {
//                tooltip.add(new TranslatableComponent("jea.item.tooltip.location.any_title").withStyle(ChatFormatting.GOLD));
//                tooltip.addAll(list);
//            }
//        }
//    }
//}
