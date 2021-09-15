package de.melanx.jea.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SteveRender {
    
    private static ClientLevel currentLevel;
    private static FakeClientPlayer fakePlayer;
    private static FakeFishingBobber fakeBobber;
    
    public static void defaultPose(Minecraft mc) {
        setPose(mc, 0, 0, 0, 0, Pose.STANDING, InteractionHand.MAIN_HAND, false, false, 0, false);
    }
    
    public static void setPose(Minecraft mc, float yRot, float xRot, float swingProgress, int usingTick, Pose pose, InteractionHand hand, boolean sneaking, boolean sprinting, float limbSwingAmount, boolean sitting) {
        updateFakePlayer(mc);
        fakePlayer.setYRot(0);
        fakePlayer.yRotO = 0;
        fakePlayer.yHeadRot = yRot;
        fakePlayer.yHeadRotO = yRot;
        fakePlayer.setXRot(xRot);
        fakePlayer.xRotO = xRot;
        fakePlayer.useItemRemaining = usingTick;
        fakePlayer.attackAnim = swingProgress;
        fakePlayer.oAttackAnim = swingProgress;
        fakePlayer.swingTime = (int) (6 * swingProgress);
        fakePlayer.setPose(pose);
        fakePlayer.swingingArm = hand;
        fakePlayer.startUsingItem(hand);
        fakePlayer.setShiftKeyDown(sneaking);
        fakePlayer.setSprinting(sprinting);
        fakePlayer.animationSpeed = limbSwingAmount;
        fakePlayer.animationSpeedOld = limbSwingAmount;
        if (sitting) {
            fakePlayer.vehicle = fakePlayer;
        } else {
            fakePlayer.vehicle = null;
        }
        
        // Technically not the pose but still used together with it
        fakePlayer.setRemainingFireTicks(0);
        fakePlayer.hurtTime = 0;
        fakePlayer.fishing = null;
    }
    
    public static void swing(float swingProgress, InteractionHand hand) {
        if (fakePlayer != null) {
            fakePlayer.attackAnim = swingProgress;
            fakePlayer.oAttackAnim = swingProgress;
            if (swingProgress != 0) {
                fakePlayer.swingingArm = hand;
                fakePlayer.startUsingItem(hand);
            }
            fakePlayer.useItemRemaining = 0;
        }
    }

    public static void limbSwing(float limbSwingAmount) {
        if (fakePlayer != null) {
            fakePlayer.animationSpeed = limbSwingAmount;
            fakePlayer.animationSpeedOld = limbSwingAmount;
        }
    }

    public static void use(int useTick, InteractionHand hand) {
        if (fakePlayer != null) {
            if (useTick != 0) {
                fakePlayer.swingingArm = hand;
                fakePlayer.startUsingItem(hand);
            }
            fakePlayer.useItemRemaining = useTick;
        }
    }
    
    public static void fireTicks(int fire) {
        if (fakePlayer != null) {
            fakePlayer.setRemainingFireTicks(fire);
        }
    }
    
    public static void hurtTime(int hurt) {
        if (fakePlayer != null) {
            fakePlayer.hurtTime = hurt;
        }
    }
    
    public static void fishingBobber(boolean bobber) {
        if (fakePlayer != null) {
            fakePlayer.fishing = bobber ? fakeBobber : null;
        }
    }
    
    public static void sitting(boolean sitting) {
        if (fakePlayer != null) {
            if (sitting) {
                fakePlayer.vehicle = fakePlayer;
            } else {
                fakePlayer.vehicle = null;
            }
        }
    }
    
    public static void rotationHead(float yRot, float xRot) {
        if (fakePlayer != null) {
            fakePlayer.yHeadRot = yRot;
            fakePlayer.yHeadRotO = yRot;
            fakePlayer.setXRot(xRot);
            fakePlayer.xRotO = xRot;
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
        fakePlayer.getInventory().selected = 0;
        fakePlayer.getInventory().items.set(0, mainHand.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : mainHand);
        fakePlayer.getInventory().offhand.set(0, offHand.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : offHand);
        fakePlayer.getInventory().armor.set(EquipmentSlot.HEAD.getIndex(), head.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : head);
        fakePlayer.getInventory().armor.set(EquipmentSlot.CHEST.getIndex(), chest.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : chest);
        fakePlayer.getInventory().armor.set(EquipmentSlot.LEGS.getIndex(), legs.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : legs);
        fakePlayer.getInventory().armor.set(EquipmentSlot.FEET.getIndex(), feet.getItem() == Items.STRUCTURE_VOID ? ItemStack.EMPTY : feet);
        fakePlayer.useItem = fakePlayer.getUsedItemHand() == InteractionHand.MAIN_HAND ? mainHand : offHand;
    }
    
    public static void renderSteve(Minecraft mc, PoseStack poseStack, MultiBufferSource buffer) {
        if (fakePlayer != null) {
            EntityRenderer<? super FakeClientPlayer> renderer = mc.getEntityRenderDispatcher().getRenderer(fakePlayer);
            renderer.render(fakePlayer, fakePlayer.getYRot(), mc.getFrameTime(), poseStack, buffer, LightTexture.pack(15, 15));
        }
    }
    
    public static void renderSteveStatic(Minecraft mc, PoseStack poseStack, MultiBufferSource buffer) {
        if (fakePlayer != null) {
            EntityRenderer<? super FakeClientPlayer> renderer = mc.getEntityRenderDispatcher().getRenderer(fakePlayer);
            renderer.render(fakePlayer, fakePlayer.getYRot(), 0, poseStack, buffer, LightTexture.pack(15, 15));
        }
    }
    
    private static void updateFakePlayer(Minecraft mc) {
        if (currentLevel == null || mc.level != currentLevel) {
            currentLevel = mc.level;
            fakePlayer = new FakeClientPlayer(mc.level);
            fakeBobber = new FakeFishingBobber(fakePlayer);
            fakePlayer.fishing = null;
        }
    }
}
