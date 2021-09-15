//package de.melanx.jea.plugins.botania.criteria;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.SteveRender;
//import de.melanx.jea.util.IngredientUtil;
//import de.melanx.jea.util.TooltipUtil;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.TextComponent;
//import net.minecraft.network.chat.TranslatableComponent;
//import vazkii.botania.common.advancements.ManaGunTrigger;
//import vazkii.botania.common.item.ModItems;
//
//import java.util.List;
//
//public class ManaGunInfo implements ICriterionInfo<ManaGunTrigger.Instance> {
//
//    @Override
//    public Class<ManaGunTrigger.Instance> criterionClass() {
//        return ManaGunTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ManaGunTrigger.Instance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                gunStacks(instance)
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ManaGunTrigger.Instance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, 55, SPACE_TOP + 72);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ManaGunTrigger.Instance instance, double mouseX, double mouseY) {
//        JeaRender.slotAt(poseStack, 55, SPACE_TOP + 72);
//        ItemStack stack = JeaRender.cycle(gunStacks(instance));
//        poseStack.pushPose();
//        poseStack.translate(30, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
//        SteveRender.defaultPose(mc);
//        SteveRender.swing(0.35f, InteractionHand.MAIN_HAND);
//        SteveRender.setEquipmentHand(mc, stack);
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ManaGunTrigger.Instance instance, double mouseX, double mouseY) {
//
//    }
//    
//    private static List<ItemStack> gunStacks(ManaGunTrigger.Instance instance) {
//        if (instance.getDesu() != null) {
//            List<ItemStack> stacks = IngredientUtil.fromItemPredicate(instance.getItem(), new TranslatableComponent("jea.item.tooltip.botania.desu_gun", TooltipUtil.yesNo(instance.getDesu())), true, ModItems.manaGun);
//            if (instance.getDesu()) {
//                for (ItemStack stack : stacks) {
//                    stack.setHoverName(new TextComponent("desu gun"));
//                }
//            }
//            return stacks;
//        } else {
//            return IngredientUtil.fromItemPredicate(instance.getItem(),true, ModItems.manaGun);
//        }
//    }
//}
