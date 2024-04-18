package es.degrassi.common.utils;

import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@SuppressWarnings("unchecked")
public interface Rotate<Self> {

  Self multiply(Quaternionf var1);

  default Self rotate(Direction axis, float radians) {
    return radians == 0.0F ? (Self) this : this.multiply((new Quaternionf()).rotationAxis(radians, axis.step()));
  }

  default Self rotate(double angle, Direction.Axis axis) {
    Axis vec = axis == net.minecraft.core.Direction.Axis.X ? Axis.XP : (axis == net.minecraft.core.Direction.Axis.Y ? Axis.YP : Axis.ZP);
    return this.multiply(vec, angle);
  }

  default Self rotateX(double angle) {
    return this.multiply(Axis.XP, angle);
  }

  default Self rotateY(double angle) {
    return this.multiply(Axis.YP, angle);
  }

  default Self rotateZ(double angle) {
    return this.multiply(Axis.ZP, angle);
  }

  default Self rotateXRadians(double angle) {
    return this.multiplyRadians(Axis.XP, angle);
  }

  default Self rotateYRadians(double angle) {
    return this.multiplyRadians(Axis.YP, angle);
  }

  default Self rotateZRadians(double angle) {
    return this.multiplyRadians(Axis.ZP, angle);
  }

  default Self multiply(Axis axis, double angle) {
    return angle == 0.0 ? (Self) this : this.multiply(axis.rotationDegrees((float)angle));
  }

  default Self multiplyRadians(Axis axis, double angle) {
    return angle == 0.0 ? (Self) this : this.multiply(axis.rotation((float)angle));
  }

  default Self multiply(Vector3f axis, double angle) {
    return angle == 0.0 ? (Self) this : this.multiply(new Quaternionf(new AxisAngle4f((float)Math.toRadians(angle), axis)));
  }

  default Self multiplyRadians(Vector3f axis, double angle) {
    return angle == 0.0 ? (Self) this : this.multiply(new Quaternionf(new AxisAngle4f((float)angle, axis)));
  }

  default Self rotateToFace(Direction facing) {
    switch (facing) {
      case SOUTH -> this.multiply(Axis.YP.rotationDegrees(180.0F));
      case WEST -> this.multiply(Axis.YP.rotationDegrees(90.0F));
      case NORTH -> this.multiply(Axis.YP.rotationDegrees(0.0F));
      case EAST -> this.multiply(Axis.YP.rotationDegrees(270.0F));
      case UP -> this.multiply(Axis.XP.rotationDegrees(90.0F));
      case DOWN -> this.multiply(Axis.XN.rotationDegrees(90.0F));
    }

    return (Self) this;
  }
}
