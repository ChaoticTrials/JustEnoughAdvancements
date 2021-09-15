//package de.melanx.jea.plugins.botania.special;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.SteveRender;
//import de.melanx.jea.util.IngredientUtil;
//import de.melanx.jea.util.TooltipUtil;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.block.BlockRenderDispatcher;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.LightTexture;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.Mth;
//import com.mojang.math.Vector3f;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.network.chat.Component;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.TranslatableComponent;
//import vazkii.botania.common.advancements.UseItemSuccessTrigger;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LaputaInfo implements ICriterionInfo<UseItemSuccessTrigger.Instance> {
//
//    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "challenge/l20_shard_use");
//    public static final String CRITERION = "use_l20_shard";
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
//        layout.getItemStacks().init(0, true, 48, SPACE_TOP + RECIPE_HEIGHT - 20);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, double mouseX, double mouseY) {
//        JeaRender.slotAt(poseStack, 48, SPACE_TOP + RECIPE_HEIGHT - 20);
//        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 50;
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
//        float upFloat = (((ClientTickHandler.ticksInGame + mc.getFrameTime()) % 10) / 10f) * 1.3f;
//        upFloat = Mth.clamp(upFloat, 0, 1);
//        int upAmount = ((ClientTickHandler.ticksInGame) / 10) % 5;
//        BlockState state = Blocks.GRASS_BLOCK.defaultBlockState();
//        int light = LightTexture.pack(15, 15);
//        BlockRenderDispatcher brd = mc.getBlockRenderer();
//        poseStack.pushPose();
//        poseStack.translate(RECIPE_WIDTH - 45, SPACE_TOP + 68, 0);
//        poseStack.mulPose(Vector3f.XP.rotationDegrees(-13));
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderFront(poseStack, true, 0.9f);
//        poseStack.translate(-2, 0, 0);
//        for (int i = 0; i < 5; i++) {
//            poseStack.pushPose();
//            if (i < upAmount) {
//                poseStack.translate(0, 4.2, 0);
//            } else if (i == upAmount) {
//                poseStack.translate(0, 4.2 * upFloat, 0);
//            }
//            //noinspection deprecation
//            brd.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//            poseStack.popPose();
//            poseStack.translate(1, 0, 0);
//        }
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
