//package de.melanx.jea.plugins.botania.special;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.plugins.botania.BotaniaJea;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.SteveRender;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.advancements.critereon.ImpossibleTrigger;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.network.chat.Component;
//import vazkii.botania.common.block.ModBlocks;
//
//import java.util.List;
//
//public class LuminizerInfo implements ICriterionInfo<ImpossibleTrigger.TriggerInstance> {
//
//    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "main/luminizer_ride");
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
//                List.of(new ItemStack(ModBlocks.lightRelayDefault), new ItemStack(ModBlocks.lightRelayDetector), new ItemStack(ModBlocks.lightRelayFork), new ItemStack(ModBlocks.lightRelayToggle)),
//                List.of(new ItemStack(ModBlocks.lightRelayDefault), new ItemStack(ModBlocks.lightRelayDetector), new ItemStack(ModBlocks.lightRelayFork), new ItemStack(ModBlocks.lightRelayToggle))
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, BotaniaJea.LUMINIZER, 5, SPACE_TOP + RECIPE_HEIGHT - 29, 24, 24, 0, 0);
//        layout.getItemStacks().init(1, true, BotaniaJea.LUMINIZER, RECIPE_WIDTH - 29, SPACE_TOP + RECIPE_HEIGHT - 29, 24, 24, 0, 0);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//        double length = RECIPE_WIDTH - (2 * 20);
//        double b = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % length;
//        double x = Math.abs((2 * b) - length);
//        boolean back = (2 * b) - length < 0;
//        poseStack.pushPose();
//        poseStack.translate(20 + x, SPACE_TOP + 85, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, back, 2f);
//        SteveRender.defaultPose(mc);
//        SteveRender.limbSwing(((float) (Math.sin((ClientTickHandler.ticksInGame + mc.getFrameTime()) / 3) * 0.5)));
//        SteveRender.clearEquipment(mc);
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//        //
//    }
//}
