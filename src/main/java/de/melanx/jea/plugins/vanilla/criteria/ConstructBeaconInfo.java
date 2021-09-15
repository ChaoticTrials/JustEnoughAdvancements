package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.ConstructBeaconTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.stream.Collectors;

public class ConstructBeaconInfo implements ICriterionInfo<ConstructBeaconTrigger.TriggerInstance> {

    @Override
    public Class<ConstructBeaconTrigger.TriggerInstance> criterionClass() {
        return ConstructBeaconTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ConstructBeaconTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(new ItemStack(Items.BEACON)),
                BlockTags.BEACON_BASE_BLOCKS.getValues().stream().map(ItemStack::new).collect(Collectors.toList())
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ConstructBeaconTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 3, SPACE_TOP + 3);
        layout.getItemStacks().init(1, true, 3, SPACE_TOP + 24);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ConstructBeaconTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, 3, SPACE_TOP + 3);
        JeaRender.slotAt(poseStack, 3, SPACE_TOP + 24);
        MinMaxBounds.Ints level = MinMaxBounds.Ints.atLeast(1);
        if (!instance.level.isAny()) {
            level = instance.level;
        }
        Component text = new TranslatableComponent("jea.item.tooltip.beacon.level", IngredientUtil.text(level));
        mc.font.draw(poseStack, text, 2, SPACE_TOP + RECIPE_HEIGHT - 2 - mc.font.lineHeight, 0x000000);
        int renderLevel = IngredientUtil.getExampleValue(level).orElse(1);
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 60, SPACE_TOP + RECIPE_HEIGHT - 30, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderFront(poseStack, true, 4f / (1 + (2 * renderLevel)));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(20));
        this.renderBeacon(poseStack, buffer, mc, renderLevel);
        poseStack.popPose();
    }
    
    private void renderBeacon(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, int level) {
        if (!BlockTags.BEACON_BASE_BLOCKS.getValues().isEmpty()) {
            BlockRenderDispatcher brd = mc.getBlockRenderer();
            BlockState beacon = Blocks.BEACON.defaultBlockState();
            BlockState base = JeaRender.cycle(BlockTags.BEACON_BASE_BLOCKS.getValues()).defaultBlockState();
            int light = LightTexture.pack(15, 15);
            int overlay = OverlayTexture.NO_OVERLAY;
            poseStack.pushPose();
            for (int i = level; i > 0; i--) {
                renderBeaconLayer(poseStack, buffer, mc, brd, base, light, overlay, i);
                poseStack.translate(0, 1, 0);
            }
            poseStack.translate(-0.5, 0, -0.5);
            //noinspection deprecation
            brd.renderSingleBlock(beacon, poseStack, buffer, light, overlay);
            poseStack.popPose();
        }
    }
    
    private static void renderBeaconLayer(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, BlockRenderDispatcher brd, BlockState state, int light, int overlay, int layer) {
        poseStack.pushPose();
        poseStack.translate(-0.5 - layer, 0, -0.5 - layer);
        for (int x = -layer; x <= layer; x++) {
            for (int z = -layer; z <= layer; z++) {
                //noinspection deprecation
                brd.renderSingleBlock(state, poseStack, buffer, light, overlay);
                poseStack.translate(0, 0, 1);
            }
            poseStack.translate(1, 0, -(2 * layer) - 1);
        }
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, ConstructBeaconTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
