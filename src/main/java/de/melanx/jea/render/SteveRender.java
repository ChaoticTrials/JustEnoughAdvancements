package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Pose;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class SteveRender {
    
    private static ClientWorld currentWorld;
    private static FakeClientPlayer fakePlayer;
    private static FakeFishingBobber fakeBobber;
    
    public static void defaultPose(Minecraft mc) {
        setPose(mc, 0, 0, 0, 0, Pose.STANDING, Hand.MAIN_HAND, false, false, 0, false);
    }
    
    public static void setPose(Minecraft mc, float yaw, float pitch, float swingProgress, int usingTick, Pose pose, Hand hand, boolean sneaking, boolean sprinting, float limbSwingAmount, boolean sitting) {
        updateFakePlayer(mc);
        fakePlayer.rotationYaw = 0;
        fakePlayer.prevRotationYaw = 0;
        fakePlayer.rotationYawHead = yaw;
        fakePlayer.prevRotationYawHead = yaw;
        fakePlayer.rotationPitch = pitch;
        fakePlayer.activeItemStackUseCount = usingTick;
        fakePlayer.prevRotationPitch = pitch;
        fakePlayer.swingProgress = swingProgress;
        fakePlayer.prevSwingProgress = swingProgress;
        fakePlayer.swingProgressInt = (int) (6 * swingProgress);
        fakePlayer.setPose(pose);
        fakePlayer.swingingHand = hand;
        fakePlayer.setActiveHand(hand);
        fakePlayer.setSneaking(sneaking);
        fakePlayer.setSprinting(sprinting);
        fakePlayer.limbSwingAmount = limbSwingAmount;
        fakePlayer.prevLimbSwingAmount = limbSwingAmount;
        if (sitting) {
            fakePlayer.ridingEntity = fakePlayer;
        } else {
            fakePlayer.ridingEntity = null;
        }
        
        // Technically not the pose but still used together with it
        fakePlayer.forceFireTicks(0);
        fakePlayer.hurtTime = 0;
        fakePlayer.fishingBobber = null;
    }
    
    public static void swing(float swingProgress, Hand hand) {
        if (fakePlayer != null) {
            fakePlayer.swingProgress = swingProgress;
            fakePlayer.prevSwingProgress = swingProgress;
            if (swingProgress != 0) {
                fakePlayer.swingingHand = hand;
                fakePlayer.setActiveHand(hand);
            }
        }
    }

    public static void limbSwing(float limbSwingAmount) {
        if (fakePlayer != null) {
            fakePlayer.limbSwingAmount = limbSwingAmount;
            fakePlayer.prevLimbSwingAmount = limbSwingAmount;
        }
    }

    public static void use(int useTick, Hand hand) {
        if (fakePlayer != null) {
            fakePlayer.activeItemStackUseCount = useTick;
            if (useTick != 0) {
                fakePlayer.swingingHand = hand;
                fakePlayer.setActiveHand(hand);
            }
        }
    }
    
    public static void fireTicks(int fire) {
        if (fakePlayer != null) {
            fakePlayer.forceFireTicks(fire);
        }
    }
    
    public static void hurtTime(int hurt) {
        if (fakePlayer != null) {
            fakePlayer.hurtTime = hurt;
        }
    }
    
    public static void fishingBobber(boolean bobber) {
        if (fakePlayer != null) {
            fakePlayer.fishingBobber = bobber ? fakeBobber : null;
        }
    }
    
    public static void sitting(boolean sitting) {
        if (fakePlayer != null) {
            if (sitting) {
                fakePlayer.ridingEntity = fakePlayer;
            } else {
                fakePlayer.ridingEntity = null;
            }
        }
    }
    
    public static void clearEquipment(Minecraft mc) {
        setEquipment(mc, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
    }
    
    public static void setEquipmentHand(Minecraft mc, ItemStack mainHand) {
        setEquipment(mc, mainHand, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
    }
    
    public static void setEquipment(Minecraft mc, ItemStack mainHand, ItemStack offHand, ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) {
        updateFakePlayer(mc);
        fakePlayer.inventory.currentItem = 0;
        fakePlayer.inventory.mainInventory.set(0, mainHand.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : mainHand);
        fakePlayer.inventory.offHandInventory.set(0, offHand.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : offHand);
        fakePlayer.inventory.armorInventory.set(EquipmentSlotType.HEAD.getIndex(), head.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : head);
        fakePlayer.inventory.armorInventory.set(EquipmentSlotType.CHEST.getIndex(), chest.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : chest);
        fakePlayer.inventory.armorInventory.set(EquipmentSlotType.LEGS.getIndex(), legs.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : legs);
        fakePlayer.inventory.armorInventory.set(EquipmentSlotType.FEET.getIndex(), feet.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : feet);
        fakePlayer.activeItemStack = fakePlayer.getActiveHand() == Hand.MAIN_HAND ? mainHand : offHand;
    }
    
    public static void renderSteve(Minecraft mc, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        if (fakePlayer != null) {
            EntityRenderer<? super FakeClientPlayer> renderer = mc.getRenderManager().getRenderer(fakePlayer);
            renderer.render(fakePlayer, fakePlayer.rotationYaw, mc.getRenderPartialTicks(), matrixStack, buffer, LightTexture.packLight(15, 15));
        }
    }
    
    public static void renderSteveStatic(Minecraft mc, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        if (fakePlayer != null) {
            EntityRenderer<? super FakeClientPlayer> renderer = mc.getRenderManager().getRenderer(fakePlayer);
            renderer.render(fakePlayer, fakePlayer.rotationYaw, 0, matrixStack, buffer, LightTexture.packLight(15, 15));
        }
    }
    
    private static void updateFakePlayer(Minecraft mc) {
        if (currentWorld == null || mc.world != currentWorld) {
            currentWorld = mc.world;
            fakePlayer = new FakeClientPlayer(mc.world);
            fakeBobber = new FakeFishingBobber(fakePlayer);
            fakePlayer.fishingBobber = null;
        }
    }
}
