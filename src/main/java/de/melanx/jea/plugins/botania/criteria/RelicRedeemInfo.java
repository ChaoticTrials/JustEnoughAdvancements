//package de.melanx.jea.plugins.botania.criteria;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.systems.RenderSystem;
//import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.SteveRender;
//import de.melanx.jea.util.IngredientUtil;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.network.chat.Component;
//import vazkii.botania.common.advancements.RelicBindTrigger;
//import vazkii.botania.common.item.ModItems;
//
//import java.util.List;
//
//public class RelicRedeemInfo implements ICriterionInfo<RelicBindTrigger.Instance> {
//
//    @Override
//    public Class<RelicBindTrigger.Instance> criterionClass() {
//        return RelicBindTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, RelicBindTrigger.Instance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                List.of(new ItemStack(ModItems.dice)),
//                IngredientUtil.fromItemPredicate(instance.getPredicate(), ModItems.odinRing, ModItems.thorRing, ModItems.lokiRing, ModItems.kingKey, ModItems.flugelEye, ModItems.infiniteFruit)
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, RelicBindTrigger.Instance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, 70, SPACE_TOP + 68);
//        layout.getItemStacks().init(1, true, 124, SPACE_TOP + 68);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, RelicBindTrigger.Instance instance, double mouseX, double mouseY) {
//        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 80;
//        ItemStack stack = animationTime > 60 ? ItemStack.EMPTY : animationTime < 30 ? new ItemStack(ModItems.dice) : JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.getPredicate(), ModItems.odinRing, ModItems.thorRing, ModItems.lokiRing, ModItems.kingKey, ModItems.flugelEye, ModItems.infiniteFruit));
//        float swing = animationTime >= 27 && animationTime <= 33 ? (animationTime - 27) / 6f : 0;
//        poseStack.pushPose();
//        poseStack.translate(30, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
//        SteveRender.defaultPose(mc);
//        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
//        SteveRender.setEquipmentHand(mc, stack);
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//        
//        JeaRender.slotAt(poseStack, 70, SPACE_TOP + 68);
//        JeaRender.slotAt(poseStack, 124, SPACE_TOP + 68);
//        RenderSystem.enableBlend();
//        JustEnoughAdvancementsJEIPlugin.getArrow(false).draw(poseStack, 90, SPACE_TOP + 69);
//        RenderSystem.disableBlend();
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, RelicBindTrigger.Instance instance, double mouseX, double mouseY) {
//
//    }
//}
