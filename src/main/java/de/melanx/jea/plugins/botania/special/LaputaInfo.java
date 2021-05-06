package de.melanx.jea.plugins.botania.special;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.TooltipUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;

import java.util.ArrayList;
import java.util.List;

public class LaputaInfo implements ICriterionInfo<UseItemSuccessTrigger.Instance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "challenge/l20_shard_use");
    public static final String CRITERION = "use_l20_shard";

    @Override
    public Class<UseItemSuccessTrigger.Instance> criterionClass() {
        return UseItemSuccessTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.fromItemPredicate(instance.getItem())
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 48, SPACE_TOP + RECIPE_HEIGHT - 20);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 48, SPACE_TOP + RECIPE_HEIGHT - 20);
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 50;
        float swing = animationTime < 6 ? animationTime / 6f : 0;
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.getItem()));
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderFront(matrixStack, false, 2.7f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        float upFloat = (((ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 10) / 10f) * 1.3f;
        upFloat = MathHelper.clamp(upFloat, 0, 1);
        int upAmount = ((ClientTickHandler.ticksInGame) / 10) % 5;
        BlockState state = Blocks.GRASS_BLOCK.getDefaultState();
        int light = LightTexture.packLight(15, 15);
        BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 45, SPACE_TOP + 68, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-13));
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderFront(matrixStack, true, 0.9f);
        matrixStack.translate(-2, 0, 0);
        for (int i = 0; i < 5; i++) {
            matrixStack.push();
            if (i < upAmount) {
                matrixStack.translate(0, 4.2, 0);
            } else if (i == upAmount) {
                matrixStack.translate(0, 4.2 * upFloat, 0);
            }
            //noinspection deprecation
            brd.renderBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();
            matrixStack.translate(1, 0, 0);
        }
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, double mouseX, double mouseY) {
        if (mouseX > 10 && mouseX < 51 && mouseY > SPACE_TOP + 7 && mouseY < SPACE_TOP + 90) {
            List<IFormattableTextComponent> list = new ArrayList<>();
            TooltipUtil.addLocationValues(list, instance.getLocation());
            if (!list.isEmpty()) {
                tooltip.add(new TranslationTextComponent("jea.item.tooltip.location.any_title").mergeStyle(TextFormatting.GOLD));
                tooltip.addAll(list);
            }
        }
    }
}
