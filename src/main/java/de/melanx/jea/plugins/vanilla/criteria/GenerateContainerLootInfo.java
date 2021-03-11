package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderMisc;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.PlayerGeneratesContainerLootTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Objects;

public class GenerateContainerLootInfo implements ICriterionInfo<PlayerGeneratesContainerLootTrigger.Instance> {

    private final ChestTileEntity tile = new ChestTileEntity();
    private TileEntityRenderer<ChestTileEntity> tileRender = null;
    
    @Override
    public Class<PlayerGeneratesContainerLootTrigger.Instance> criterionClass() {
        return PlayerGeneratesContainerLootTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, PlayerGeneratesContainerLootTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, PlayerGeneratesContainerLootTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, PlayerGeneratesContainerLootTrigger.Instance instance, double mouseX, double mouseY) {
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 45;
        float chestOpen;
        if (animationTime < 10) {
            chestOpen = animationTime / 10f;
        } else if (animationTime < 15) {
            chestOpen = 1;
        } else if (animationTime < 25) {
            chestOpen = 1 - ((animationTime - 15) / 10f);
        } else {
            chestOpen = 0;
        }
        
        matrixStack.push();
        //noinspection IntegerDivisionInFloatingPointContext
        matrixStack.translate(RECIPE_WIDTH / 2, RECIPE_HEIGHT + 8, 0);
        JeaRender.normalize(matrixStack);
        matrixStack.scale(2.4f, -2.4f, 2.4f);
        double translate = Math.sqrt(2) / 2;
        matrixStack.translate(translate, 0, translate);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(22.5f));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180 + 45));
        this.tile.setWorldAndPos(Objects.requireNonNull(mc.world), JeaRender.BELOW_WORLD);
        this.tile.cachedBlockState = Blocks.CHEST.getDefaultState();
        this.tile.lidAngle = 0.5f * chestOpen;
        this.tile.prevLidAngle = this.tile.lidAngle;
        if (this.tileRender == null) {
            this.tileRender = TileEntityRendererDispatcher.instance.getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            this.tileRender.render(this.tile, mc.getRenderPartialTicks(), matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        }
        matrixStack.pop();

        IFormattableTextComponent text1 = new TranslationTextComponent("jea.item.tooltip.generate_loot");
        IFormattableTextComponent text2 = new StringTextComponent(IngredientUtil.rl(instance.generatedLoot));
        int width1 = mc.fontRenderer.getStringPropertyWidth(text1);
        int width2 = mc.fontRenderer.getStringPropertyWidth(text2);
        //noinspection IntegerDivisionInFloatingPointContext
        mc.fontRenderer.func_243248_b(matrixStack, text1, (RECIPE_WIDTH / 2) - (width1 / 2), SPACE_TOP + 71, 0x000000);
        //noinspection IntegerDivisionInFloatingPointContext
        mc.fontRenderer.func_243248_b(matrixStack, text2, (RECIPE_WIDTH / 2) - (width2 / 2), SPACE_TOP + 73 + mc.fontRenderer.FONT_HEIGHT, 0x000000);
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, PlayerGeneratesContainerLootTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
