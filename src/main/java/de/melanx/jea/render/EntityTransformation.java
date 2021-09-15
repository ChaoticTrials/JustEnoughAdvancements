package de.melanx.jea.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

public class EntityTransformation {
    
    public static final EntityTransformation NOTHING = new EntityTransformation(1, Quaternion.ONE);
    
    private final float entityScale;
    private final Quaternion quaternion;

    public EntityTransformation(float entityScale, Quaternion quaternion) {
        this.entityScale = entityScale;
        this.quaternion = quaternion;
    }
    
    public void applyForEntity(PoseStack poseStack) {
        poseStack.scale(this.entityScale, -this.entityScale, this.entityScale);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-2));
        poseStack.mulPose(this.quaternion);
    }
    
    public void applyForMissing(PoseStack poseStack) {
        poseStack.scale(this.entityScale, -this.entityScale, this.entityScale);
    }
}
