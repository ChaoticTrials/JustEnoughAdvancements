//package de.melanx.jea.plugins.botania.special;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.RenderEntityCache;
//import de.melanx.jea.render.SteveRender;
//import de.melanx.jea.util.IngredientUtil;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.network.chat.Component;
//import vazkii.botania.common.advancements.UseItemSuccessTrigger;
//import vazkii.botania.common.entity.ModEntities;
//import vazkii.botania.common.item.ModItems;
//
//import java.util.List;
//
//public class PinkinatorInfo implements ICriterionInfo<UseItemSuccessTrigger.Instance> {
//
//    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "challenge/pinkinator");
//    public static final String CRITERION = "use_pinkinator";
//
//    @Override
//    public Class<UseItemSuccessTrigger.Instance> criterionClass() {
//        return UseItemSuccessTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                IngredientUtil.fromItemPredicate(instance.getItem(), true, ModItems.pinkinator)
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, (RECIPE_WIDTH - 18) / 2, SPACE_TOP + 70);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, double mouseX, double mouseY) {
//        JeaRender.slotAt(poseStack, (RECIPE_WIDTH - 18) / 2, SPACE_TOP + 70);
//        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 60;
//        float swing = animationTime >= 27 && animationTime <= 33 ? (animationTime - 27) / 6f : 0;
//        ItemStack stack = animationTime > 30 ? ItemStack.EMPTY : JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.getItem(), true, ModItems.pinkinator));
//        EntityType<?> entity = animationTime > 30 ? ModEntities.PINK_WITHER : EntityType.WITHER;
//        poseStack.pushPose();
//        poseStack.translate(30, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, false, 2.7f);
//        SteveRender.defaultPose(mc);
//        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
//        SteveRender.setEquipmentHand(mc, stack);
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//        poseStack.pushPose();
//        poseStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, true, 2.7f);
//        RenderEntityCache.renderPlainEntity(mc, entity, poseStack, buffer);
//        poseStack.popPose();
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, double mouseX, double mouseY) {
//
//    }
//}
