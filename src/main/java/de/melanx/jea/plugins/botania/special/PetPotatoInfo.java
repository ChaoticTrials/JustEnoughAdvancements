package de.melanx.jea.plugins.botania.special;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.plugins.botania.BotaniaJea;
import de.melanx.jea.util.ItemUtil;
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
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public class PetPotatoInfo implements ICriterionInfo<ImpossibleTrigger.Instance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "main/tiny_potato_pet");
    public static final String CRITERION = "code_triggered";

    @Override
    public Class<ImpossibleTrigger.Instance> criterionClass() {
        return ImpossibleTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(ModBlocks.tinyPotato))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, BotaniaJea.TINY_POTATO, (RECIPE_WIDTH / 2) + 10, SPACE_TOP + 36, 48, 48, 0, 0);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, double mouseX, double mouseY) {
        List<ItemStack> stacks = ItemUtil.creators();
        int animationTimeTicks = ClientTickHandler.ticksInGame % 40;
        int idx = (ClientTickHandler.ticksInGame / 40) % stacks.size();
        ItemStack stack = animationTimeTicks < 20 ? stacks.get(idx) : ItemStack.EMPTY;
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 40;
        float swing = animationTime >= 14 && animationTime <= 20 ? (animationTime - 14) / 6f : 0;
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.7f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ImpossibleTrigger.Instance instance, double mouseX, double mouseY) {

    }
}