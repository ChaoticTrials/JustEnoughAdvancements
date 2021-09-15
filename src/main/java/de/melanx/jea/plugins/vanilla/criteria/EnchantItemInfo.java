package de.melanx.jea.plugins.vanilla.criteria;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
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
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

public class EnchantItemInfo implements ICriterionInfo<EnchantedItemTrigger.TriggerInstance> {
    
    private final EnchantmentTableBlockEntity tile = new EnchantmentTableBlockEntity(JeaRender.BELOW_WORLD, Blocks.ENCHANTING_TABLE.defaultBlockState());
    private BlockEntityRenderer<EnchantmentTableBlockEntity> tileRender = null;
    
    @Override
    public Class<EnchantedItemTrigger.TriggerInstance> criterionClass() {
        return EnchantedItemTrigger.TriggerInstance.class;
    }

    @Override
    public void setIngredients(IAdvancementInfo advancement, String criterionKey, EnchantedItemTrigger.TriggerInstance instance, IIngredients ii) {
        ii.setInputLists(VanillaTypes.ITEM, List.of(
                getStacks(instance.item)
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAdvancementInfo advancement, String criterionKey, EnchantedItemTrigger.TriggerInstance instance, IIngredients ii) {
        layout.getItemStacks().init(0, true, 95, SPACE_TOP + 17);
        layout.getItemStacks().set(ii);
    }

    @Override
    public void draw(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, IAdvancementInfo advancement, String criterionKey, EnchantedItemTrigger.TriggerInstance instance, double mouseX, double mouseY) {
        JeaRender.slotAt(poseStack, 95, SPACE_TOP + 17);
        if (!instance.levels.isAny()) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            JustEnoughAdvancementsJEIPlugin.getXpOrb().draw(poseStack, 98, SPACE_TOP + 17);
            RenderSystem.disableBlend();
            Component text1 = new TranslatableComponent("jea.item.tooltip.enchant.level");
            mc.font.draw(poseStack, text1, 109, SPACE_TOP + 17, 0x000000);
            Component text2 = IngredientUtil.text(instance.levels);
            mc.font.draw(poseStack, text2, 99, SPACE_TOP + mc.font.lineHeight + 19, 0x000000);
        }
        float animationTime = (ClientTickHandler.ticksInGame + mc.getFrameTime()) % 110;
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
        poseStack.pushPose();
        poseStack.translate(30, SPACE_TOP + 90, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderSide(poseStack, false, 2.4f);
        SteveRender.defaultPose(mc);
        SteveRender.swing(swing, InteractionHand.MAIN_HAND);
        SteveRender.setEquipmentHand(mc, held);
        SteveRender.renderSteve(mc, poseStack, buffer);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(RECIPE_WIDTH - 65, SPACE_TOP + 85, 0);
        JeaRender.normalize(poseStack);
        JeaRender.transformForEntityRenderFront(poseStack, true, 2);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(10));
        BlockState state = Blocks.ENCHANTING_TABLE.defaultBlockState();
        //noinspection deprecation
        mc.getBlockRenderer().renderSingleBlock(state, poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        this.tile.setLevel(Objects.requireNonNull(mc.level));
        this.tile.blockState = state;
        this.tile.oRot = (float) Math.PI - (0.5f * (1 - bookOpen) * (float) Math.PI);
        this.tile.rot = this.tile.oRot;
        this.tile.oOpen = 2 * (0.5f - (0.5f * (1 - bookOpen)));
        this.tile.open = this.tile.oOpen;
        if (this.tileRender == null) {
            this.tileRender = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(this.tile);
        }
        if (this.tileRender != null) {
            this.tileRender.render(this.tile, mc.getFrameTime(), poseStack, buffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();
    }

    @Override
    public void addTooltip(List<Component> tooltip, IAdvancementInfo advancement, String criterionKey, EnchantedItemTrigger.TriggerInstance instance, double mouseX, double mouseY) {

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
            CompoundTag nbt = stack.getOrCreateTag();
            ListTag list = new ListTag();
            list.add(new CompoundTag());
            nbt.put("Enchantments", list);
            stack.setTag(nbt);
        } else if (stack.hasTag()) {
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.remove("Enchantments");
            stack.setTag(nbt);
        }
        return stack;
    }
}
