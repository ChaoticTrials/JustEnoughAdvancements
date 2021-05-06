package de.melanx.jea.plugins.botania.special;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.plugins.botania.BotaniaItems;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import io.github.noeppi_noeppi.libx.render.RenderHelperBlock;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class AreaMineInfo implements ICriterionInfo<ImpossibleTrigger.Instance> {
    
    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "challenge/rank_ss_pick");
    public static final String CRITERION = "code_triggered";
    
    @Override
    public Class<ImpossibleTrigger.Instance> criterionClass() {
        return ImpossibleTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                BotaniaItems.shatterers(5, true, false)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, ((RECIPE_WIDTH - 18) / 2) - 10, SPACE_TOP + 70);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, ((RECIPE_WIDTH - 18) / 2) - 10, SPACE_TOP + 70);
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 60;
        float swing = animationTime >= 20 && animationTime <= 50 ? ((animationTime - 20) % 6) / 6f : 0;
        ItemStack stack = JeaRender.cycle(BotaniaItems.shatterers(5, true, false));
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        
        if (animationTime < 50) {
            float b = animationTime >= 20 ? (animationTime - 20) / 30f : 0;
            int breakProgress = b <= 0 ? 0 : (int) Math.ceil(10 * b);
            BlockState state = Blocks.STONE.getDefaultState();
            BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
            int light = LightTexture.packLight(15, 15);
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH - 40, SPACE_TOP + 63, 0);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-13));
            JeaRender.normalize(matrixStack);
            JeaRender.transformForEntityRenderSide(matrixStack, true, 0.6f);
            matrixStack.translate(-2, 0, -0.5);
            for (int y = 0; y < 7; y++) {
                for (int x = 0; x < 9; x++) {
                    //noinspection deprecation
                    brd.renderBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
                    RenderHelperBlock.renderBlockBreak(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY, breakProgress);
                    matrixStack.translate(1, 0, 0);
                }
                matrixStack.translate(-9, 1, 0);
            }
            Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish();
            Minecraft.getInstance().getRenderTypeBuffers().getCrumblingBufferSource().finish();
            matrixStack.pop();
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
