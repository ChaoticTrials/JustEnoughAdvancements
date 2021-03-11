package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DefaultEntityProperties;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.CuredZombieVillagerTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class CureZombieVillagerInfo implements ICriterionInfo<CuredZombieVillagerTrigger.Instance> {

    @Override
    public Class<CuredZombieVillagerTrigger.Instance> criterionClass() {
        return CuredZombieVillagerTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, CuredZombieVillagerTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                ImmutableList.of(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.WEAKNESS)),
                ImmutableList.of(new ItemStack(Items.GOLDEN_APPLE))
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, CuredZombieVillagerTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 20, SPACE_TOP + 47);
        layout.getItemStacks().init(1, true, (RECIPE_WIDTH / 2) + 2, SPACE_TOP + 47);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, CuredZombieVillagerTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, (RECIPE_WIDTH / 2) - 20, SPACE_TOP + 47);
        JeaRender.slotAt(matrixStack, (RECIPE_WIDTH / 2) + 2, SPACE_TOP + 47);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        JustEnoughAdvancementsJEIPlugin.getRightArrow().draw(matrixStack, (RECIPE_WIDTH / 2) - 16, SPACE_TOP + 29);
        RenderSystem.disableBlend();
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 80, 0);
        JeaRender.normalize(matrixStack);
        RenderEntityCache.renderEntity(mc, instance.zombie, matrixStack, buffer, JeaRender.entityRenderFront(false, 2), DefaultEntityProperties.ZOMBIE_VILLAGER);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 30, SPACE_TOP + 80, 0);
        JeaRender.normalize(matrixStack);
        RenderEntityCache.renderEntity(mc, instance.villager, matrixStack, buffer, JeaRender.entityRenderFront(true, 2), DefaultEntityProperties.VILLAGER);
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, CuredZombieVillagerTrigger.Instance instance, double mouseX, double mouseY) {
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.zombie, 30, SPACE_TOP + 80, JeaRender.normalScale(2), DefaultEntityProperties.ZOMBIE_VILLAGER, mouseX, mouseY);
        RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.villager, RECIPE_WIDTH - 30, SPACE_TOP + 80, JeaRender.normalScale(2), DefaultEntityProperties.VILLAGER, mouseX, mouseY);
    }
}
