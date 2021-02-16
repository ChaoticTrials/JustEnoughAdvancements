package de.melanx.jea.plugins.vanilla.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.LootUtil;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.BredAnimalsTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BreedAnimalsInfo implements ICriterionInfo<BredAnimalsTrigger.Instance> {
    
    @SuppressWarnings("UnstableApiUsage")
    private final List<ItemStack> DEFAULT_BREEDING_ITEMS = ImmutableList.of(
            Items.WHEAT,
            Items.WHEAT_SEEDS,
            Items.CARROT
    ).stream().map(ItemStack::new).collect(ImmutableList.toImmutableList());
    private final Map<EntityType<?>, List<ItemStack>> BREEDING_ITEMS = new HashMap<>();
    
    @Override
    public Class<BredAnimalsTrigger.Instance> criterionClass() {
        return BredAnimalsTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, BredAnimalsTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                this.getBreedingItems(Minecraft.getInstance(), instance.parent, instance.partner, instance.child)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, BredAnimalsTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 28);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, BredAnimalsTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 28);
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 50, 0);
        JeaRender.normalize(matrixStack);
        RenderEntityCache.renderEntity(mc, instance.parent, matrixStack, buffer, JeaRender.entityRenderFront(false, 1.8f));
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 50, 0);
        JeaRender.normalize(matrixStack);
        RenderEntityCache.renderEntity(mc, instance.parent, matrixStack, buffer, JeaRender.entityRenderFront(true, 1.8f));
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH / 2d, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        RenderEntityCache.renderEntity(mc, instance.child, matrixStack, buffer, JeaRender.entityRenderFront(false, 1.8f), DefaultEntityProperties.BABY);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, BredAnimalsTrigger.Instance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.parent, 30, SPACE_TOP + 50, JeaRender.normalScale(1.8f), mouseX, mouseY);
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.partner, RECIPE_WIDTH - 30, SPACE_TOP + 50, JeaRender.normalScale(1.8f), mouseX, mouseY);
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.child, RECIPE_WIDTH / 2d, SPACE_TOP + 90, JeaRender.normalScale(1.8f), DefaultEntityProperties.BABY, mouseX, mouseY);
    }
    
    private List<ItemStack> getBreedingItems(Minecraft mc, EntityPredicate.AndPredicate parent, EntityPredicate.AndPredicate partner, EntityPredicate.AndPredicate child) {
        Optional<List<ItemStack>> option = this.getBreedingItemsOption(RenderEntityCache.getRenderEntity(mc, LootUtil.asEntity(parent)));
        if (!option.isPresent()) {
            option = this.getBreedingItemsOption(RenderEntityCache.getRenderEntity(mc, LootUtil.asEntity(partner)));
            if (!option.isPresent()) {
                option = this.getBreedingItemsOption(RenderEntityCache.getRenderEntity(mc, LootUtil.asEntity(child)));
            }
        }
        return option.orElse(this.DEFAULT_BREEDING_ITEMS);
    }
    
    private Optional<List<ItemStack>> getBreedingItemsOption(Entity entity) {
        if (entity == null) {
            return Optional.empty();
        } else if (this.BREEDING_ITEMS.containsKey(entity.getType())) {
            return Optional.of(this.BREEDING_ITEMS.get(entity.getType()));
        } else if (entity instanceof AnimalEntity){
            //noinspection UnstableApiUsage
            List<ItemStack> breedingItems = ForgeRegistries.ITEMS.getValues().stream()
                    .map(ItemStack::new)
                    .filter(((AnimalEntity) entity)::isBreedingItem)
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
