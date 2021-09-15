//package de.melanx.jea.plugins.botania.criteria;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.plugins.botania.AlfPortalRenderer;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.util.TooltipUtil;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import com.mojang.math.Vector3f;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.network.chat.Component;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.TranslatableComponent;
//import vazkii.botania.common.advancements.AlfPortalBreadTrigger;
//import vazkii.botania.common.block.ModBlocks;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BreadInPortalInfo implements ICriterionInfo<AlfPortalBreadTrigger.Instance> {
//
//    @Override
//    public Class<AlfPortalBreadTrigger.Instance> criterionClass() {
//        return AlfPortalBreadTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, AlfPortalBreadTrigger.Instance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                List.of(new ItemStack(Items.BREAD)),
//                List.of(new ItemStack(ModBlocks.alfPortal))
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, AlfPortalBreadTrigger.Instance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, (RECIPE_WIDTH - 18) / 2, SPACE_TOP + ((RECIPE_HEIGHT - 18) / 2));
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, AlfPortalBreadTrigger.Instance instance, double mouseX, double mouseY) {
//        JeaRender.slotAt(poseStack, (RECIPE_WIDTH - 18) / 2, SPACE_TOP + ((RECIPE_HEIGHT - 18) / 2));
//        poseStack.pushPose();
//        //noinspection IntegerDivisionInFloatingPointContext
//        poseStack.translate(RECIPE_WIDTH / 2, SPACE_TOP + 80, 0);
//        JeaRender.normalize(poseStack);
//        poseStack.mulPose(Vector3f.XP.rotationDegrees(-10));
//        JeaRender.transformForEntityRenderFront(poseStack, true, 0.9f);
//        AlfPortalRenderer.renderAlfPortal(poseStack, buffer, mc, true);
//        poseStack.popPose();
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, AlfPortalBreadTrigger.Instance instance, double mouseX, double mouseY) {
//        //noinspection IntegerDivisionInFloatingPointContext
//        if (mouseX > (RECIPE_WIDTH / 2) - 37 && mouseX < (RECIPE_WIDTH / 2) + 37 && mouseY > SPACE_TOP + 7 && mouseY < SPACE_TOP + 81) {
//            List<MutableComponent> list = new ArrayList<>();
//            TooltipUtil.addLocationValues(list, instance.getPortal());
//            if (!list.isEmpty()) {
//                tooltip.add(new TranslatableComponent("jea.item.tooltip.botania.alf_portal.location").withStyle(ChatFormatting.GOLD));
//                tooltip.addAll(list);
//            }
//        }
//    }
//}
