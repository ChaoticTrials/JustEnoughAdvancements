package de.melanx.jea.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.core.BlockPos;

import java.util.List;

public class JeaRender {
    
    public static final BlockPos BELOW_WORLD = new BlockPos(0, -655538, 0);

    public static double normalScale(double totalScale) {
        return 16 * totalScale;
    }
    
    public static void normalize(PoseStack poseStack) {
        normalize(poseStack, 0, 0);
    }
    
    public static void normalize(PoseStack poseStack, int x, int y) {
        poseStack.translate(x, y, 60);
        poseStack.scale(16, 16, 16);
    }
    
    public static void transformForEntityRenderFront(PoseStack poseStack, boolean right, float entityScale) {
        poseStack.scale(entityScale, -entityScale, entityScale);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-2));
        if (right) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-15));
        } else {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(15));
        }
    }
    
    public static void transformForEntityRenderSide(PoseStack poseStack, boolean right, float entityScale) {
        poseStack.scale(entityScale, -entityScale, entityScale);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-2));
        if (right) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-65));
        } else {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(65));
        }
    }
    
    public static EntityTransformation entityRenderFront(boolean right, float entityScale) {
        if (right) {
            return new EntityTransformation(entityScale, Vector3f.YP.rotationDegrees(-15));
        } else {
            return new EntityTransformation(entityScale, Vector3f.YP.rotationDegrees(15));
        }
    }
    
    public static EntityTransformation entityRenderSide(boolean right, float entityScale) {
        if (right) {
            return new EntityTransformation(entityScale, Vector3f.YP.rotationDegrees(-65));
        } else {
            return new EntityTransformation(entityScale, Vector3f.YP.rotationDegrees(65));
        }
    }
    
    public static <T> T cycle(List<T> list) {
        return cycle(list, 20);
    }

    public static <T> T cycle(List<T> list, int time) {
        return list.get((((ClientTickHandler.ticksInGame / time) % list.size()) + list.size()) % list.size());
    }
    
    public static <T extends Enum<T>> T cycle(Class<T> enumClass) {
        return cycle(enumClass, 20);
    }
    
    public static <T extends Enum<T>> T cycle(Class<T> enumClass, int time) {
        T[] constants = enumClass.getEnumConstants();
        return constants[(((ClientTickHandler.ticksInGame / time) % constants.length) + constants.length) % constants.length];
    }
    
    public static void slotAt(PoseStack poseStack, int x, int y) {
        if (JustEnoughAdvancementsJEIPlugin.getSlot() != null) {
            JustEnoughAdvancementsJEIPlugin.getSlot().draw(poseStack, x, y);
        }
    }
}
