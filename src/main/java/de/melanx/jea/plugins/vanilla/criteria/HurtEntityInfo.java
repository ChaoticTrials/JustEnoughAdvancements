package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DamageUtil;
import de.melanx.jea.render.JeaRender;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class HurtEntityInfo implements ICriterionInfo<PlayerHurtEntityTrigger.Instance> {

    @Override
    public Class<PlayerHurtEntityTrigger.Instance> criterionClass() {
        return PlayerHurtEntityTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, PlayerHurtEntityTrigger.Instance instance, IIngredients ii) {
        
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, PlayerHurtEntityTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        ItemStack stack = new ItemStack(Items.CREEPER_HEAD);
        stack.setDisplayName(new TranslationTextComponent("jea.item.tooltip.damage.hurt_entity").mergeStyle(TextFormatting.RED));
        layout.getItemStacks().set(0, stack);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, PlayerHurtEntityTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        DamageUtil.draw(matrixStack, buffer, mc, instance.damage, instance.entity, true, false);
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, PlayerHurtEntityTrigger.Instance instance, double mouseX, double mouseY) {
        DamageUtil.addTooltip(tooltip, Minecraft.getInstance(), instance.damage, instance.entity, true, false, mouseX, mouseY);
    }
}
