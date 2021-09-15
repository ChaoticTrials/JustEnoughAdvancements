//package de.melanx.jea.plugins.botania.special;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.Jea;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
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
//import net.minecraft.util.LazyLoadedValue;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.network.chat.Component;
//import net.minecraftforge.registries.ForgeRegistries;
//import vazkii.botania.common.item.equipment.bauble.ItemBauble;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class BaubleInfo implements ICriterionInfo<ImpossibleTrigger.TriggerInstance> {
//
//    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "main/bauble_wear");
//    public static final String CRITERION = "code_triggered";
//
//    private static final LazyLoadedValue<List<ItemStack>> baubles = new LazyLoadedValue<>(BaubleInfo::getBaubles);
//
//    @Override
//    public Class<ImpossibleTrigger.TriggerInstance> criterionClass() {
//        return ImpossibleTrigger.TriggerInstance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                baubles.get()
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) + 10, SPACE_TOP + 36, 48, 48, 0, 0);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime() + 10) % 40;
//        float swing = animationTime < 6 ? animationTime / 6f : 0;
//        ItemStack stack = animationTime > 3 && animationTime < 15 ? ItemStack.EMPTY : JeaRender.cycle(baubles.get(), 40);
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
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.TriggerInstance instance, double mouseX, double mouseY) {
//
//    }
//
//    private static List<ItemStack> getBaubles() {
//        return ForgeRegistries.ITEMS.getValues().stream()
//                .filter(item -> item instanceof ItemBauble)
//                .map(ItemStack::new)
//                .collect(Collectors.toList());
//    }
//}
