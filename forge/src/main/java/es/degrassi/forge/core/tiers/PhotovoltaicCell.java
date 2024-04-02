package es.degrassi.forge.core.tiers;

import es.degrassi.common.registry.IVariant;

public enum PhotovoltaicCell implements IVariant<PhotovoltaicCell> {
  I,
  II,
  III,
  IV,
  V,
  VI,
  VII,
  VIII;

  @Override
  public PhotovoltaicCell[] getVariants() {
    return values();
  }

  public static PhotovoltaicCell[] getNormalizedVariants() {
    return new PhotovoltaicCell[] { I, II, III, IV, V, VI, VII, VIII };
  }
}
