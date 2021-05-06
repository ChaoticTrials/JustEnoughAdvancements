package de.melanx.jea.plugins.botania.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class RelicRedeemInfo implements ICriterionInfo<RelicBindTrigger.Instance> {

    @Override
    public Class<RelicBindTrigger.Instance> criterionClass() {
        return RelicBindTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, RelicBindTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(ModItems.dice)),
                IngredientUtil.fromItemPredicate(instance.getPredicate(), ModItems.odinRing, ModItems.thorRing, ModItems.lokiRing, ModItems.kingKey, ModItems.flugelEye, ModItems.infiniteFruit)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, RelicBindTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 70, SPACE_TOP + 68);
        layout.getItemStacks().init(1, true, 124, SPACE_TOP + 68);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, RelicBindTrigger.Instance instance, double mouseX, double mouseY) {
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 80;
        ItemStack stack = animationTime > 60 ? ItemStack.EMPTY : animationTime < 30 ? new ItemStack(ModItems.dice) : JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.getPredicate(), ModItems.odinRing, ModItems.thorRing, ModItems.lokiRing, ModItems.kingKey, ModItems.flugelEye, ModItems.infiniteFruit));
        float swing = animationTime >= 27 && animationTime <= 33 ? (animationTime - 27) / 6f : 0;
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        
        JeaRender.slotAt(matrixStack, 70, SPACE_TOP + 68);
        JeaRender.slotAt(matrixStack, 124, SPACE_TOP + 68);
        RenderSystem.enableBlend();
        JustEnoughAdvancementsJEIPlugin.getArrow(false).draw(matrixStack, 90, SPACE_TOP + 69);
        RenderSystem.disableBlend();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, RelicBindTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
