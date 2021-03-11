package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.PlayerEntityInteractionTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class EntityInteractionInfo implements ICriterionInfo<PlayerEntityInteractionTrigger.Instance> {

    @Override
    public Class<PlayerEntityInteractionTrigger.Instance> criterionClass() {
        return PlayerEntityInteractionTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, PlayerEntityInteractionTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.fromItemPredicate(instance.stack, Items.STRUCTURE_VOID)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, PlayerEntityInteractionTrigger.Instance instance, IIngredients ii) {
        
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, PlayerEntityInteractionTrigger.Instance instance, double mouseX, double mouseY) {
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 40;
        float swing = 0;
        ItemStack stack = ItemStack.EMPTY;
        DefaultEntityProperties properties = DefaultEntityProperties.DEFAULT;
        if (animationTime < 20) {
            stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.stack, Items.STRUCTURE_VOID));
        } else {
            properties = new DefaultEntityProperties(null, false, false, false, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.stack, Items.STRUCTURE_VOID)), 0, 0, 0, 0, 0);
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
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        RenderEntityCache.renderEntity(mc, instance.entity, matrixStack, buffer, JeaRender.entityRenderFront(true, 2.6f), properties);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, PlayerEntityInteractionTrigger.Instance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.entity, RECIPE_WIDTH - 30, SPACE_TOP + 90, JeaRender.normalScale(2.6f), mouseX, mouseY);
    }
}
