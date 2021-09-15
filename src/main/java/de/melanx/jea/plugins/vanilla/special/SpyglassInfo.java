package de.melanx.jea.plugins.vanilla.special;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.UsingItemTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class SpyglassInfo implements ICriterionInfo<UsingItemTrigger.TriggerInstance> {
    
    public static final ResourceLocation PARROT_ID = new ResourceLocation("minecraft", "adventure/spyglass_at_parrot");
    public static final String PARROT_CRITERION = "spyglass_at_parrot";

    public static final ResourceLocation GHASTT_ID = new ResourceLocation("minecraft", "adventure/spyglass_at_ghast");
    public static final String GHAST_CRITERION = "spyglass_at_ghast";

    public static final ResourceLocation DRAGON_ID = new ResourceLocation("minecraft", "adventure/spyglass_at_dragon");
    public static final String DRAGON_CRITERION = "spyglass_at_dragon";
    
    private final EntityType<?> entity;

    public SpyglassInfo(EntityType<?> entity) {
        this.entity = entity;
    }

    @Override
    public Class<UsingItemTrigger.TriggerInstance> criterionClass() {
        return UsingItemTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, UsingItemTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(new ItemStack(Items.SPYGLASS))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, UsingItemTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 55, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, UsingItemTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, 55, SPACE_TOP + 72);
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.8f);
        SteveRender.defaultPose(mc);
        SteveRender.use(1, InteractionHand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, new ItemStack(Items.SPYGLASS));
        SteveRender.renderSteveStatic(mc, poseStack, buffer);
        poseStack.popPose();
        
        boolean dragon = this.entity == EntityType.ENDER_DRAGON;
        poseStack.pushPose();
        poseStack.translate(105, SPACE_TOP + 50, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, !dragon, dragon ? 6 : 2.1f);
        RenderEntityCache.renderPlainEntity(mc, this.entity, poseStack, buffer);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, UsingItemTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
