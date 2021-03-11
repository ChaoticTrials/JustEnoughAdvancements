package de.melanx.jea.plugins.vanilla.special;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.ItemDurabilityTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class RideStriderInfo implements ICriterionInfo<ItemDurabilityTrigger.Instance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("minecraft", "nether/ride_strider");
    public static final String CRITERION = "used_warped_fungus_on_a_stick";
    
    @Override
    public Class<ItemDurabilityTrigger.Instance> criterionClass() {
        return ItemDurabilityTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.Instance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(63, SPACE_TOP + 90, 3);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 1.4f);
        RenderEntityCache.renderPlainEntity(mc, EntityType.STRIDER, matrixStack, buffer);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(55, SPACE_TOP + 78, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.2f);
        SteveRender.defaultPose(mc);
        SteveRender.sitting(true);
        SteveRender.setEquipmentHand(mc, new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK));
        SteveRender.renderSteveStatic(mc, matrixStack, buffer);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ItemDurabilityTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
