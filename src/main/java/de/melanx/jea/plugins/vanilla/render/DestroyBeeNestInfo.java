package de.melanx.jea.plugins.vanilla.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.BeeNestDestroyedTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class DestroyBeeNestInfo implements ICriterionInfo<BeeNestDestroyedTrigger.Instance> {
    
    @Override
    public Class<BeeNestDestroyedTrigger.Instance> criterionClass() {
        return BeeNestDestroyedTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, BeeNestDestroyedTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.ingredients(instance.block, Blocks.BEEHIVE, Blocks.BEE_NEST),
                IngredientUtil.fromItemPredicate(instance.itemPredicate, Items.IRON_HOE)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, BeeNestDestroyedTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_BLOCK_BREAK_SLOW, 82, SPACE_TOP + 42, 48, 48, 0, 0);
        layout.getItemStacks().init(1, true, 55, SPACE_TOP + 74);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, BeeNestDestroyedTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 55, SPACE_TOP + 74);
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.itemPredicate, Items.IRON_HOE));
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(((ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 6) / 6, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        if (!instance.beesContained.isUnbounded()) {
            matrixStack.push();
            matrixStack.translate(95, SPACE_TOP + 34, 0);
            JeaRender.normalize(matrixStack);
            JeaRender.transformForEntityRenderSide(matrixStack, false, 1);
            RenderEntityCache.renderPlainEntity(mc, EntityType.BEE, matrixStack, buffer);
            matrixStack.pop();
            ITextComponent text = new StringTextComponent(": ").append(IngredientUtil.text(instance.beesContained));
            mc.fontRenderer.func_243248_b(matrixStack, text, 105, SPACE_TOP + 25, 0x000000);
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, BeeNestDestroyedTrigger.Instance instance, double mouseX, double mouseY) {
        //
    }
}
