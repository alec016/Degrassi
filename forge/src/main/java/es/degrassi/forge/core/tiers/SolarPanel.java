package es.degrassi.forge.core.tiers;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum SolarPanel {
  T1(
    25_000,
    EntityRegistration.SP1,
    Component.translatable("block.degrassi.sp1")
  ),
  T2(
    50_000,
    EntityRegistration.SP2,
    Component.translatable("block.degrassi.sp2")
  ),
  T3(
    100_000,
    EntityRegistration.SP3,
    Component.translatable("block.degrassi.sp3")
  ),
  T4(
    250_000,
    EntityRegistration.SP4,
    Component.translatable("block.degrassi.sp4")
  ),
  T5(
    500_000,
    EntityRegistration.SP5,
    Component.translatable("block.degrassi.sp5")
  ),
  T6(
    1_000_000,
    EntityRegistration.SP6,
    Component.translatable("block.degrassi.sp6")
  ),
  T7(
    10_000_000,
    EntityRegistration.SP7,
    Component.translatable("block.degrassi.sp7")
  ),
  T8(
    100_000_000,
    EntityRegistration.SP8,
    Component.translatable("block.degrassi.sp8")
  );

  private final int energyCapacity;
  private final RegistrySupplier<BlockEntityType<SolarPanelEntity>> type;
  private final Component name;

  SolarPanel(
    int energyCapacity,
    RegistrySupplier<BlockEntityType<SolarPanelEntity>> type,
    Component name
  ) {
    this.energyCapacity = energyCapacity;
    this.type = type;
    this.name = name;
  }

  public int getEnergyCapacity() {
    return energyCapacity;
  }

  public RegistrySupplier<BlockEntityType<SolarPanelEntity>> getType() {
    return type;
  }

  public Component getName() {
    return name;
  }

  public String getTextureName() {
    return switch (this) {
      case T1 -> "sp1";
      case T2 -> "sp2";
      case T3 -> "sp3";
      case T4 -> "sp4";
      case T5 -> "sp5";
      case T6 -> "sp6";
      case T7 -> "sp7";
      case T8 -> "sp8";
    };
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
