package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.ConstructBeaconTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class ConstructBeaconInfo implements ICriterionInfo<ConstructBeaconTrigger.Instance> {

    @Override
    public Class<ConstructBeaconTrigger.Instance> criterionClass() {
        return ConstructBeaconTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ConstructBeaconTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(Items.BEACON)),
                BlockTags.BEACON_BASE_BLOCKS.getAllElements().stream().map(ItemStack::new).collect(Collectors.toList())
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ConstructBeaconTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 3, SPACE_TOP + 3);
        layout.getItemStacks().init(1, true, 3, SPACE_TOP + 24);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ConstructBeaconTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 3, SPACE_TOP + 3);
        JeaRender.slotAt(matrixStack, 3, SPACE_TOP + 24);
        MinMaxBounds.IntBound level = MinMaxBounds.IntBound.atLeast(1);
        if (!instance.level.isUnbounded()) {
            level = instance.level;
        }
        ITextComponent text = new TranslationTextComponent("jea.item.tooltip.beacon.level", IngredientUtil.text(level));
        mc.fontRenderer.drawText(matrixStack, text, 2, SPACE_TOP + RECIPE_HEIGHT - 2 - mc.fontRenderer.FONT_HEIGHT, 0x000000);
        int renderLevel = IngredientUtil.getExampleValue(level).orElse(1);
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 60, SPACE_TOP + RECIPE_HEIGHT - 30, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderFront(matrixStack, true, 4f / (1 + (2 * renderLevel)));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(20));
        this.renderBeacon(matrixStack, buffer, mc, renderLevel);
        matrixStack.pop();
    }
    
    private void renderBeacon(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, int level) {
        if (!BlockTags.BEACON_BASE_BLOCKS.getAllElements().isEmpty()) {
            BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
            BlockState beacon = Blocks.BEACON.getDefaultState();
            BlockState base = JeaRender.cycle(BlockTags.BEACON_BASE_BLOCKS.getAllElements()).getDefaultState();
            int light = LightTexture.packLight(15, 15);
            int overlay = OverlayTexture.NO_OVERLAY;
            matrixStack.push();
            for (int i = level; i > 0; i--) {
                renderBeaconLayer(matrixStack, buffer, mc, brd, base, light, overlay, i);
                matrixStack.translate(0, 1, 0);
            }
            matrixStack.translate(-0.5, 0, -0.5);
            //noinspection deprecation
            brd.renderBlock(beacon, matrixStack, buffer, light, overlay);
            matrixStack.pop();
        }
    }
    
    private static void renderBeaconLayer(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, BlockRendererDispatcher brd, BlockState state, int light, int overlay, int layer) {
        matrixStack.push();
        matrixStack.translate(-0.5 - layer, 0, -0.5 - layer);
        for (int x = -layer; x <= layer; x++) {
            for (int z = -layer; z <= layer; z++) {
                //noinspection deprecation
                brd.renderBlock(state, matrixStack, buffer, light, overlay);
                matrixStack.translate(0, 0, 1);
            }
            matrixStack.translate(1, 0, -(2 * layer) - 1);
        }
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ConstructBeaconTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
