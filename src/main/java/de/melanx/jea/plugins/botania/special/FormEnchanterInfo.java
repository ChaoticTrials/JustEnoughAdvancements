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
//import net.minecraft.world.item.DyeColor;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.resources.ResourceLocation;
//import com.mojang.math.Vector3f;
//import net.minecraft.network.chat.Component;
//import vazkii.botania.common.block.ModBlocks;
//
//import java.util.List;
//
//public class FormEnchanterInfo implements ICriterionInfo<ImpossibleTrigger.TriggerInstance> {
//
//    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "main/enchanter_make");
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
//                List.of(new ItemStack(ModBlocks.enchanter))
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, IIngredients ii) {
//        //
//    }
//
//    @Override
//    @SuppressWarnings("deprecation")
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//        float animationTime = (7 + ClientTickHandler.ticksInGame + mc.getFrameTime()) % 50;
//        float swing = animationTime <= 6 ? animationTime / 6f : 0;
//        poseStack.pushPose();
//        poseStack.translate(27, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
//        SteveRender.defaultPose(mc);
//        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
//        SteveRender.setEquipmentHand(mc, JeaRender.cycle(BotaniaItems.wands()));
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//        BlockState state = (animationTime > 3 && animationTime <= 33 ? ModBlocks.enchanter : Blocks.LAPIS_BLOCK).defaultBlockState();
//        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
//        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
//        BlockState flower = ModBlocks.getFlower(JeaRender.cycle(DyeColor.class)).defaultBlockState();
//        BlockRenderDispatcher brd = mc.getBlockRenderer();
//        int light = LightTexture.pack(15, 15);
//        poseStack.pushPose();
//        poseStack.translate(RECIPE_WIDTH - 60, SPACE_TOP + 50, 0);
//        poseStack.mulPose(Vector3f.XP.rotationDegrees(-13));
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, false, 1f);
//        brd.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//        poseStack.translate(-2, -1, -2);
//        for (int z = 0; z < 5; z++) {
//            for (int x = 0; x < 5; x++) {
//                if ((x != 0 && x != 4) || (z != 0 && z != 4)) {
//                    if ((x == 1 || x == 3) && (z == 1 || z == 3)) {
//                        brd.renderSingleBlock(grass, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//                        poseStack.translate(0, 1, 0);
//                        brd.renderSingleBlock(flower, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//                        poseStack.translate(0, -1, 0);
//                    } else {
//                        brd.renderSingleBlock(obsidian, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//                    }
//                }
//                poseStack.translate(1, 0, 0);
//            }
//            poseStack.translate(-5, 0, 1);
//        }
//        poseStack.popPose();
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//
//    }
//}
