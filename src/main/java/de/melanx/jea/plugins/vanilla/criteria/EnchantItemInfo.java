package de.melanx.jea.plugins.vanilla.criteria;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.criterion.ICriterionInfo;
import de.melanx.jea.render.JeaRender;
import de.melanx.jea.render.SteveRender;
import de.melanx.jea.util.IngredientUtil;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.advancements.criterion.EnchantedItemTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Objects;

public class EnchantItemInfo implements ICriterionInfo<EnchantedItemTrigger.Instance> {
    
    private final EnchantingTableTileEntity tile = new EnchantingTableTileEntity();
    private TileEntityRenderer<EnchantingTableTileEntity> tileRender = null;
    
    @Override
    public Class<EnchantedItemTrigger.Instance> criterionClass() {
        return EnchantedItemTrigger.Instance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, EnchantedItemTrigger.Instance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
                getStacks(instance.item)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, EnchantedItemTrigger.Instance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 95, SPACE_TOP + 17);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, EnchantedItemTrigger.Instance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(matrixStack, 95, SPACE_TOP + 17);
        if (!instance.levels.isUnbounded()) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            JustEnoughAdvancementsJEIPlugin.getXpOrb().draw(matrixStack, 98, SPACE_TOP + 17);
            RenderSystem.disableBlend();
            ITextComponent text1 = new TranslationTextComponent("jea.item.tooltip.enchant.level");
            mc.fontRenderer.func_243248_b(matrixStack, text1, 109, SPACE_TOP + 17, 0x000000);
            ITextComponent text2 = IngredientUtil.text(instance.levels);
            mc.fontRenderer.func_243248_b(matrixStack, text2, 99, SPACE_TOP + mc.fontRenderer.FONT_HEIGHT + 19, 0x000000);
        }
        float animationTime = (ClientTickHandler.ticksInGame + mc.getRenderPartialTicks()) % 110;
        float bookOpen;
        float swing;
        ItemStack held;
        if (animationTime < 10) {
            bookOpen = 0;
            swing = 0;
            held = ItemStack.EMPTY;
        } else if (animationTime < 30) {
            bookOpen = 0;
            swing = 0;
            held = getStack(getStacks(instance.item), false);
        } else if (animationTime < 40) {
            bookOpen = 0;
            swing = getSwingTime(animationTime - 30);
            held = swing > 0.5 ? ItemStack.EMPTY : getStack(getStacks(instance.item), false);
        } else if (animationTime < 50) {
            bookOpen = (animationTime - 40) / 10f;
            swing = 0;
            held = ItemStack.EMPTY;
        } else if (animationTime < 70) {
            bookOpen = 1;
            swing = 0;
            held = ItemStack.EMPTY;
        } else if (animationTime < 80) {
            bookOpen = 1 - ((animationTime - 70) / 10f);
            swing = 0;
            held = ItemStack.EMPTY;
        } else if (animationTime < 90) {
            bookOpen = 0;
            swing = getSwingTime(animationTime - 80);
            held = swing < 0.5 ? ItemStack.EMPTY : getStack(getStacks(instance.item), true);
        } else {
            bookOpen = 0;
            swing = 0;
            held = getStack(getStacks(instance.item), true);
        }
        matrixStack.push();
        matrixStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderSide(matrixStack, false, 2.4f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, Hand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, held);
        SteveRender.renderSteve(mc, matrixStack, buffer);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(RECIPE_WIDTH - 65, SPACE_TOP + 85, 0);
        JeaRender.normalize(matrixStack);
        JeaRender.transformForEntityRenderFront(matrixStack, true, 2);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(10));
        BlockState state = Blocks.ENCHANTING_TABLE.getDefaultState();
        //noinspection deprecation
        mc.getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        this.tile.setWorldAndPos(Objects.requireNonNull(mc.world), BlockPos.ZERO);
        this.tile.cachedBlockState = state;
        this.tile.pageAngle = (float) Math.PI - (0.5f * (1 - bookOpen) * (float) Math.PI);
        this.tile.nextPageAngle = this.tile.pageAngle;
        this.tile.pageTurningSpeed = 2 * (0.5f - (0.5f * (1 - bookOpen)));
        this.tile.nextPageTurningSpeed = this.tile.pageTurningSpeed;
        if (this.tileRender == null) {
            this.tileRender = TileEntityRendererDispatcher.instance.getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            this.tileRender.render(this.tile, mc.getRenderPartialTicks(), matrixStack, buffer, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        }
        matrixStack.pop();
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip, IAdvancementInfo advancement, String criterionKey, EnchantedItemTrigger.Instance instance, double mouseX, double mouseY) {

    }

    private static float getSwingTime(float timeTenTicks) {
        if (timeTenTicks < 2) {
            return 0;
        } else if (timeTenTicks > 8) {
            return 1;
        } else {
            return (timeTenTicks - 2) / 6f;
        }
    }
    
    private static List<ItemStack> getStacks(ItemPredicate predicate) {
        return IngredientUtil.fromItemPredicate(predicate, Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_PICKAXE, Items.IRON_SHOVEL);
    }
    
    private static ItemStack getStack(List<ItemStack> stacks, boolean enchanted) {
        // item stack should switch after a whole animation and not while its animated.
        int idx = ((ClientTickHandler.ticksInGame - 2) / 110) % stacks.size();
        ItemStack stack = stacks.get(idx);
        if (enchanted) {
            CompoundNBT nbt = stack.getOrCreateTag();
            ListNBT list = new ListNBT();
            list.add(new CompoundNBT());
            nbt.put("Enchantments", list);
            stack.setTag(nbt);
        } else if (stack.hasTag()) {
            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.remove("Enchantments");
            stack.setTag(nbt);
        }
        return stack;
    }
}
