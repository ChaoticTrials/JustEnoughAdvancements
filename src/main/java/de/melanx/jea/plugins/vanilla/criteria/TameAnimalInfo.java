package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.TameAnimalTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class TameAnimalInfo implements ICriterionInfo<TameAnimalTrigger.Instance> {

    @Override
    public Class<TameAnimalTrigger.Instance> criterionClass() {
        return TameAnimalTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.Instance instance, double mouseX, double mouseY) {

    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, TameAnimalTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
