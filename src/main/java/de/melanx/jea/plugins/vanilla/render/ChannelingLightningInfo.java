package de.melanx.jea.plugins.vanilla.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancements;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.RenderEntityCache;
import de.melanx.jea.render.SteveRender;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.ChanneledLightningTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Pose;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class ChannelingLightningInfo implements ICriterionInfo<ChanneledLightningTrigger.Instance> {
    
    public static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "textures/lightning.png");
    private final ItemStack trident;

    public ChannelingLightningInfo() {
        this.trident = new ItemStack(Items.TRIDENT);
        this.trident.addEnchantment(Enchantments.CHANNELING, 1);
    }

    @Override
    public Class<ChanneledLightningTrigger.Instance> criterionClass() {
        return ChanneledLightningTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, ChanneledLightningTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, ChanneledLightningTrigger.Instance instance, IIngredients ii) {

    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, ChanneledLightningTrigger.Instance instance, double mouseX, double mouseY) {
        matrixStack.push();
        matrixStack.translate(10, SPACE_TOP + 55, 0);
        JeaRender.normalize(matrixStack);
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(60));
        JeaRender.transformForEntityRenderSide(matrixStack, true, 2);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-45));
        SteveRender.setPose(mc, 180, -70, 0, 1, Pose.STANDING, Hand.MAIN_HAND, false, false, 0);
        SteveRender.setEquipmentHand(mc, this.trident);
        SteveRender.renderSteveStatic(mc, matrixStack, buffer);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 84, SPACE_TOP + RECIPE_HEIGHT - 84 - 2, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        JustEnoughAdvancementsJEIPlugin.getLightning().draw(matrixStack);
        RenderSystem.disableBlend();
        matrixStack.pop();
        if (instance.victims.length > 0) {
            matrixStack.push();
            matrixStack.translate(RECIPE_WIDTH - 24, SPACE_TOP + RECIPE_HEIGHT - 2, 0);
            JeaRender.normalize(matrixStack);
            int victimIdx = ((ClientTickHandler.ticksInGame / 20) % instance.victims.length);
            RenderEntityCache.renderEntity(mc, instance.victims[victimIdx], matrixStack, buffer, JeaRender.entityRenderFront(true, 1.5f));
            matrixStack.pop();
        }
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, ChanneledLightningTrigger.Instance instance, double mouseX, double mouseY) {
        if (instance.victims.length > 0) {
            int victimIdx = ((ClientTickHandler.ticksInGame / 20) % instance.victims.length);
            RenderEntityCache.addTooltipForEntity(Minecraft.getInstance(), tooltip, instance.victims[victimIdx], RECIPE_WIDTH - 24, SPACE_TOP + RECIPE_HEIGHT - 2, JeaRender.normalScale(1.5f), mouseX, mouseY);
        }
    }
}
