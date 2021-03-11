package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DamageUtil;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.EntityHurtPlayerTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class HurtByEntityInfo implements ICriterionInfo<EntityHurtPlayerTrigger.Instance> {

    @Override
    public Class<EntityHurtPlayerTrigger.Instance> criterionClass() {
        return EntityHurtPlayerTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, EntityHurtPlayerTrigger.Instance instance, IIngredients ii) {
        
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, EntityHurtPlayerTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, EntityHurtPlayerTrigger.Instance instance, double mouseX, double mouseY) {
        DamageUtil.draw(matrixStack, buffer, mc, instance.damage, null, false, true);
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, EntityHurtPlayerTrigger.Instance instance, double mouseX, double mouseY) {
        DamageUtil.addTooltip(tooltip, Minecraft.getInstance(), instance.damage, null, false, true, mouseX, mouseY);
    }
}
