package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.ShotCrossbowTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class ShootCrossbowInfo implements ICriterionInfo<ShotCrossbowTrigger.Instance> {

    public static final ResourceLocation AROW_TEXTURE = new ResourceLocation("minecraft", "textures/entity/arrow.png");
    
    @Override
    public Class<ShotCrossbowTrigger.Instance> criterionClass() {
        return ShotCrossbowTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ShotCrossbowTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.fromItemPredicate(instance.itemPredicate, Items.CROSSBOW)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ShotCrossbowTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 50, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ShotCrossbowTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 50, SPACE_TOP + 72);
        float animationTime = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()) % 66;
        ItemStack stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.itemPredicate, Items.CROSSBOW));
        int useTick;
        if (animationTime < 30) {
            useTick = 0;
            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.putBoolean("Charged", false);
            stack.setTag(nbt);
        } else if (animationTime < 58) {
            useTick = Math.max(1, 25 - (int) Math.ceil(animationTime - 30));
            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.putBoolean("Charged", false);
            stack.setTag(nbt);
        } else {
            useTick = 0;
            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.putBoolean("Charged", true);
            stack.setTag(nbt);
        }
        matrixStack.push();
        matrixStack.translate(25, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.use(useTick, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        if (animationTime < 10) {
            // Function for how far the arrow travelled down: 0.2*x^2
            float arrowDist = animationTime / 10f;
            float arrowDown = 0.2f * (arrowDist * arrowDist);
            matrixStack.push();
            matrixStack.translate(45 + (80 * arrowDist), SPACE_TOP + 25 + (20 * arrowDown), 0);
            // Use the derivative of our arrow-don function to determine the rotation:
            // 0.4*x -> Angle is the arc sine of that value
            float angle = (float) Math.asin(0.4 * arrowDist);
            matrixStack.translate(10, 2.5, 0);
            matrixStack.rotate(Vector3f.ZP.rotation(angle));
            matrixStack.translate(-10, -2.5, 0);
            RenderSystem.enableBlend();
            JustEnoughAdvancementsJEIPlugin.getArrow().draw(matrixStack, 0, 0);
            RenderSystem.disableBlend();
            matrixStack.pop();
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ShotCrossbowTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
