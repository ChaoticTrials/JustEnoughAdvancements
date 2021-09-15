package de.melanx.jea.plugins.vanilla.special;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.LocationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class WalkOnPowderSnowInfo implements ICriterionInfo<LocationTrigger.TriggerInstance> {

    public static final ResourceLocation ADVANCEMENT = new ResourceLocation("minecraft", "adventure/walk_on_powder_snow_with_leather_boots");
    public static final String CRITERION = "walk_on_powder_snow_with_leather_boots";
    
    @Override
    public Class<LocationTrigger.TriggerInstance> criterionClass() {
        return LocationTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(new ItemStack(Blocks.POWDER_SNOW)),
                List.of(new ItemStack(Items.LEATHER_BOOTS))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, Jea.LARGE_BLOCK, 36, SPACE_TOP + 42, 48, 48, 0, 0);
        layout.getItemStacks().init(1, true, 96, SPACE_TOP + 72);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, 96, SPACE_TOP + 72);
        poseStack.pushPose();
        poseStack.translate(60, SPACE_TOP + 50, 200);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 1.6f);
        SteveRender.defaultPose(mc);
        SteveRender.setEquipment(
                mc, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                ItemStack.EMPTY, new ItemStack(Items.LEATHER_BOOTS)
        );
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, LocationTrigger.TriggerInstance instance, double mouseX, double mouseY) {

    }
}
