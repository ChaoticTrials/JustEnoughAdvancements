package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.LootUtil;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.EntityTransformation;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.advancements.criterion.SummonedEntityTrigger;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class SummonEntityInfo implements ICriterionInfo<SummonedEntityTrigger.Instance> {

    private final SkullTileEntity tile = new SkullTileEntity();
    private TileEntityRenderer<SkullTileEntity> tileRender = null;

    @Override
    public Class<SummonedEntityTrigger.Instance> criterionClass() {
        return SummonedEntityTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, SummonedEntityTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, this.getMaterials(this.getType(instance.entity)));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, SummonedEntityTrigger.Instance instance, IIngredients ii) {
        int size = ii.getInputs(VanillaTypes.ITEM).size();
        int x = RECIPE_WIDTH - 5 - (18 * size);
        for (int i = 0; i < size; i++) {
            layout.getItemStacks().init(i, true, x + (18 * i), SPACE_TOP + RECIPE_HEIGHT - 23);
        }
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, SummonedEntityTrigger.Instance instance, double mouseX, double mouseY) {
        @Nullable
        EntityType<?> type = this.getType(instance.entity);
        int size = this.getMaterials(type).size();
        int x = RECIPE_WIDTH - 5 - (18 * size);
        for (int i = 0; i < size; i++) {
            JeaRender.slotAt(matrixStack, x + (18 * i), SPACE_TOP + RECIPE_HEIGHT - 23);
        }
        matrixStack.push();
        //noinspection IntegerDivisionInFloatingPointContext
        matrixStack.translate((RECIPE_WIDTH / 2) - 15, SPACE_TOP + 80, 0);
        JeaRender.normalize(matrixStack);
        EntityTransformation transformation = JeaRender.entityRenderFront(false, 2.6f);
        if (this.renderBlockPattern(matrixStack, buffer, mc, type, transformation)) {
            if (type == EntityType.ENDER_DRAGON) {
                matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
                matrixStack.scale(4.5f, 4.5f, 4.5f);
            } else if (type == EntityType.WITHER) {
                matrixStack.translate(0, 0.6, 0);
            }
            RenderEntityCache.renderEntity(mc, instance.entity, matrixStack, buffer, transformation);
        }
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, SummonedEntityTrigger.Instance instance, double mouseX, double mouseY) {
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
    private EntityType<?> getType(EntityPredicate.AndPredicate predicate) {
        EntityPredicate entity = LootUtil.asEntity(predicate);
        if (entity.type instanceof EntityTypePredicate.TypePredicate) {
            return ((EntityTypePredicate.TypePredicate) entity.type).type;
        } else {
            return null;
        }
    }

    private List<List<ItemStack>> getMaterials(@Nullable EntityType<?> type) {
        if (type == EntityType.ENDER_DRAGON) {
            return ImmutableList.of(
                    ImmutableList.of(new ItemStack(Items.END_CRYSTAL, 4))
            );
        } else if (type == EntityType.WITHER) {
            return ImmutableList.of(
                    ImmutableList.of(new ItemStack(Items.WITHER_SKELETON_SKULL, 3)),
                    ImmutableList.of(new ItemStack(Items.SOUL_SAND, 4), new ItemStack(Items.SOUL_SOIL, 4))
            );
        } else if (type == EntityType.IRON_GOLEM) {
            return ImmutableList.of(
                    ImmutableList.of(new ItemStack(Items.CARVED_PUMPKIN)),
                    ImmutableList.of(new ItemStack(Items.IRON_BLOCK, 4))
            );
        } else if (type == EntityType.SNOW_GOLEM) {
            return ImmutableList.of(
                    ImmutableList.of(new ItemStack(Items.CARVED_PUMPKIN)),
                    ImmutableList.of(new ItemStack(Items.SNOW_BLOCK, 2))
            );
        } else {
            Item item = SpawnEggItem.getEgg(type);
            if (item == null) {
                return ImmutableList.of();
            } else {
                return ImmutableList.of(
                        ImmutableList.of(new ItemStack(item))
                );
            }
        }
    }

    @SuppressWarnings("deprecation")
    private boolean renderBlockPattern(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, @Nullable EntityType<?> type, EntityTransformation transformation) {
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 40;
        int light = LightTexture.packLight(15, 15);
        if (animationTime < 20) {
            return true;
        } else if (type == EntityType.ENDER_DRAGON) {
            matrixStack.push();
            transformation.applyForEntity(matrixStack);
            matrixStack.scale(0.3f, 0.3f, 0.3f);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(15));
            matrixStack.push();
            matrixStack.translate(-0.5, 0, -0.5);
            BlockState bedrock = Blocks.BEDROCK.getDefaultState();

            matrixStack.push();
            matrixStack.translate(-1, 0, -2);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

            matrixStack.translate(-3, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

            matrixStack.translate(-4, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

            matrixStack.translate(-4, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

            matrixStack.translate(-3, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(-1, 1, -3);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(-1, 1, 3);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(-3, 0, -5);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(-3, 1, -1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(3, 1, -1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(-1, 0, 1);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(0, 1, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 1, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 1, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 1, 0);
            mc.getBlockRendererDispatcher().renderBlock(bedrock, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();

            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(-3, 2, 0);
            matrixStack.scale(2, 2, 2);
            RenderEntityCache.renderPlainEntity(mc, EntityType.END_CRYSTAL, matrixStack, buffer);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(3, 2, 0);
            matrixStack.scale(2, 2, 2);
            RenderEntityCache.renderPlainEntity(mc, EntityType.END_CRYSTAL, matrixStack, buffer);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(0, 2, -3);
            matrixStack.scale(2, 2, 2);
            RenderEntityCache.renderPlainEntity(mc, EntityType.END_CRYSTAL, matrixStack, buffer);
            matrixStack.pop();

            matrixStack.push();
            matrixStack.translate(0, 2, 3);
            matrixStack.scale(2, 2, 2);
            RenderEntityCache.renderPlainEntity(mc, EntityType.END_CRYSTAL, matrixStack, buffer);
            matrixStack.pop();

            matrixStack.pop();
        } else if (type == EntityType.WITHER) {
            matrixStack.push();
            transformation.applyForEntity(matrixStack);
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            matrixStack.translate(-0.5, 0, -0.5);
            BlockState soul = (animationTime < 30 ? Blocks.SOUL_SOIL : Blocks.SOUL_SAND).getDefaultState();
            BlockState skull = Blocks.WITHER_SKELETON_SKULL.getDefaultState().with(BlockStateProperties.ROTATION_0_15, 8);
            mc.getBlockRendererDispatcher().renderBlock(soul, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(-1, 1, 0);
            mc.getBlockRendererDispatcher().renderBlock(soul, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(soul, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(soul, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(-2, 1, 0);
            this.renderSkull(matrixStack, buffer, mc, skull);
            matrixStack.translate(1, 0, 0);
            this.renderSkull(matrixStack, buffer, mc, skull);
            matrixStack.translate(1, 0, 0);
            this.renderSkull(matrixStack, buffer, mc, skull);
            matrixStack.pop();
        } else if (type == EntityType.IRON_GOLEM) {
            matrixStack.push();
            transformation.applyForEntity(matrixStack);
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            matrixStack.translate(-0.5, 0, -0.5);
            BlockState iron = Blocks.IRON_BLOCK.getDefaultState();
            BlockState pumpkin = Blocks.CARVED_PUMPKIN.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
            mc.getBlockRendererDispatcher().renderBlock(iron, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(-1, 1, 0);
            mc.getBlockRendererDispatcher().renderBlock(iron, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(iron, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(1, 0, 0);
            mc.getBlockRendererDispatcher().renderBlock(iron, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(-1, 1, 0);
            mc.getBlockRendererDispatcher().renderBlock(pumpkin, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();
        } else if (type == EntityType.SNOW_GOLEM) {
            matrixStack.push();
            transformation.applyForEntity(matrixStack);
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            matrixStack.translate(-0.5, 0, -0.5);
            BlockState snow = Blocks.SNOW_BLOCK.getDefaultState();
            BlockState pumpkin = Blocks.CARVED_PUMPKIN.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
            mc.getBlockRendererDispatcher().renderBlock(snow, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 1, 0);
            mc.getBlockRendererDispatcher().renderBlock(snow, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.translate(0, 1, 0);
            mc.getBlockRendererDispatcher().renderBlock(pumpkin, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();
        } else {
            return true;
        }
        return false;
    }

    private void renderSkull(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, BlockState state) {
        this.tile.setWorldAndPos(Objects.requireNonNull(mc.world), JeaRender.BELOW_WORLD);
        this.tile.cachedBlockState = state;
        if (this.tileRender == null) {
            this.tileRender = TileEntityRendererDispatcher.instance.getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            this.tileRender.render(this.tile, mc.getRenderPartialTicks(), matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        }
    }
}
