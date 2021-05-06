package de.melanx.jea.plugins.botania.special;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.plugins.botania.BotaniaJea;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public class LuminizerInfo implements ICriterionInfo<ImpossibleTrigger.Instance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "main/luminizer_ride");
    public static final String CRITERION = "code_triggered";

    @Override
    public Class<ImpossibleTrigger.Instance> criterionClass() {
        return ImpossibleTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(ModBlocks.lightRelayDefault), new ItemStack(ModBlocks.lightRelayDetector), new ItemStack(ModBlocks.lightRelayFork), new ItemStack(ModBlocks.lightRelayToggle)),
                ImmutableList.of(new ItemStack(ModBlocks.lightRelayDefault), new ItemStack(ModBlocks.lightRelayDetector), new ItemStack(ModBlocks.lightRelayFork), new ItemStack(ModBlocks.lightRelayToggle))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, BotaniaJea.LUMINIZER, 5, SPACE_TOP + RECIPE_HEIGHT - 29, 24, 24, 0, 0);
        layout.getItemStacks().init(1, true, BotaniaJea.LUMINIZER, RECIPE_WIDTH - 29, SPACE_TOP + RECIPE_HEIGHT - 29, 24, 24, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, double mouseX, double mouseY) {
        double length = RECIPE_WIDTH - (2 * 20);
        double b = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % length;
        double x = Math.abs((2 * b) - length);
        boolean back = (2 * b) - length < 0;
        matrixStack.push();
        matrixStack.translate(20 + x, SPACE_TOP + 85, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, back, 2f);
        SteveRender.defaultPose(mc);
        SteveRender.limbSwing(((float) (Math.sin((ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) / 3) * 0.5)));
        SteveRender.clearEquipment(mc);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, double mouseX, double mouseY) {
        //
    }
}
