package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.UsedTotemTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class UseTotemInfo implements ICriterionInfo<UsedTotemTrigger.Instance> {

    @Override
    public Class<UsedTotemTrigger.Instance> criterionClass() {
        return UsedTotemTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, UsedTotemTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.fromItemPredicate(instance.item, true, Items.TOTEM_OF_UNDYING)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, UsedTotemTrigger.Instance instance, IIngredients ii) {
         layout.getItemStacks().init(0, true, Jea.LARGE_ITEM, (RECIPE_WIDTH / 2) + 10, SPACE_TOP + 36, 48, 48, 0, 0);
         layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, UsedTotemTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 85, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderFront(matrixStack, false, 2.7f);
        SteveRender.defaultPose(mc);
        SteveRender.hurtTime(10);
        SteveRender.use(10, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, true, Items.TOTEM_OF_UNDYING)));
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        ITextComponent text = new TranslationTextComponent("jea.item.tooltip.totem");
        mc.fontRenderer.func_243248_b(matrixStack, text, 60, SPACE_TOP + 15, 0x000000);
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, UsedTotemTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
