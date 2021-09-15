package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SpecialModels;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import de.melanx.jea.util.LootUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import io.github.noeppi_noeppi.libx.render.RenderHelperItem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FishingRodHookInfo implements ICriterionInfo<FishingRodHookedTrigger.TriggerInstance> {

    public static final ResourceLocation FISHING_BOBBER_TEXTURE = new ResourceLocation("minecraft", "textures/entity/fishing_hook.png");
    
    @Override
    public Class<FishingRodHookedTrigger.TriggerInstance> criterionClass() {
        return FishingRodHookedTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, FishingRodHookedTrigger.TriggerInstance instance, IIngredients ii) {
        if (instance.item != ItemPredicate.ANY) {
            ii.setInputLists(VanillaTypes.ITEM, List.of(
                    IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)
            ));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, FishingRodHookedTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, RECIPE_WIDTH - 25, SPACE_TOP + 7);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, FishingRodHookedTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (instance.item != ItemPredicate.ANY) {
            JeaRender.slotAt(poseStack, RECIPE_WIDTH - 25, SPACE_TOP + 7);
        } else {
            poseStack.pushPose();
            EntityPredicate.Composite entity = LootUtil.asLootPredicate(EntityPredicate.Builder.entity().of(EntityType.CHICKEN).build());
            poseStack.translate(RECIPE_WIDTH - 22, SPACE_TOP + 36, 0);
            JeaRender.normalize(poseStack);
            RenderEntityCache.renderEntity(mc, instance.entity, poseStack, buffer, JeaRender.entityRenderFront(true, 1));
            poseStack.popPose();
        }
        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 75;
        float swing;
        float bobber;
        float bobbing;
        float contentScale;
        if (animationTime < 20) {
            swing = 0;
            bobber = 0;
            bobbing = 0;
            contentScale = 0;
        } else if (animationTime < 29) {
            if (animationTime < 26) {
                swing = (animationTime - 20) / 6f;
            } else {
                swing = 0;
            }
            if (animationTime > 23) {
                bobber = (animationTime - 23) / 6f;
            } else {
                bobber = 0;
            }
            bobbing = 0;
            contentScale = 0;
        } else if (animationTime < 69) {
            swing = 0;
            bobber = 1;
            bobbing = (animationTime - 29) / 40f;
            contentScale = (animationTime - 29) / 40f;
        } else {
            swing = 0;
            bobber = 1 - ((animationTime - 69) / 6f);
            bobbing = 0;
            contentScale = 1;
        }
        
        poseStack.pushPose();
        poseStack.translate(20, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
        SteveRender.fishingBobber(bobber > 0);
        SteveRender.setEquipmentHand(mc, getStack(IngredientUtil.fromItemPredicate(instance.rod, Items.FISHING_ROD)));
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH- 40, SPACE_TOP + 80, 0);
        JeaRender.normalize(poseStack);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-18));
        JeaRender.transformForEntityRenderFront(poseStack, true, 5);
        SpecialModels.renderFluidPool(poseStack, buffer, mc, new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), null);
        poseStack.popPose();

        if (bobber > 0) {
            poseStack.pushPose();
            RenderSystem.setShaderTexture(0, FISHING_BOBBER_TEXTURE);
            float bobbingOffset = 8 * (float) ((0.5 * Math.sin((bobbing * 8 * Math.PI) - (0.5 * Math.PI))) + 0.5);
            float bobberOffset = -35 * (float) (Math.sin((0.75 * Math.PI * bobber) + (Math.PI / 4)));
            poseStack.translate(45 + (60 * bobber), 50 + bobberOffset + bobbingOffset, 300);
            GuiComponent.blit(poseStack, 0, SPACE_TOP, 0, 0, 0, 8, 8, 8, 8);
            if (contentScale > 0) {
                if (instance.item != ItemPredicate.ANY) {
                    poseStack.translate(4, SPACE_TOP + 6, 100);
                    poseStack.scale(0.75f * contentScale, 0.75f * contentScale, 1);
                    poseStack.translate(0, 8, 0);
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees(125));
                    poseStack.translate(-8, -8, 0);
                    RenderHelperItem.renderItemGui(poseStack, buffer, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)), 0, 0, 16, false);
                } else {
                    poseStack.translate(4, SPACE_TOP + 8, 0);
                    JeaRender.normalize(poseStack);
                    RenderEntityCache.renderEntity(mc, instance.entity, poseStack, buffer, JeaRender.entityRenderFront(true, 0.6f * contentScale));
                }
            }
            poseStack.popPose();
            Matrix4f model = poseStack.last().pose();
            Matrix3f normal = poseStack.last().normal();
            
            int light = LightTexture.pack(15, 15);
            float hdiff = (float) (-35 * Math.sin(Math.PI / 4)) - (bobberOffset + bobbingOffset - 8);
            float lastDown = 0;
            for (int i = 1; i <= 10; i++) {
                float down = (float) -Math.log(1 + ((i / 12f) * (Math.E - 1)));
                VertexConsumer vertex = buffer.getBuffer(RenderType.entitySolid(RenderHelper.TEXTURE_WHITE));
                float x1 = 48 + (60 * ((i - 1) / 10f) * bobber);
                float x2 = 48 + (60 * (i / 10f) * bobber);
                float y1 = 73 + (hdiff * lastDown);
                float y2 = 73 + (hdiff * down);
                vertex.vertex(model, x1, y1 + 1, 280).color(0, 0, 0, 1).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1, 0, 0).endVertex();
                vertex.vertex(model, x2, y2 + 1, 280).color(0, 0, 0, 1).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1, 0, 0).endVertex();
                vertex.vertex(model, x2, y2, 280).color(0, 0, 0, 1).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1, 0, 0).endVertex();
                vertex.vertex(model, x1, y1, 280).color(0, 0, 0, 1).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1, 0, 0).endVertex();
                lastDown = down;
            }
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, FishingRodHookedTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (instance.item == ItemPredicate.ANY) {
            RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.entity, RECIPE_WIDTH - 22, SPACE_TOP + 36, JeaRender.normalScale(1), mouseX, mouseY);
        }
    }
    
    private static ItemStack getStack(List<ItemStack> stacks) {
        // item stack should switch after a whole animation and not while its animated.
        int idx = ((ClientTickHandler.ticksInGame - 2) / 75) % stacks.size();
        return stacks.get(idx);
    }
}
