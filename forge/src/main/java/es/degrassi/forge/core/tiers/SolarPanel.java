package es.degrassi.forge.core.tiers;

import es.degrassi.common.registry.IVariant;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public enum SolarPanel implements IVariant<SolarPanel> {
  T1(
    25_000,
    2_500,
    8,
    Component.translatable("block.degrassi.sp_t1")
  ),
  T2(
    50_000,
    5_000,
    16,
    Component.translatable("block.degrassi.sp_t2")
  ),
  T3(
    100_000,
    10_000,
    64,
    Component.translatable("block.degrassi.sp_t3")
  ),
  T4(
    250_000,
    25_000,
    256,
    Component.translatable("block.degrassi.sp_t4")
  ),
  T5(
    500_000,
    50_000,
    1_024,
    Component.translatable("block.degrassi.sp_t5")
  ),
  T6(
    1_000_000,
    100_000,
    4_196,
    Component.translatable("block.degrassi.sp_t6")
  ),
  T7(
    10_000_000,
    1_000_000,
    16_784,
    Component.translatable("block.degrassi.sp_t7")
  ),
  T8(
    100_000_000,
    10_000_000,
    33_568,
    Component.translatable("block.degrassi.sp_t8")
  );

  private final int energyCapacity, energyTransfer, maxGeneration;
  private final Component name;

  SolarPanel(
    int energyCapacity,
    int energyTransfer,
    int maxGeneration,
    Component name
  ) {
    this.energyCapacity = energyCapacity;
    this.energyTransfer = energyCapacity;
    this.maxGeneration = maxGeneration;
    this.name = name;
  }

  public int getEnergyCapacity() {
    return energyCapacity;
  }

  public int getMaxGeneration() {
    return maxGeneration;
  }

  public int getEnergyTransfer() {
    return energyTransfer;
  }

  @Override
  public SolarPanel[] getVariants() {
    return values();
  }

  public static SolarPanel[] getNormalVariants() {
    return new SolarPanel[] { T1, T2, T3, T4, T5, T6, T7, T8 };
  }

  public Component getTranslation() {
    return name;
  }

  public String getName() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static SolarPanel value (@NotNull String tier) {
    return switch (tier.toLowerCase()) {
      case "t8" -> T8;
      case "t7" -> T7;
      case "t6" -> T6;
      case "t5" -> T5;
      case "t4" -> T4;
      case "t3" -> T3;
      case "t2" -> T2;
      default -> T1;
    };
  }
}
