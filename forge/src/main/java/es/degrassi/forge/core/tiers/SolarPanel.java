package es.degrassi.forge.core.tiers;

import es.degrassi.common.registry.IVariant;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum SolarPanel implements IVariant<SolarPanel> {
  T1(
    25_000,
    8,
    Component.translatable("block.degrassi.sp_t1")
  ),
  T2(
    50_000,
    16,
    Component.translatable("block.degrassi.sp_t2")
  ),
  T3(
    100_000,
    64,
    Component.translatable("block.degrassi.sp_t3")
  ),
  T4(
    250_000,
    256,
    Component.translatable("block.degrassi.sp_t4")
  ),
  T5(
    500_000,
    1_024,
    Component.translatable("block.degrassi.sp_t5")
  ),
  T6(
    1_000_000,
    4_196,
    Component.translatable("block.degrassi.sp_t6")
  ),
  T7(
    10_000_000,
    16_784,
    Component.translatable("block.degrassi.sp_t7")
  ),
  T8(
    100_000_000,
    33_568,
    Component.translatable("block.degrassi.sp_t8")
  );

  private final int energyCapacity, maxGeneration;
  private final Component name;

  SolarPanel(
    int energyCapacity,
    int maxGeneration,
    Component name
  ) {
    this.energyCapacity = energyCapacity;
    this.maxGeneration = maxGeneration;
    this.name = name;
  }

  public int getEnergyCapacity() {
    return energyCapacity;
  }

  public int getMaxGeneration() {
    return maxGeneration;
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

  public static @Nullable SolarPanel value (@NotNull String tier) {
    if (tier.equalsIgnoreCase("t1")) return T1;
    if (tier.equalsIgnoreCase("t2")) return T2;
    if (tier.equalsIgnoreCase("t3")) return T3;
    if (tier.equalsIgnoreCase("t4")) return T4;
    if (tier.equalsIgnoreCase("t5")) return T5;
    if (tier.equalsIgnoreCase("t6")) return T6;
    if (tier.equalsIgnoreCase("t7")) return T7;
    if (tier.equalsIgnoreCase("t8")) return T8;
    return null;
  }
}
