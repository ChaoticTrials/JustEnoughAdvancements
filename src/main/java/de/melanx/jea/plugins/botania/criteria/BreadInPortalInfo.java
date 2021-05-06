package de.melanx.jea.plugins.botania.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.plugins.botania.AlfPortalRenderer;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.util.TooltipUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.botania.common.advancements.AlfPortalBreadTrigger;
import vazkii.botania.common.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class BreadInPortalInfo implements ICriterionInfo<AlfPortalBreadTrigger.Instance> {

    @Override
    public Class<AlfPortalBreadTrigger.Instance> criterionClass() {
        return AlfPortalBreadTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, AlfPortalBreadTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(Items.BREAD)),
                ImmutableList.of(new ItemStack(ModBlocks.alfPortal))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, AlfPortalBreadTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH - 18) / 2, SPACE_TOP + ((RECIPE_HEIGHT - 18) / 2));
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, AlfPortalBreadTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, (RECIPE_WIDTH - 18) / 2, SPACE_TOP + ((RECIPE_HEIGHT - 18) / 2));
        matrixStack.push();
        //noinspection IntegerDivisionInFloatingPointContext
        matrixStack.translate(RECIPE_WIDTH / 2, SPACE_TOP + 80, 0);
        JeaRender.normalize(matrixStack);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-10));
        JeaRender.transformForEntityRenderFront(matrixStack, true, 0.9f);
        AlfPortalRenderer.renderAlfPortal(matrixStack, buffer, mc, true);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, AlfPortalBreadTrigger.Instance instance, double mouseX, double mouseY) {
        //noinspection IntegerDivisionInFloatingPointContext
        if (mouseX > (RECIPE_WIDTH / 2) - 37 && mouseX < (RECIPE_WIDTH / 2) + 37 && mouseY > SPACE_TOP + 7 && mouseY < SPACE_TOP + 81) {
            List<IFormattableTextComponent> list = new ArrayList<>();
            TooltipUtil.addLocationValues(list, instance.getPortal());
            if (!list.isEmpty()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.botania.alf_portal.location").mergeStyle(TextFormatting.GOLD));
                tooltip.addAll(list);
            }
        }
    }
}
