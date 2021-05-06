package de.melanx.jea.plugins.botania.special;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class PinkinatorInfo implements ICriterionInfo<UseItemSuccessTrigger.Instance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("botania", "challenge/pinkinator");
    public static final String CRITERION = "use_pinkinator";

    @Override
    public Class<UseItemSuccessTrigger.Instance> criterionClass() {
        return UseItemSuccessTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.fromItemPredicate(instance.getItem(), true, ModItems.pinkinator)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH - 18) / 2, SPACE_TOP + 70);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, (RECIPE_WIDTH - 18) / 2, SPACE_TOP + 70);
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 60;
        float swing = animationTime >= 27 && animationTime <= 33 ? (animationTime - 27) / 6f : 0;
        ItemStack stack = animationTime > 30 ? ItemStack.EMPTY : JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.getItem(), true, ModItems.pinkinator));
        EntityType<?> entity = animationTime > 30 ? ModEntities.PINK_WITHER : EntityType.WITHER;
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.7f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, true, 2.7f);
        RenderEntityCache.renderPlainEntity(mc, entity, matrixStack, buffer);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, UseItemSuccessTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
