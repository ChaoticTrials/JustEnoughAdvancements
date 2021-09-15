package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.util.IngredientUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledByCrossbowTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class KilledByCrossbowInfo implements ICriterionInfo<KilledByCrossbowTrigger.TriggerInstance> {

    @Override
    public Class<KilledByCrossbowTrigger.TriggerInstance> criterionClass() {
        return KilledByCrossbowTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, KilledByCrossbowTrigger.TriggerInstance instance, IIngredients ii) {
        ItemStack stack = new ItemStack(Items.CROSSBOW);
        if ((instance.uniqueEntityTypes.getMin() != null && instance.uniqueEntityTypes.getMin() >= 2) || instance.victims.length >= 2) {
            stack.enchant(Enchantments.PIERCING, Mth.clamp(Math.max(instance.uniqueEntityTypes.getMin() == null ? 0 :instance.uniqueEntityTypes.getMin(), instance.victims.length) - 1, 1, 4));
        }
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                List.of(stack)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, KilledByCrossbowTrigger.TriggerInstance instance, IIngredients ii) {
        if (instance.victims.length == 0) {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM_FACING_RIGHT, 33, SPACE_TOP + (RECIPE_HEIGHT / 2) - 30, 48, 48, 0, 0);
        } else {
            layout.getItemStacks().init(0, true, Jea.LARGE_ITEM_FACING_RIGHT, 8, SPACE_TOP + (RECIPE_HEIGHT / 2) - 30, 48, 48, 0, 0);
        }
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, KilledByCrossbowTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (!instance.uniqueEntityTypes.isAny()) {
            MutableComponent text = new TranslatableComponent("jea.item.tooltip.crossbow.unique", IngredientUtil.text(instance.uniqueEntityTypes));
            int width = mc.font.width(text);
            //noinspection IntegerDivisionInFloatingPointContext
            mc.font.draw(poseStack, text, (RECIPE_WIDTH / 2) - (width / 2), SPACE_TOP + RECIPE_HEIGHT - 2 - mc.font.lineHeight, 0x000000);
        }
        if (instance.victims.length > 0) {
            int x;
            int space;
            if (instance.victims.length == 1) {
                x = (80 + (RECIPE_WIDTH - 20)) / 2;
                space = 0;
            } else {
                x = 80;
                space = ((RECIPE_WIDTH - 20) - 80) / (instance.victims.length - 1);
            }
            for (EntityPredicate.Composite entity : instance.victims) {
                poseStack.pushPose();
                poseStack.translate(x, SPACE_TOP + 65, 0);
                JeaRender.normalize(poseStack);
                RenderEntityCache.renderEntity(mc, entity, poseStack, buffer, JeaRender.entityRenderSide(true, 1.8f));
                poseStack.popPose();
                x += space;
            }
        }
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, KilledByCrossbowTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        if (instance.victims.length > 0) {
            int x;
            int space;
            if (instance.victims.length == 1) {
                x = (80 + (RECIPE_WIDTH - 20)) / 2;
                space = 0;
            } else {
                x = 80;
                space = ((RECIPE_WIDTH - 20) - 80) / (instance.victims.length - 1);
            }
            for (EntityPredicate.Composite entity : instance.victims) {
                RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, entity, x, SPACE_TOP + 65, JeaRender.normalScale(1.8f), mouseX, mouseY);
                x += space;
            }
        }
    }
}
