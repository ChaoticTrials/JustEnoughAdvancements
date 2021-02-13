package de.melanx.jea.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemCriterionInfo implements ICriterionInfo<InventoryChangeTrigger.Instance> {

    @Override
    public Class<InventoryChangeTrigger.Instance> criterionClass() {
        return InventoryChangeTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.Instance instance, IIngredients ii) {
        ii.setInput(VanillaTypes.ITEM, new ItemStack(Items.DIAMOND));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 0, SPACE_TOP);
    }

    @Override
    public void draw(MatrixStack matrixStack, Minecraft mc, IAdvancementInfo advancement, String criterionKey, InventoryChangeTrigger.Instance instance, double mouseX, double mouseY) {
        
    }
}
