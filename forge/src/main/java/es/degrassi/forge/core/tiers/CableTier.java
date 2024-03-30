package es.degrassi.forge.core.tiers;

import es.degrassi.common.registry.IVariant;
import java.util.Locale;

public enum CableTier implements IVariant<CableTier> {
  BASIC(
    0,
    10_000,
    1_000,
    10_000,
    100
  ),
  ADVANCE(
    0,
    100_000,
    10_000,
    50_000,
    1_000
  ),
  EXTREME(
    0,
    1_000_000,
    100_000,
    100_000,
    10_000
  );

  private final int color;
  private final int energyCapacity, energyTransfer;
  private final int fluidCapacity, fluidTransfer;

  CableTier(int color, int energyCapacity, int energyTransfer, int fluidCapacity, int fluidTransfer) {
    this.color = color;
    this.energyCapacity = energyCapacity;
    this.energyTransfer = energyTransfer;
    this.fluidCapacity = fluidCapacity;
    this.fluidTransfer = fluidTransfer;
  }

  @Override
  public CableTier[] getVariants() {
    return values();
  }

  public int getEnergyCapacity() {
    return energyCapacity;
  }

  public int getEnergyTransfer() {
    return energyTransfer;
  }

  public int getFluidCapacity() {
    return fluidCapacity;
  }

  public int getFluidTransfer() {
    return fluidTransfer;
  }

  public static CableTier[] getNormalVariants() {
    return new CableTier[] { BASIC, ADVANCE, EXTREME };
  }

  public int getColor() {
    return color;
  }

  public String getName() {
    return name().toLowerCase(Locale.ENGLISH);
  }
}
