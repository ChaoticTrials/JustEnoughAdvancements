//package de.melanx.jea.plugins.botania.criteria;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.jea.api.client.IAdvancementInfo;
//import de.melanx.jea.api.client.criterion.ICriterionInfo;
//import de.melanx.jea.render.JeaRender;
//import de.melanx.jea.render.SteveRender;
//import de.melanx.jea.util.IngredientUtil;
//import de.melanx.jea.util.TooltipUtil;
//import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.ingredients.IIngredients;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.LightTexture;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
//import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
//import net.minecraft.core.BlockPos;
//import com.mojang.math.Vector3f;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.network.chat.Component;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.TranslatableComponent;
//import vazkii.botania.common.advancements.CorporeaRequestTrigger;
//import vazkii.botania.common.block.ModBlocks;
//import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class CorporeaRequestInfo implements ICriterionInfo<CorporeaRequestTrigger.Instance> {
//
//    private final TileCorporeaIndex tile = new TileCorporeaIndex();
//    private BlockEntityRenderer<TileCorporeaIndex> tileRender = null;
//
//    @Override
//    public Class<CorporeaRequestTrigger.Instance> criterionClass() {
//        return CorporeaRequestTrigger.Instance.class;
//    }
//
//    @Override
//    public void setIngredients(IAdvancementInfo advancement, String criterionKey, CorporeaRequestTrigger.Instance instance, IIngredients ii) {
//        
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, CorporeaRequestTrigger.Instance instance, IIngredients ii) {
//
//    }
//
//    @Override
//    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, CorporeaRequestTrigger.Instance instance, double mouseX, double mouseY) {
//        poseStack.pushPose();
//        poseStack.translate(30, SPACE_TOP + 78, 0);
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderFront(poseStack, false, 2.4f);
//        SteveRender.defaultPose(mc);
//        SteveRender.clearEquipment(mc);
//        SteveRender.renderSteve(mc, poseStack, buffer);
//        poseStack.popPose();
//
//        poseStack.pushPose();
//        poseStack.translate(RECIPE_WIDTH - 65, SPACE_TOP + 65, 0);
//        poseStack.mulPose(Vector3f.XP.rotationDegrees(-13));
//        JeaRender.normalize(poseStack);
//        JeaRender.transformForEntityRenderSide(poseStack, false, 3.2f);
//        this.tile.setLevelAndPosition(Objects.requireNonNull(mc.level), BlockPos.ZERO);
//        this.tile.blockState = ModBlocks.corporeaIndex.defaultBlockState();
//        this.tile.hasCloseby = true;
//        this.tile.ticksWithCloseby = ClientTickHandler.ticksInGame;
//        this.tile.ticks = ClientTickHandler.ticksInGame;
//        if (this.tileRender == null) {
//            this.tileRender = BlockEntityRenderDispatcher.instance.getRenderer(this.tile);
//        }
//        if (this.tileRender != null) {
//            this.tileRender.render(this.tile, mc.getFrameTime(), poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
//        }
//        poseStack.popPose();
//
//        if (!instance.getCount().isAny()) {
//            Component text = new TranslatableComponent("jea.item.tooltip.botania.corporea_request_amount", IngredientUtil.text(instance.getCount()));
//            mc.font.draw(poseStack, text, 10, SPACE_TOP + 80, 0x000000);
//        }
//    }
//
//    @Override
//    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, CorporeaRequestTrigger.Instance instance, double mouseX, double mouseY) {
//        if (mouseX < RECIPE_WIDTH - 8 && mouseX > RECIPE_WIDTH - 55 && mouseY > SPACE_TOP + 28 && mouseY < SPACE_TOP + 67) {
//            List<MutableComponent> list = new ArrayList<>();
//            TooltipUtil.addLocationValues(list, instance.getIndexPos());
//            if (!list.isEmpty()) {
//                tooltip.add(new TranslatableComponent("jea.item.tooltip.botania.corporea_location").withStyle(ChatFormatting.GOLD));
//                tooltip.addAll(list);
//            }
//        }
//    }
//}
