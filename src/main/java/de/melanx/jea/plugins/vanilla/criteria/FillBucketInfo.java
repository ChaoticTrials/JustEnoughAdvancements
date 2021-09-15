package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
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
import net.minecraft.advancements.critereon.FilledBucketTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.List;

public class FillBucketInfo implements ICriterionInfo<FilledBucketTrigger.TriggerInstance> {
    
    private Method fishBucketSupplier = null;
    
    @Override
    public Class<FilledBucketTrigger.TriggerInstance> criterionClass() {
        return FilledBucketTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, FilledBucketTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(new ItemStack(Items.BUCKET)),
                IngredientUtil.fromItemPredicate(instance.item, Items.WATER_BUCKET, Items.LAVA_BUCKET)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, FilledBucketTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 91, SPACE_TOP + 15);
        layout.getItemStacks().init(1, true, 109, SPACE_TOP + 15);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, FilledBucketTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, 91, SPACE_TOP + 15);
        JeaRender.slotAt(poseStack, 109, SPACE_TOP + 15);
        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 52f;
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
            if (fullBucket.getItem() instanceof MobBucketItem mobBucket) {
                fish = this.getBucketMob(mobBucket);
            }
            if (fullBucket.getItem() instanceof BucketItem bucket) {
                fluid = new FluidStack(bucket.getFluid(), FluidAttributes.BUCKET_VOLUME);
            } else {
                LazyOptional<IFluidHandlerItem> tankCap = fullBucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
                if (tankCap.isPresent()) {
                    //noinspection OptionalGetWithoutIsPresent
                    IFluidHandlerItem tank = tankCap.resolve().get();
                    fluid = tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE).copy();
                }
            }
        }
        
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, insideBucket ? fullBucket : new ItemStack(Items.BUCKET));
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH- 40, SPACE_TOP + 80, 0);
        JeaRender.normalize(poseStack);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-18));
        JeaRender.transformForEntityRenderFront(poseStack, true, 5);
        SpecialModels.renderFluidPool(poseStack, buffer, mc, fluid, fish);
        poseStack.popPose();
    }
    
    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, FilledBucketTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }

    private static ItemStack getStack(List<ItemStack> stacks) {
        // item stack should switch after a whole animation and not while its animated.
        int idx = ((ClientTickHandler.ticksInGame - 2) / 52) % stacks.size();
        return stacks.get(idx);
    }
    
    @Nullable
    private EntityType<?> getBucketMob(MobBucketItem bucket) {
        try {
            if (this.fishBucketSupplier == null) {
                this.fishBucketSupplier = MobBucketItem.class.getDeclaredMethod("getFishType");
                this.fishBucketSupplier.setAccessible(true);
            }
            return (EntityType<?>) this.fishBucketSupplier.invoke(bucket);
        } catch (ReflectiveOperationException e) {
            this.fishBucketSupplier = null;
            return bucket.type;
        }
    }
}
