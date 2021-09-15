package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.EntityTransformation;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.util.LootUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class SummonEntityInfo implements ICriterionInfo<SummonedEntityTrigger.TriggerInstance> {

    private final SkullBlockEntity tile = new SkullBlockEntity(JeaRender.BELOW_WORLD, Blocks.SKELETON_SKULL.defaultBlockState());
    private BlockEntityRenderer<SkullBlockEntity> tileRender = null;

    @Override
    public Class<SummonedEntityTrigger.TriggerInstance> criterionClass() {
        return SummonedEntityTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, SummonedEntityTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, this.getMaterials(this.getType(instance.entity)));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, SummonedEntityTrigger.TriggerInstance instance, IIngredients ii) {
        int size = ii.getInputs(VanillaTypes.ITEM).size();
        int x = RECIPE_WIDTH - 5 - (18 * size);
        for (int i = 0; i < size; i++) {
            layout.getItemStacks().init(i, true, x + (18 * i), SPACE_TOP + RECIPE_HEIGHT - 23);
        }
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, SummonedEntityTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        @Nullable
        EntityType<?> type = this.getType(instance.entity);
        int size = this.getMaterials(type).size();
        int x = RECIPE_WIDTH - 5 - (18 * size);
        for (int i = 0; i < size; i++) {
            JeaRender.slotAt(poseStack, x + (18 * i), SPACE_TOP + RECIPE_HEIGHT - 23);
        }
        poseStack.pushPose();
        //noinspection IntegerDivisionInFloatingPointContext
        poseStack.translate((RECIPE_WIDTH / 2) - 15, SPACE_TOP + 80, 0);
        JeaRender.normalize(poseStack);
        EntityTransformation transformation = JeaRender.entityRenderFront(false, 2.6f);
        if (this.renderBlockPattern(poseStack, buffer, mc, type, transformation)) {
            if (type == EntityType.ENDER_DRAGON) {
                poseStack.scale(1.8f, 1.8f, 1.8f);
            } else if (type == EntityType.WITHER) {
                poseStack.translate(0, 0.6, 0);
            }
            RenderEntityCache.renderEntity(mc, instance.entity, poseStack, buffer, transformation);
        }
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, SummonedEntityTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        @Nullable
        EntityType<?> type = this.getType(instance.entity);
        if (type == EntityType.ENDER_DRAGON) {
            //noinspection IntegerDivisionInFloatingPointContext
            RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.entity, (RECIPE_WIDTH / 2) - 15, SPACE_TOP + 88, JeaRender.normalScale(2.6f) * 2.8f, mouseX, mouseY);
        } else if (type == EntityType.WITHER) {
            //noinspection IntegerDivisionInFloatingPointContext
            RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.entity, (RECIPE_WIDTH / 2) - 15, SPACE_TOP + 95, JeaRender.normalScale(2.6f), mouseX, mouseY);
        } else {
            //noinspection IntegerDivisionInFloatingPointContext
            RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.entity, (RECIPE_WIDTH / 2) - 15, SPACE_TOP + 80, JeaRender.normalScale(2.6f), mouseX, mouseY);
        }
    }

    @Nullable
    private EntityType<?> getType(EntityPredicate.Composite predicate) {
        EntityPredicate entity = LootUtil.asEntity(predicate);
        if (entity.entityType instanceof EntityTypePredicate.TypePredicate typePredicate) {
            return typePredicate.type;
        } else {
            return null;
        }
    }

    private List<List<ItemStack>> getMaterials(@Nullable EntityType<?> type) {
        if (type == EntityType.ENDER_DRAGON) {
            return List.of(
                    List.of(new ItemStack(Items.END_CRYSTAL, 4))
            );
        } else if (type == EntityType.WITHER) {
            return List.of(
                    List.of(new ItemStack(Items.WITHER_SKELETON_SKULL, 3)),
                    List.of(new ItemStack(Items.SOUL_SAND, 4), new ItemStack(Items.SOUL_SOIL, 4))
            );
        } else if (type == EntityType.IRON_GOLEM) {
            return List.of(
                    List.of(new ItemStack(Items.CARVED_PUMPKIN)),
                    List.of(new ItemStack(Items.IRON_BLOCK, 4))
            );
        } else if (type == EntityType.SNOW_GOLEM) {
            return List.of(
                    List.of(new ItemStack(Items.CARVED_PUMPKIN)),
                    List.of(new ItemStack(Items.SNOW_BLOCK, 2))
            );
        } else {
            Item item = SpawnEggItem.byId(type);
            if (item == null) {
                return List.of();
            } else {
                return List.of(
                        List.of(new ItemStack(item))
                );
            }
        }
    }

    @SuppressWarnings("deprecation")
    private boolean renderBlockPattern(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, @Nullable EntityType<?> type, EntityTransformation transformation) {
        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 40;
        int light = LightTexture.pack(15, 15);
        if (animationTime < 20) {
            return true;
        } else if (type == EntityType.ENDER_DRAGON) {
            poseStack.pushPose();
            transformation.applyForEntity(poseStack);
            poseStack.scale(0.3f, 0.3f, 0.3f);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(15));
            poseStack.pushPose();
            poseStack.translate(-0.5, 0, -0.5);
            BlockState bedrock = Blocks.BEDROCK.defaultBlockState();

            poseStack.pushPose();
            poseStack.translate(-1, 0, -2);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);

            poseStack.translate(-3, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);

            poseStack.translate(-4, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);

            poseStack.translate(-4, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);

            poseStack.translate(-3, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(-1, 1, -3);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(-1, 1, 3);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(-3, 0, -5);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(-3, 1, -1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(0, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(0, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(3, 1, -1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(0, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(0, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(-1, 0, 1);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0, 1, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(0, 1, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(0, 1, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(0, 1, 0);
            mc.getBlockRenderer().renderSingleBlock(bedrock, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();

            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(-3, 2, 0);
            poseStack.scale(2, 2, 2);
            RenderEntityCache.renderPlainEntity(mc, EntityType.END_CRYSTAL, poseStack, buffer);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(3, 2, 0);
            poseStack.scale(2, 2, 2);
            RenderEntityCache.renderPlainEntity(mc, EntityType.END_CRYSTAL, poseStack, buffer);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0, 2, -3);
            poseStack.scale(2, 2, 2);
            RenderEntityCache.renderPlainEntity(mc, EntityType.END_CRYSTAL, poseStack, buffer);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0, 2, 3);
            poseStack.scale(2, 2, 2);
            RenderEntityCache.renderPlainEntity(mc, EntityType.END_CRYSTAL, poseStack, buffer);
            poseStack.popPose();

            poseStack.popPose();
        } else if (type == EntityType.WITHER) {
            poseStack.pushPose();
            transformation.applyForEntity(poseStack);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(-0.5, 0, -0.5);
            BlockState soul = (animationTime < 30 ? Blocks.SOUL_SOIL : Blocks.SOUL_SAND).defaultBlockState();
            BlockState skull = Blocks.WITHER_SKELETON_SKULL.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, 8);
            mc.getBlockRenderer().renderSingleBlock(soul, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(-1, 1, 0);
            mc.getBlockRenderer().renderSingleBlock(soul, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(soul, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(soul, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(-2, 1, 0);
            this.renderSkull(poseStack, buffer, mc, skull);
            poseStack.translate(1, 0, 0);
            this.renderSkull(poseStack, buffer, mc, skull);
            poseStack.translate(1, 0, 0);
            this.renderSkull(poseStack, buffer, mc, skull);
            poseStack.popPose();
        } else if (type == EntityType.IRON_GOLEM) {
            poseStack.pushPose();
            transformation.applyForEntity(poseStack);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(-0.5, 0, -0.5);
            BlockState iron = Blocks.IRON_BLOCK.defaultBlockState();
            BlockState pumpkin = Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
            mc.getBlockRenderer().renderSingleBlock(iron, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(-1, 1, 0);
            mc.getBlockRenderer().renderSingleBlock(iron, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(iron, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(1, 0, 0);
            mc.getBlockRenderer().renderSingleBlock(iron, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(-1, 1, 0);
            mc.getBlockRenderer().renderSingleBlock(pumpkin, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        } else if (type == EntityType.SNOW_GOLEM) {
            poseStack.pushPose();
            transformation.applyForEntity(poseStack);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(-0.5, 0, -0.5);
            BlockState snow = Blocks.SNOW_BLOCK.defaultBlockState();
            BlockState pumpkin = Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
            mc.getBlockRenderer().renderSingleBlock(snow, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(0, 1, 0);
            mc.getBlockRenderer().renderSingleBlock(snow, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.translate(0, 1, 0);
            mc.getBlockRenderer().renderSingleBlock(pumpkin, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        } else {
            return true;
        }
        return false;
    }

    private void renderSkull(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, BlockState state) {
        this.tile.setLevel(Objects.requireNonNull(mc.level));
        this.tile.blockState = state;
        if (this.tileRender == null) {
            this.tileRender = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            this.tileRender.render(this.tile, mc.getFrameTime(), poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        }
    }
}
