//package de.melanx.jea.plugins.botania.criteria;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.render.DamageUtil;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.util.LootUtil;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.advancements.critereon.DamagePredicate;
//import net.minecraft.advancements.critereon.EntityPredicate;
//import net.minecraft.advancements.critereon.MinMaxBounds;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.network.chat.Component;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.TranslatableComponent;
//import vazkii.botania.common.advancements.DopplegangerNoArmorTrigger;
//import vazkii.botania.common.block.ModBlocks;
//import vazkii.botania.common.entity.ModEntities;
//
//import java.util.List;
//
//public class GaiaNoArmorInfo implements ICriterionInfo<DopplegangerNoArmorTrigger.Instance> {
//
//    @Override
//    public Class<DopplegangerNoArmorTrigger.Instance> criterionClass() {
//        return DopplegangerNoArmorTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, DopplegangerNoArmorTrigger.Instance instance, IIngredients ii) {
//        
//
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, DopplegangerNoArmorTrigger.Instance instance, IIngredients ii) {
//        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
//        ItemStack stack = new ItemStack(ModBlocks.gaiaHead);
//        stack.setHoverName(new TranslatableComponent("jea.item.tooltip.botania.kill_no_armor").withStyle(ChatFormatting.RED));
//        layout.getItemStacks().set(0, stack);
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, DopplegangerNoArmorTrigger.Instance instance, double mouseX, double mouseY) {
//        JeaRender.slotAt(poseStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
//        DamagePredicate predicate = new DamagePredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, EntityPredicate.ANY, null, instance.getKillingBlow());
//        DamageUtil.draw(poseStack, buffer, mc, predicate, LootUtil.asLootPredicate(instance.getGuardian()), true, false, null, ModEntities.DOPPLEGANGER);
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, DopplegangerNoArmorTrigger.Instance instance, double mouseX, double mouseY) {
//        DamagePredicate predicate = new DamagePredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, EntityPredicate.ANY, null, instance.getKillingBlow());
//        DamageUtil.addTooltip(tooltip, Minecraft.getInstance(), predicate, LootUtil.asLootPredicate(instance.getGuardian()), true, false, null, ModEntities.DOPPLEGANGER, mouseX, mouseY);
//    }
//}
