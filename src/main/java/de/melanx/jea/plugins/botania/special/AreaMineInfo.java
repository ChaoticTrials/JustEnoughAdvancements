//package de.melanx.jea.plugins.botania.special;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.plugins.botania.BotaniaItems;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.SteveRender;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import io.github.noeppi_noeppi.libx.render.RenderHelperBlock;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.advancements.critereon.ImpossibleTrigger;
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
//import com.mojang.math.Vector3f;
//import net.minecraft.network.chat.Component;
//
//import java.util.List;
//
//public class AreaMineInfo implements ICriterionInfo<ImpossibleTrigger.TriggerInstance> {
//    
//    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "challenge/rank_ss_pick");
//    public static final String CRITERION = "code_triggered";
//    
//    @Override
//    public Class<ImpossibleTrigger.TriggerInstance> criterionClass() {
//        return ImpossibleTrigger.TriggerInstance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                BotaniaItems.shatterers(5, true, false)
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, ((RECIPE_WIDTH - 18) / 2) - 10, SPACE_TOP + 70);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//        JeaRender.slotAt(poseStack, ((RECIPE_WIDTH - 18) / 2) - 10, SPACE_TOP + 70);
//        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 60;
//        float swing = animationTime >= 20 && animationTime <= 50 ? ((animationTime - 20) % 6) / 6f : 0;
//        ItemStack stack = JeaRender.cycle(BotaniaItems.shatterers(5, true, false));
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
//        if (animationTime < 50) {
//            float b = animationTime >= 20 ? (animationTime - 20) / 30f : 0;
//            int breakProgress = b <= 0 ? 0 : (int) Math.ceil(10 * b);
//            BlockState state = Blocks.STONE.defaultBlockState();
//            BlockRenderDispatcher brd = mc.getBlockRenderer();
//            int light = LightTexture.pack(15, 15);
//            poseStack.pushPose();
//            poseStack.translate(RECIPE_WIDTH - 40, SPACE_TOP + 63, 0);
//            poseStack.mulPose(Vector3f.XP.rotationDegrees(-13));
//            JeaRender.normalize(poseStack);
//            JeaRender.transformForEntityRenderSide(poseStack, true, 0.6f);
//            poseStack.translate(-2, 0, -0.5);
//            for (int y = 0; y < 7; y++) {
//                for (int x = 0; x < 9; x++) {
//                    //noinspection deprecation
//                    brd.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//                    RenderHelperBlock.renderBlockBreak(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, breakProgress);
//                    poseStack.translate(1, 0, 0);
//                }
//                poseStack.translate(-9, 1, 0);
//            }
//            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
//            Minecraft.getInstance().renderBuffers().crumblingBufferSource().endBatch();
//            poseStack.popPose();
//        }
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//
//    }
//}
