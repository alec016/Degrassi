package es.degrassi.forge.init.entity.renderer;

public interface Scale<Self> {
  Self scale(float factorX, float factorY, float factorZ);

  default Self scale(float factor) {
    return scale(factor, factor, factor);
  }
}
