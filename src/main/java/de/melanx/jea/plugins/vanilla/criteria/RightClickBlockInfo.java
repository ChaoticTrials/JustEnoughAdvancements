package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.*;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.ItemUsedOnBlockTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class RightClickBlockInfo implements ICriterionInfo<ItemUsedOnBlockTrigger.TriggerInstance> {

    private static final ResourceLocation SAFELY_HARVEST_HONEY_ID = new ResourceLocation("minecraft", "husbandry/safely_harvest_honey");
    private static final ResourceLocation FILL_RESPAWN_ANCHOR_ID = new ResourceLocation("minecraft", "nether/charge_respawn_anchor");
    private static final ResourceLocation USE_LODESTONE_ID = new ResourceLocation("minecraft", "nether/use_lodestone");
    private static final ResourceLocation GLOW_SIGN = new ResourceLocation("minecraft", "husbandry/make_a_sign_glow");
    
    // Store the blockstate JEI would render at one time for one criterion.
    // Required so tooltip and block match up and you get to the correct recipe
    // page when clicking the block
    // TODO needs a better solution as this will break if two recipes are rendered at the same time.
    // Also this is always delayed one frame.
    private BlockState state;
    private final LargeBlockEmptyIngredientRender blockRender = new LargeBlockEmptyIngredientRender(s -> this.state = s);

    private final SignBlockEntity sign = new SignBlockEntity(JeaRender.BELOW_WORLD, Blocks.OAK_SIGN.defaultBlockState());
    private BlockEntityRenderer<SignBlockEntity> signRenderer = null;
    
    @Override
    public Class<ItemUsedOnBlockTrigger.TriggerInstance> criterionClass() {
        return ItemUsedOnBlockTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ItemUsedOnBlockTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                IngredientUtil.getBlockIngredients(instance.location.block),
                IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ItemUsedOnBlockTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, this.blockRender, 92, SPACE_TOP + 42 - (int) Math.round(48 * RenderMisc.getYOffset(instance.location)), 48, 48, 0, 0);
        layout.getItemStacks().init(1, true, 55, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }
    
    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ItemUsedOnBlockTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, 55, SPACE_TOP + 72);
        float animationTime = (ClientTickHandler.ticksInGame + Minecraft.getInstance().getFrameTime()) % 50;
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
                property = BlockStateProperties.LEVEL_HONEY;
                value = 0;
            } else if (advancement.getId().equals(FILL_RESPAWN_ANCHOR_ID)) {
                property = BlockStateProperties.RESPAWN_ANCHOR_CHARGES;
                value = 4;
            } else if (advancement.getId().equals(USE_LODESTONE_ID)) {
                stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID));
                CompoundTag nbt = stack.getOrCreateTag();
                nbt.putBoolean("LodestoneTracked", false);
                nbt.put("LodestonePos", NbtUtils.writeBlockPos(BlockPos.ZERO));
                stack.setTag(nbt);
            }
            if (stack == null) {
                stack = ItemStack.EMPTY;
            }
        } else {
            if (advancement.getId().equals(SAFELY_HARVEST_HONEY_ID)) {
                property = BlockStateProperties.LEVEL_HONEY;
                value = 5;
            } else if (advancement.getId().equals(FILL_RESPAWN_ANCHOR_ID)) {
                property = BlockStateProperties.RESPAWN_ANCHOR_CHARGES;
                value = 0;
            }
            //noinspection ConstantConditions
            if (stack == null) {
                stack = JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID));
            }
        }
        if (animationTime > 40) {
            stack = ItemStack.EMPTY;
        }
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, stack);
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(92, SPACE_TOP + 42, 0);
        LargeBlockIngredientRender.modifyPoseStack(poseStack);
        if (advancement.getId().equals(GLOW_SIGN)) {
            if (this.state != null) {
                boolean glowing = animationTime <= 45 && animationTime > 20;
                this.sign.setLevel(Objects.requireNonNull(Minecraft.getInstance().level));
                this.sign.blockState = this.state;
                this.sign.setMessage(1, new TextComponent("Hello, World!"));
                this.sign.setHasGlowingText(glowing);
                if (this.signRenderer == null) {
                    this.signRenderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(this.sign);
                }
                if (this.signRenderer != null) {
                    poseStack.pushPose();
                    poseStack.scale(1.3f, 1.3f, 1.3f);
                    poseStack.translate(0.5, 0, 0.5);
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                    poseStack.translate(-0.5, -0.2, -0.7);
                    this.signRenderer.render(this.sign, 0, poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
                    poseStack.popPose();
                }
            }
        } else {
            this.renderLocationPredicateUnchecked(poseStack, buffer, mc, instance.location, property, value);
        }
        poseStack.popPose();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void renderLocationPredicateUnchecked(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, LocationPredicate predicate, @Nullable Property<?> property, @CheckForNull Comparable<?> value) {
        RenderMisc.renderLocationPredicate(poseStack, buffer, mc, predicate, this.state, (Property) property, (Comparable) value);
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ItemUsedOnBlockTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
