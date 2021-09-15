package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.LootTableTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.List;
import java.util.Objects;

public class GenerateContainerLootInfo implements ICriterionInfo<LootTableTrigger.TriggerInstance> {

    private final ChestBlockEntity tile = new ChestBlockEntity(JeaRender.BELOW_WORLD, Blocks.CHEST.defaultBlockState());
    private BlockEntityRenderer<ChestBlockEntity> tileRender = null;
    
    @Override
    public Class<LootTableTrigger.TriggerInstance> criterionClass() {
        return LootTableTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, LootTableTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, LootTableTrigger.TriggerInstance instance, IIngredients ii) {
        //
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, LootTableTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 45;
        float chestOpen;
        if (animationTime < 10) {
            chestOpen = animationTime / 10f;
        } else if (animationTime < 15) {
            chestOpen = 1;
        } else if (animationTime < 25) {
            chestOpen = 1 - ((animationTime - 15) / 10f);
        } else {
            chestOpen = 0;
        }
        
        poseStack.pushPose();
        //noinspection IntegerDivisionInFloatingPointContext
        poseStack.translate(RECIPE_WIDTH / 2, RECIPE_HEIGHT + 8, 0);
        JeaRender.normalize(poseStack);
        poseStack.scale(2.4f, -2.4f, 2.4f);
        double translate = Math.sqrt(2) / 2;
        poseStack.translate(translate, 0, translate);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(22.5f));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180 + 45));
        this.tile.setLevel(Objects.requireNonNull(mc.level));
        this.tile.blockState = Blocks.CHEST.defaultBlockState();
        this.tile.chestLidController.openness = 0.5f * chestOpen;
        this.tile.chestLidController.oOpenness = this.tile.chestLidController.openness;
        this.tile.chestLidController.shouldBeOpen = this.tile.chestLidController.openness > 0;
        if (this.tileRender == null) {
            this.tileRender = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            this.tileRender.render(this.tile, mc.getFrameTime(), poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();

        MutableComponent text1 = new TranslatableComponent("jea.item.tooltip.generate_loot");
        MutableComponent text2 = new TextComponent(IngredientUtil.rl(instance.lootTable));
        int width1 = mc.font.width(text1);
        int width2 = mc.font.width(text2);
        //noinspection IntegerDivisionInFloatingPointContext
        mc.font.draw(poseStack, text1, (RECIPE_WIDTH / 2) - (width1 / 2), SPACE_TOP + 71, 0x000000);
        //noinspection IntegerDivisionInFloatingPointContext
        mc.font.draw(poseStack, text2, (RECIPE_WIDTH / 2) - (width2 / 2), SPACE_TOP + 73 + mc.font.lineHeight, 0x000000);
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, LootTableTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
