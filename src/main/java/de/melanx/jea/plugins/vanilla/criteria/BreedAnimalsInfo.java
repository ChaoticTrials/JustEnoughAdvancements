package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.util.LootUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class BreedAnimalsInfo implements ICriterionInfo<BredAnimalsTrigger.TriggerInstance> {
    
    @SuppressWarnings("UnstableApiUsage")
    private final List<ItemStack> DEFAULT_BREEDING_ITEMS = Stream.of(
            Items.WHEAT,
            Items.WHEAT_SEEDS,
            Items.CARROT
    ).map(ItemStack::new).collect(ImmutableList.toImmutableList());
    private final Map<EntityType<?>, List<ItemStack>> BREEDING_ITEMS = new HashMap<>();
    
    @Override
    public Class<BredAnimalsTrigger.TriggerInstance> criterionClass() {
        return BredAnimalsTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, BredAnimalsTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                this.getBreedingItems(Minecraft.getInstance(), instance.parent, instance.partner, instance.child)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, BredAnimalsTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 28);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, BredAnimalsTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 28);
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 50, 0);
        JeaRender.normalize(poseStack);
        RenderEntityCache.renderEntity(mc, instance.parent, poseStack, buffer, JeaRender.entityRenderFront(false, 1.8f));
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 50, 0);
        JeaRender.normalize(poseStack);
        RenderEntityCache.renderEntity(mc, instance.parent, poseStack, buffer, JeaRender.entityRenderFront(true, 1.8f));
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH / 2d, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        RenderEntityCache.renderEntity(mc, instance.child, poseStack, buffer, JeaRender.entityRenderFront(false, 1.8f), DefaultEntityProperties.BABY);
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, BredAnimalsTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.parent, 30, SPACE_TOP + 50, JeaRender.normalScale(1.8f), mouseX, mouseY);
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.partner, RECIPE_WIDTH - 30, SPACE_TOP + 50, JeaRender.normalScale(1.8f), mouseX, mouseY);
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.child, RECIPE_WIDTH / 2d, SPACE_TOP + 90, JeaRender.normalScale(1.8f), DefaultEntityProperties.BABY, mouseX, mouseY);
    }
    
    private List<ItemStack> getBreedingItems(Minecraft mc, EntityPredicate.Composite parent, EntityPredicate.Composite partner, EntityPredicate.Composite child) {
        Optional<List<ItemStack>> option = this.getBreedingItemsOption(RenderEntityCache.getRenderEntity(mc, LootUtil.asEntity(parent), DefaultEntityProperties.DEFAULT));
        if (option.isEmpty()) {
            option = this.getBreedingItemsOption(RenderEntityCache.getRenderEntity(mc, LootUtil.asEntity(partner), DefaultEntityProperties.DEFAULT));
            if (option.isEmpty()) {
                option = this.getBreedingItemsOption(RenderEntityCache.getRenderEntity(mc, LootUtil.asEntity(child), DefaultEntityProperties.DEFAULT));
            }
        }
        return option.orElse(this.DEFAULT_BREEDING_ITEMS);
    }
    
    private Optional<List<ItemStack>> getBreedingItemsOption(Entity entity) {
        if (entity == null) {
            return Optional.empty();
        } else if (this.BREEDING_ITEMS.containsKey(entity.getType())) {
            return Optional.of(this.BREEDING_ITEMS.get(entity.getType()));
        } else if (entity instanceof Animal animal) {
            //noinspection UnstableApiUsage
            List<ItemStack> breedingItems = ForgeRegistries.ITEMS.getValues().stream()
                    .map(ItemStack::new)
                    .filter(animal::isFood)
                    .collect(ImmutableList.toImmutableList());
            if (breedingItems.isEmpty()) {
                breedingItems = this.DEFAULT_BREEDING_ITEMS;
            }
            this.BREEDING_ITEMS.put(entity.getType(), breedingItems);
            return Optional.of(breedingItems);
        } else {
            return Optional.empty();
        }
    }
}
