package es.degrassi.common.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@SuppressWarnings("unchecked")
public interface Transform<Self extends Transform<Self>> extends Translate<Self>, Rotate<Self>, Scale<Self> {
  Self mulPose(Matrix4f var1);

  Self mulNormal(Matrix3f var1);

  default Self transform(Matrix4f pose, Matrix3f normal) {
    this.mulPose(pose);
    return this.mulNormal(normal);
  }

  default Self transform(PoseStack stack) {
    PoseStack.Pose last = stack.last();
    return this.transform(last.pose(), last.normal());
  }

  default Self rotateCentered(Direction axis, float radians) {
    ((Transform)((Transform)this.translate(0.5, 0.5, 0.5)).rotate(axis, radians)).translate(-0.5, -0.5, -0.5);
    return (Self) this;
  }

  default Self rotateCentered(Quaternionf q) {
    ((Transform)((Transform)this.translate(0.5, 0.5, 0.5)).multiply(q)).translate(-0.5, -0.5, -0.5);
    return (Self) this;
  }
}
