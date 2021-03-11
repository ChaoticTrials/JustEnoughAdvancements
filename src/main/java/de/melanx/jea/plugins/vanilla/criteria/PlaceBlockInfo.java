package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.PlacedBlockTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class PlaceBlockInfo implements ICriterionInfo<PlacedBlockTrigger.Instance> {

    @Override
    public Class<PlacedBlockTrigger.Instance> criterionClass() {
        return PlacedBlockTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, PlacedBlockTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.ingredients(instance.block, Blocks.COBBLESTONE),
                IngredientUtil.fromItemPredicate(instance.item, instance.block == null ? Items.COBBLESTONE : instance.block)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, PlacedBlockTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_BLOCK_APPEARING, 92, SPACE_TOP + 42, 48, 48, 0, 0);
        layout.getItemStacks().init(1, true, 55, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, PlacedBlockTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 55, SPACE_TOP + 72);
        float animationTime = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()) % 40;
        float swing = 0;
        ItemStack stack = ItemStack.EMPTY;
        if (animationTime < 20) {
            stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, instance.block == null ? Items.COBBLESTONE : instance.block));
        }
        if (animationTime > 17 && animationTime < 23) {
            swing = (animationTime - 17) / 6f;
        }
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, PlacedBlockTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
