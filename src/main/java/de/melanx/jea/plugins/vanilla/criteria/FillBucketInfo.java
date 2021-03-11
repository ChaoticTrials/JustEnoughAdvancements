package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SpecialModels;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.FilledBucketTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.List;

public class FillBucketInfo implements ICriterionInfo<FilledBucketTrigger.Instance> {
    
    private Method fishBucketSupplier = null;
    
    @Override
    public Class<FilledBucketTrigger.Instance> criterionClass() {
        return FilledBucketTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, FilledBucketTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(new ItemStack(Items.BUCKET)),
                IngredientUtil.fromItemPredicate(instance.item, Items.WATER_BUCKET, Items.LAVA_BUCKET)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, FilledBucketTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 91, SPACE_TOP + 15);
        layout.getItemStacks().init(1, true, 109, SPACE_TOP + 15);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, FilledBucketTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 91, SPACE_TOP + 15);
        JeaRender.slotAt(matrixStack, 109, SPACE_TOP + 15);
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 52f;
        ItemStack fullBucket = getStack(IngredientUtil.fromItemPredicate(instance.item));
        float swing;
        boolean insideBucket;
        EntityType<?> fish = null;
        FluidStack fluid = FluidStack.EMPTY;
        if (animationTime < 20) {
            swing = 0;
            insideBucket = false;
        } else if (animationTime < 26) {
            swing = (animationTime - 20) / 6f;
            insideBucket = swing > 0.5;
        } else if (animationTime < 46) {
            swing = 0;
            insideBucket = true;
        } else {
            swing = (animationTime - 46) / 6f;
            insideBucket = swing < 0.5;
        }
        
        if (!insideBucket) {
            fluid = new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);
            if (fullBucket.getItem() instanceof FishBucketItem) {
                fish = this.getFish((FishBucketItem) fullBucket.getItem());
            }
            if (fullBucket.getItem() instanceof BucketItem) {
                fluid = new FluidStack(((BucketItem) fullBucket.getItem()).getFluid(), FluidAttributes.BUCKET_VOLUME);
            } else {
                LazyOptional<IFluidHandlerItem> tankCap = fullBucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
                if (tankCap.isPresent()) {
                    //noinspection OptionalGetWithoutIsPresent
                    IFluidHandlerItem tank = tankCap.resolve().get();
                    fluid = tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE).copy();
                }
            }
        }
        
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, insideBucket ? fullBucket : new ItemStack(Items.BUCKET));
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH- 40, SPACE_TOP + 80, 0);
        JeaRender.normalize(matrixStack);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-18));
        JeaRender.transformForEntityRenderFront(matrixStack, true, 5);
        SpecialModels.renderFluidPool(matrixStack, buffer, mc, fluid, fish);
        matrixStack.pop();
    }
    
    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, FilledBucketTrigger.Instance instance, double mouseX, double mouseY) {

    }

    private static ItemStack getStack(List<ItemStack> stacks) {
        // item stack should switch after a whole animation and not while its animated.
        int idx = ((ClientTickHandler.ticksInGame - 2) / 52) % stacks.size();
        return stacks.get(idx);
    }
    
    @Nullable
    private EntityType<?> getFish(FishBucketItem bucket) {
        try {
            if (this.fishBucketSupplier == null) {
                this.fishBucketSupplier = FishBucketItem.class.getDeclaredMethod("getFishType");
                this.fishBucketSupplier.setAccessible(true);
            }
            return (EntityType<?>) this.fishBucketSupplier.invoke(bucket);
        } catch (ReflectiveOperationException e) {
            this.fishBucketSupplier = null;
            return bucket.fishType;
        }
    }
}
