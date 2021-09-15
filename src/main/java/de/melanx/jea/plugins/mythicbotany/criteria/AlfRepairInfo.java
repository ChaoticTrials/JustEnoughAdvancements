//package de.melanx.jea.plugins.mythicbotany.criteria;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.Jea;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.util.IngredientUtil;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import mythicbotany.ModBlocks;
//import mythicbotany.advancement.AlfRepairTrigger;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.network.chat.Component;
//
//import java.util.List;
//
//public class AlfRepairInfo implements ICriterionInfo<AlfRepairTrigger.Instance> {
//
//    @Override
//    public Class<AlfRepairTrigger.Instance> criterionClass() {
//        return AlfRepairTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, AlfRepairTrigger.Instance instance, IIngredients ii) {
//        ii.setInputLists(VanillaTypes.ITEM, List.of(
//                List.of(new ItemStack(ModBlocks.alfsteelPylon)),
//                IngredientUtil.fromItemPredicate(instance.item, Items.IRON_SWORD, Items.IRON_AXE,
//                        Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_HOE, Items.IRON_HELMET,
//                        Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS)
//        ));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, AlfRepairTrigger.Instance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, Jea.LARGE_BLOCK, (RECIPE_WIDTH / 2) - 24, SPACE_TOP + 37, 48, 48, 0, 0);
//        layout.getItemStacks().init(1, true, (RECIPE_WIDTH / 2) - 8, SPACE_TOP + 15);
//        layout.getItemStacks().set(ii);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, AlfRepairTrigger.Instance instance, double mouseX, double mouseY) {
//        
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, AlfRepairTrigger.Instance instance, double mouseX, double mouseY) {
//
//    }
//}
