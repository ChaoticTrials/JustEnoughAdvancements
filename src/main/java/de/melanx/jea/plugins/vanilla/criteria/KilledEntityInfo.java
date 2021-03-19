package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.DamageUtil;
import de.melanx.jea.render.JeaRender;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.DamagePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class KilledEntityInfo implements ICriterionInfo<KilledTrigger.Instance> {

    public static final ResourceLocation PILLAGER_ADVANCEMENT = new ResourceLocation("minecraft", "adventure/voluntary_exile");
    public static final String PILLAGER_STRING = "voluntary_exile";
    
    @Override
    public Class<KilledTrigger.Instance> criterionClass() {
        return KilledTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, KilledTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, KilledTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        ItemStack stack = new ItemStack(Items.SKELETON_SKULL);
        stack.setDisplayName(new TranslationTextComponent("jea.item.tooltip.damage.kill_entity").mergeStyle(TextFormatting.RED));
        layout.getItemStacks().set(0, stack);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, KilledTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, (RECIPE_WIDTH / 2) - 9, SPACE_TOP + 4);
        DamagePredicate predicate = new DamagePredicate(MinMaxBounds.FloatBound.UNBOUNDED, MinMaxBounds.FloatBound.UNBOUNDED, EntityPredicate.ANY, null, instance.killingBlow);
        DamageUtil.draw(matrixStack, buffer, mc, predicate, instance.entity, true, false, null, this.getForcedTarget(advancement, criterionKey));
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, KilledTrigger.Instance instance, double mouseX, double mouseY) {
        DamagePredicate predicate = new DamagePredicate(MinMaxBounds.FloatBound.UNBOUNDED, MinMaxBounds.FloatBound.UNBOUNDED, EntityPredicate.ANY, null, instance.killingBlow);
        DamageUtil.addTooltip(tooltip, Minecraft.getInstance(), predicate, instance.entity, true, false, null, this.getForcedTarget(advancement, criterionKey), mouseX, mouseY);
    }
    
    @Nullable
    private EntityType<?> getForcedTarget(IAdvancementInfo info, String criterion) {
        if (PILLAGER_ADVANCEMENT.equals(info.getId()) && PILLAGER_STRING.equals(criterion)) {
            return EntityType.PILLAGER;
        } else {
            return null;
        }
    }
}
