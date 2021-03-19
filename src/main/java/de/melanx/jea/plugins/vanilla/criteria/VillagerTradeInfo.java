package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.VillagerTradeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class VillagerTradeInfo implements ICriterionInfo<VillagerTradeTrigger.Instance> {

    @Override
    public Class<VillagerTradeTrigger.Instance> criterionClass() {
        return VillagerTradeTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, VillagerTradeTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(Items.EMERALD)),
                IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, VillagerTradeTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        layout.getItemStacks().init(1, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 24);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, VillagerTradeTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        JeaRender.slotAt(matrixStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 24);
        matrixStack.push();
        //noinspection IntegerDivisionInFloatingPointContext
        matrixStack.translate((RECIPE_WIDTH / 2) - 28, SPACE_TOP + 5, 0);
        RenderSystem.enableBlend();
        this.renderArrow(matrixStack, false);
        matrixStack.translate(0, 20, 0);
        this.renderArrow(matrixStack, true);
        RenderSystem.disableBlend();
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(20, SPACE_TOP + 80, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderFront(matrixStack, false, 2.2f);
        SteveRender.defaultPose(mc);
        SteveRender.setEquipmentHand(mc, new ItemStack(Items.EMERALD));
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 20, SPACE_TOP + 80, 0);
        JeaRender.normalize(matrixStack);
        DefaultEntityProperties properties = new DefaultEntityProperties(EntityType.VILLAGER, false, false, false, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)), 0, 0, 0, 0, 0, false, false);
        RenderEntityCache.renderEntity(mc, instance.villager, matrixStack, buffer, JeaRender.entityRenderFront(true, 2.2f), properties);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, VillagerTradeTrigger.Instance instance, double mouseX, double mouseY) {
        DefaultEntityProperties properties = new DefaultEntityProperties(EntityType.VILLAGER, false, false, false, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)), 0, 0, 0, 0, 0, false, false);
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.villager, RECIPE_WIDTH - 20, SPACE_TOP + 80, JeaRender.normalScale(2.2f), properties, mouseX, mouseY);
    }
    
    private void renderArrow(MatrixStack matrixStack, boolean left) {
        matrixStack.push();
        JustEnoughAdvancementsJEIPlugin.getArrow(left).draw(matrixStack, 0, 0, 0, 0, 0, 16);
        matrixStack.translate(24, 0, 0);
        JustEnoughAdvancementsJEIPlugin.getArrow(left).draw(matrixStack, 0, 0, 0, 0, 16, 0);
        matrixStack.pop();
    }
}
