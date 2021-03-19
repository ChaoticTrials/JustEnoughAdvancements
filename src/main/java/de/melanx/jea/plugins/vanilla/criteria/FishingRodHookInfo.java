package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.melanx.jea.util.LootUtil;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SpecialModels;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import io.github.noeppi_noeppi.libx.render.RenderHelperItem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.FishingRodHookedTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FishingRodHookInfo implements ICriterionInfo<FishingRodHookedTrigger.Instance> {

    public static final ResourceLocation FISHING_BOBBER_TEXTURE = new ResourceLocation("minecraft", "textures/entity/fishing_hook.png");
    
    @Override
    public Class<FishingRodHookedTrigger.Instance> criterionClass() {
        return FishingRodHookedTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, FishingRodHookedTrigger.Instance instance, IIngredients ii) {
        if (instance.item != ItemPredicate.ANY) {
            ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                    IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)
            ));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, FishingRodHookedTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, RECIPE_WIDTH - 25, SPACE_TOP + 7);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, FishingRodHookedTrigger.Instance instance, double mouseX, double mouseY) {
        if (instance.item != ItemPredicate.ANY) {
            JeaRender.slotAt(matrixStack, RECIPE_WIDTH - 25, SPACE_TOP + 7);
        } else {
            matrixStack.push();
            EntityPredicate.AndPredicate entity = LootUtil.asLootPredicate(EntityPredicate.Builder.create().type(EntityType.CHICKEN).build());
            matrixStack.translate(RECIPE_WIDTH - 22, SPACE_TOP + 36, 0);
            JeaRender.normalize(matrixStack);
            RenderEntityCache.renderEntity(mc, instance.entity, matrixStack, buffer, JeaRender.entityRenderFront(true, 1));
            matrixStack.pop();
        }
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 75;
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
        
        matrixStack.push();
        matrixStack.translate(20, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.fishingBobber(bobber > 0);
        SteveRender.setEquipmentHand(mc, getStack(IngredientUtil.fromItemPredicate(instance.rod, Items.FISHING_ROD)));
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH- 40, SPACE_TOP + 80, 0);
        JeaRender.normalize(matrixStack);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-18));
        JeaRender.transformForEntityRenderFront(matrixStack, true, 5);
        SpecialModels.renderFluidPool(matrixStack, buffer, mc, new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), null);
        matrixStack.pop();

        if (bobber > 0) {
            matrixStack.push();
            mc.getTextureManager().bindTexture(FISHING_BOBBER_TEXTURE);
            float bobbingOffset = 8 * (float) ((0.5 * Math.sin((bobbing * 8 * Math.PI) - (0.5 * Math.PI))) + 0.5);
            float bobberOffset = -35 * (float) (Math.sin((0.75 * Math.PI * bobber) + (Math.PI / 4)));
            matrixStack.translate(45 + (60 * bobber), 50 + bobberOffset + bobbingOffset, 300);
            AbstractGui.blit(matrixStack, 0, SPACE_TOP, 0, 0, 0, 8, 8, 8, 8);
            if (contentScale > 0) {
                if (instance.item != ItemPredicate.ANY) {
                    matrixStack.translate(4, SPACE_TOP + 6, 100);
                    matrixStack.scale(0.75f * contentScale, 0.75f * contentScale, 1);
                    matrixStack.translate(0, 8, 0);
                    matrixStack.rotate(Vector3f.ZP.rotationDegrees(125));
                    matrixStack.translate(-8, -8, 0);
                    RenderHelperItem.renderItemGui(matrixStack, buffer, JeaRender.cycle(IngredientUtil.fromItemPredicate(instance.item, Items.STRUCTURE_VOID)), 0, 0, 16, false);
                } else {
                    matrixStack.translate(4, SPACE_TOP + 8, 0);
                    JeaRender.normalize(matrixStack);
                    RenderEntityCache.renderEntity(mc, instance.entity, matrixStack, buffer, JeaRender.entityRenderFront(true, 0.6f * contentScale));
                }
            }
            matrixStack.pop();
            Matrix4f model = matrixStack.getLast().getMatrix();
            Matrix3f normal = matrixStack.getLast().getNormal();
            
            int light = LightTexture.packLight(15, 15);
            float hdiff = (float) (-35 * Math.sin(Math.PI / 4)) - (bobberOffset + bobbingOffset - 8);
            float lastDown = 0;
            for (int i = 1; i <= 10; i++) {
                float down = (float) -Math.log(1 + ((i / 12f) * (Math.E - 1)));
                IVertexBuilder vertex = buffer.getBuffer(RenderType.getEntitySolid(RenderHelper.TEXTURE_WHITE));
                float x1 = 48 + (60 * ((i - 1) / 10f) * bobber);
                float x2 = 48 + (60 * (i / 10f) * bobber);
                float y1 = 73 + (hdiff * lastDown);
                float y2 = 73 + (hdiff * down);
                vertex.pos(model, x1, y1 + 1, 280).color(0, 0, 0, 1).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
                vertex.pos(model, x2, y2 + 1, 280).color(0, 0, 0, 1).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
                vertex.pos(model, x2, y2, 280).color(0, 0, 0, 1).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
                vertex.pos(model, x1, y1, 280).color(0, 0, 0, 1).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
                lastDown = down;
            }
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, FishingRodHookedTrigger.Instance instance, double mouseX, double mouseY) {
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
