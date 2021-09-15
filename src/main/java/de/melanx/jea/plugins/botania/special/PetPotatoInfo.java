//package de.melanx.jea.plugins.botania.special;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.plugins.botania.BotaniaJea;
//import de.melanx.jea.util.ItemUtil;
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
//import net.minecraft.world.InteractionHand;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.network.chat.Component;
//import vazkii.botania.common.block.ModBlocks;
//
//import java.util.List;
//
//public class PetPotatoInfo implements ICriterionInfo<ImpossibleTrigger.TriggerInstance> {
//
//    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "main/tiny_potato_pet");
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
//                List.of(new ItemStack(ModBlocks.tinyPotato))
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, BotaniaJea.TINY_POTATO, (RECIPE_WIDTH / 2) + 10, SPACE_TOP + 36, 48, 48, 0, 0);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//        List<ItemStack> stacks = ItemUtil.creators();
//        int animationTimeTicks = ClientTickHandler.ticksInGame % 40;
//        int idx = (ClientTickHandler.ticksInGame / 40) % stacks.size();
//        ItemStack stack = animationTimeTicks < 20 ? stacks.get(idx) : ItemStack.EMPTY;
//        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 40;
//        float swing = animationTime >= 14 && animationTime <= 20 ? (animationTime - 14) / 6f : 0;
//        poseStack.pushPose();
//        poseStack.translate(30, SPACE_TOP + 90, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, false, 2.7f);
//        SteveRender.defaultPose(mc);
//        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
//        SteveRender.setEquipmentHand(mc, stack);
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//
//    }
//}
