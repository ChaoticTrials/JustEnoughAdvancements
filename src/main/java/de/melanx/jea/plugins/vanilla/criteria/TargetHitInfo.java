package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.LargeBlockIngredientRender;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.LootUtil;
import de.melanx.jea.util.ProjectileUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.TargetHitTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TargetHitInfo implements ICriterionInfo<TargetHitTrigger.Instance> {

    // Store data from render method.
    // Required as the projectile is rendered in the ingredient render.
    // TODO needs a better solution as this will break if two recipes are rendered at the same time.
    // Also this is always delayed one frame.
    private int signalStrength = 15;
    private ItemStack projectileStack = null;
    
    // In `draw` we can not render sth aver the block. So we have a custom ingredient render here.
    private final LargeBlockIngredientRender blockRender = new LargeBlockIngredientRender() {
        @Override
        public void render(@Nonnull MatrixStack matrixStack, int x, int y, @Nullable ItemStack stack) {
            super.render(matrixStack, x, y, stack);
            Minecraft mc = Minecraft.getInstance();
            if (TargetHitInfo.this.projectileStack != null) {
                matrixStack.push();
                matrixStack.translate(x, y, 0);
            
                float animationTime = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()) % 30;
                float precisionFactor = animationTime > 15 ? -1 : 1;
                if (animationTime >= 15) animationTime -= 15;
                float arrowDist = Math.min(1, animationTime / 10f);
                float arrowDown = 0.4f * (arrowDist * arrowDist);
                int arrowPrecisionInt = 15 - TargetHitInfo.this.signalStrength;
                float arrowPrecision = precisionFactor * arrowPrecisionInt;
                matrixStack.push();
                matrixStack.translate(-76 + (80 * arrowDist), 20 + (20 * arrowDown) + arrowPrecision, 100);
                float angle = (float) Math.atan(0.2 * arrowDist);
                ProjectileUtil.rotateCenter(matrixStack, TargetHitInfo.this.projectileStack, Vector3f.ZP.rotation(angle));
                matrixStack.translate(-8, -2.5, 0);
                ProjectileUtil.renderProjectile(matrixStack, TargetHitInfo.this.projectileStack);
                matrixStack.pop();

                matrixStack.pop();
                mc.getRenderTypeBuffers().getBufferSource().finish();
            }
        }
    };

    
    @Override
    public Class<TargetHitTrigger.Instance> criterionClass() {
        return TargetHitTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, TargetHitTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(Items.TARGET)),
                ProjectileUtil.getProjectileStack(Minecraft.getInstance(), LootUtil.asEntity(instance.projectile))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, TargetHitTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, this.blockRender, 82, SPACE_TOP + 25, 48, 48, 0, 0);
        layout.getItemStacks().init(1, true, 35, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, TargetHitTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 35, SPACE_TOP + 72);
        this.signalStrength = IngredientUtil.getExampleValue(instance.signalStrength).orElse(1);
        this.projectileStack = JeaRender.cycle(ProjectileUtil.getProjectileStack(mc, LootUtil.asEntity(instance.projectile)));
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, TargetHitTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
