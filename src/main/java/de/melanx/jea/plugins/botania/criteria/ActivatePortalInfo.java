package de.melanx.jea.plugins.botania.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.plugins.botania.AlfPortalRenderer;
import de.melanx.jea.plugins.botania.BotaniaItems;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.TooltipUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.botania.common.advancements.AlfPortalTrigger;
import vazkii.botania.common.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class ActivatePortalInfo implements ICriterionInfo<AlfPortalTrigger.Instance> {

    @Override
    public Class<AlfPortalTrigger.Instance> criterionClass() {
        return AlfPortalTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, AlfPortalTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                BotaniaItems.wands(),
                ImmutableList.of(new ItemStack(ModBlocks.alfPortal))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, AlfPortalTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 35, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, AlfPortalTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 35, SPACE_TOP + 72);
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 60;
        boolean activated = animationTime > 30;
        float swing = animationTime >= 27 && animationTime <= 33 ? (animationTime - 27) / 6f : 0;

        matrixStack.push();
        matrixStack.translate(21, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, JeaRender.cycle(BotaniaItems.wands()));
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 25, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, true, 0.9f);
        AlfPortalRenderer.renderAlfPortal(matrixStack, buffer, mc, activated);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, AlfPortalTrigger.Instance instance, double mouseX, double mouseY) {
        if (mouseX > RECIPE_WIDTH - 48 && mouseX < RECIPE_WIDTH - 3 && mouseY > SPACE_TOP + 17 && mouseY < SPACE_TOP + 91) {
            List<IFormattableTextComponent> list = new ArrayList<>();
            TooltipUtil.addLocationValues(list, instance.getPos());
            if (!list.isEmpty()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.botania.alf_portal.location").mergeStyle(TextFormatting.GOLD));
                tooltip.addAll(list);
            }
        }
    }
}
