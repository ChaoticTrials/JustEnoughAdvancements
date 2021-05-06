package de.melanx.jea.plugins.botania.special;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.plugins.botania.BotaniaItems;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public class FormEnchanterInfo implements ICriterionInfo<ImpossibleTrigger.Instance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "main/enchanter_make");
    public static final String CRITERION = "code_triggered";

    @Override
    public Class<ImpossibleTrigger.Instance> criterionClass() {
        return ImpossibleTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(ModBlocks.enchanter))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    @SuppressWarnings("deprecation")
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, double mouseX, double mouseY) {
        float animationTime = (7 + ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 50;
        float swing = animationTime <= 6 ? animationTime / 6f : 0;
        matrixStack.push();
        matrixStack.translate(27, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, JeaRender.cycle(BotaniaItems.wands()));
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        BlockState state = (animationTime > 3 && animationTime <= 33 ? ModBlocks.enchanter : Blocks.LAPIS_BLOCK).getDefaultState();
        BlockState obsidian = Blocks.OBSIDIAN.getDefaultState();
        BlockState grass = Blocks.GRASS_BLOCK.getDefaultState();
        BlockState flower = ModBlocks.getFlower(JeaRender.cycle(DyeColor.class)).getDefaultState();
        BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
        int light = LightTexture.packLight(15, 15);
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 60, SPACE_TOP + 50, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-13));
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 1f);
        brd.renderBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
        matrixStack.translate(-2, -1, -2);
        for (int z = 0; z < 5; z++) {
            for (int x = 0; x < 5; x++) {
                if ((x != 0 && x != 4) || (z != 0 && z != 4)) {
                    if ((x == 1 || x == 3) && (z == 1 || z == 3)) {
                        brd.renderBlock(grass, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
                        matrixStack.translate(0, 1, 0);
                        brd.renderBlock(flower, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
                        matrixStack.translate(0, -1, 0);
                    } else {
                        brd.renderBlock(obsidian, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
                    }
                }
                matrixStack.translate(1, 0, 0);
            }
            matrixStack.translate(-5, 0, 1);
        }
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
