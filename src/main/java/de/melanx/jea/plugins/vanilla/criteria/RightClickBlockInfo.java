package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.*;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.RightClickBlockWithItemTrigger;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.List;

public class RightClickBlockInfo implements ICriterionInfo<RightClickBlockWithItemTrigger.Instance> {

    private static final ResourceLocation SAFELY_HARVEST_HONEY_ID = new ResourceLocation("minecraft", "husbandry/safely_harvest_honey");
    private static final ResourceLocation FILL_RESPAWN_ANCHOR_ID = new ResourceLocation("minecraft", "nether/charge_respawn_anchor");
    private static final ResourceLocation USE_LODESTONE_ID = new ResourceLocation("minecraft", "nether/use_lodestone");
    
    // Store the blockstate JEI would render at one time for one criterion.
    // Required so tooltip and block match up and you get to the correct recipe
    // page when clicking the block
    // TODO needs a better solution as this will break if two recipes are rendered at the same time.
    // Also this is always delayed one frame.
    private BlockState state;
    private final LargeBlockEmptyIngredientRender blockRender = new LargeBlockEmptyIngredientRender(s -> this.state = s);
    
    @Override
    public Class<RightClickBlockWithItemTrigger.Instance> criterionClass() {
        return RightClickBlockWithItemTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, RightClickBlockWithItemTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                IngredientUtil.getBlockIngredients(instance.location.block),
                IngredientUtil.fromItemPredicate(instance.stack, Items.STRUCTURE_VOID)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, RightClickBlockWithItemTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, this.blockRender, 92, SPACE_TOP + 42 - (int) Math.round(48 * RenderMisc.getYOffset(instance.location)), 48, 48, 0, 0);
        layout.getItemStacks().init(1, true, 55, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }
    
    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, RightClickBlockWithItemTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 55, SPACE_TOP + 72);
        float animationTime = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getRenderPartialTicks()) % 50;
        float swing = 0;
        ItemStack stack = null;
        Property<?> property = null;
        Comparable<?> value = null;
        if (animationTime > 17 && animationTime < 23) {
            swing = (animationTime - 17) / 6f;
        }
        if (animationTime <= 45 && animationTime > 20) {
            if (advancement.getId().equals(SAFELY_HARVEST_HONEY_ID)) {
                stack = new ItemStack(Items.HONEY_BOTTLE);
                property = BlockStateProperties.HONEY_LEVEL;
                value = 0;
            } else if (advancement.getId().equals(FILL_RESPAWN_ANCHOR_ID)) {
                property = BlockStateProperties.CHARGES;
                value = 4;
            } else if (advancement.getId().equals(USE_LODESTONE_ID)) {
                stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.stack, Items.STRUCTURE_VOID));
                CompoundNBT nbt = stack.getOrCreateTag();
                nbt.putBoolean("LodestoneTracked", false);
                nbt.put("LodestonePos", NBTUtil.writeBlockPos(BlockPos.ZERO));
                stack.setTag(nbt);
            }
            if (stack == null) {
                stack = ItemStack.EMPTY;
            }
        } else {
            if (advancement.getId().equals(SAFELY_HARVEST_HONEY_ID)) {
                property = BlockStateProperties.HONEY_LEVEL;
                value = 5;
            } else if (advancement.getId().equals(FILL_RESPAWN_ANCHOR_ID)) {
                property = BlockStateProperties.CHARGES;
                value = 0;
            }
            //noinspection ConstantConditions
            if (stack == null) {
                stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.stack, Items.STRUCTURE_VOID));
            }
        }
        if (animationTime > 40) {
            stack = ItemStack.EMPTY;
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
        matrixStack.translate(92, SPACE_TOP + 42, 0);
        LargeBlockIngredientRender.modifyMatrixStack(matrixStack);
        this.renderLocationPredicateUnchecked(matrixStack, buffer, mc, instance.location, property, value);
        matrixStack.pop();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void renderLocationPredicateUnchecked(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, LocationPredicate predicate, @Nullable Property<?> property, @CheckForNull Comparable<?> value) {
        RenderMisc.renderLocationPredicate(matrixStack, buffer, mc, predicate, this.state, (Property) property, (Comparable) value);
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, RightClickBlockWithItemTrigger.Instance instance, double mouseX, double mouseY) {

    }
}
