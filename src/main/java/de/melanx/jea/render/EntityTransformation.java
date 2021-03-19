package de.melanx.jea.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class EntityTransformation {
    
    public static final EntityTransformation NOTHING = new EntityTransformation(1, Quaternion.ONE);
    
    private final float entityScale;
    private final Quaternion quaternion;

    public EntityTransformation(float entityScale, Quaternion quaternion) {
        this.entityScale = entityScale;
        this.quaternion = quaternion;
    }
    
    public void applyForEntity(MatrixStack matrixStack) {
        matrixStack.scale(this.entityScale, -this.entityScale, this.entityScale);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-2));
        matrixStack.rotate(this.quaternion);
    }
    
    public void applyForMissing(MatrixStack matrixStack) {
        matrixStack.scale(this.entityScale, -this.entityScale, this.entityScale);
    }
}
