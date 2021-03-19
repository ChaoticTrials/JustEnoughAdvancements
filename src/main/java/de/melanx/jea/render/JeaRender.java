package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;

import java.util.List;

public class JeaRender {
    
    public static final BlockPos BELOW_WORLD = new BlockPos(0, -2, 0);

    public static double normalScale(double totalScale) {
        return 16 * totalScale;
    }
    
    public static void normalize(MatrixStack matrixStack) {
        normalize(matrixStack, 0, 0);
    }
    
    public static void normalize(MatrixStack matrixStack, int x, int y) {
        matrixStack.translate(x, y, 60);
        matrixStack.scale(16, 16, 16);
    }
    
    public static void transformForEntityRenderFront(MatrixStack matrixStack, boolean right, float entityScale) {
        matrixStack.scale(entityScale, -entityScale, entityScale);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-2));
        if (right) {
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-15));
        } else {
            matrixStack.rotate(Vector3f.YP.rotationDegrees(15));
        }
    }
    
    public static void transformForEntityRenderSide(MatrixStack matrixStack, boolean right, float entityScale) {
        matrixStack.scale(entityScale, -entityScale, entityScale);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-2));
        if (right) {
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-65));
        } else {
            matrixStack.rotate(Vector3f.YP.rotationDegrees(65));
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
        return list.get((((ClientTickHandler.ticksInGame / 20) % list.size()) + list.size()) % list.size());
    }
    
    public static void slotAt(MatrixStack matrixStack, int x, int y) {
        if (JustEnoughAdvancementsJEIPlugin.getSlot() != null) {
            JustEnoughAdvancementsJEIPlugin.getSlot().draw(matrixStack, x, y);
        }
    }
}
