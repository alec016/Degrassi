package es.degrassi.common.utils;

import com.mojang.blaze3d.vertex.PoseStack;

public interface TransformStack extends Transform<TransformStack>, TStack<TransformStack> {
  static TransformStack cast(PoseStack stack) {
    return (TransformStack) stack;
  }
}
