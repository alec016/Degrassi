package es.degrassi.forge.core.tiers;

import es.degrassi.common.registry.IVariant;
import java.util.Locale;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@Getter
public enum Furnace implements IVariant<Furnace> {
  IRON(
    25_000,
    1_000,
    1,
    1,
    Component.translatable("block.degrassi.furnace_iron")
  ),
  GOLD(
    50_000,
    5_000,
    1.5,
    0.75,
    Component.translatable("block.degrassi.furnace_gold")
  ),
  DIAMOND(
    100_000,
    10_000,
    2.5,
    0.55,
    Component.translatable("block.degrassi.furnace_diamond")
  ),
  EMERALD(
    250_000,
    50_000,
    3.75,
    0.3,
    Component.translatable("block.degrassi.furnace_emerald")
  ),
  NETHERITE(
    500_000,
    100_000,
    4.5,
    0.1,
    Component.translatable("block.degrassi.furnace_netherite")
  );

  private final int energyCapacity;
  private final float experienceCapacity;
  private final Component name;
  private final double energyModifier, speedModifier;

  Furnace(
    int energyCapacity,
    float experienceCapacity,
    double energyModifier,
    double speedModifier,
    Component name
  ) {
    this.energyCapacity = energyCapacity;
    this.experienceCapacity = experienceCapacity;
    this.name = name;
    this.energyModifier = energyModifier;
    this.speedModifier = speedModifier;
  }

  public static Furnace[] getNormalVariants() {
    return new Furnace[] { IRON, GOLD, DIAMOND, EMERALD, NETHERITE };
  }

  @Override
  public Furnace[] getVariants() {
    return values();
  }

  public String getName() {
    return name().toLowerCase(Locale.ROOT);
  }

  public Component getTranslation() {
    return name;
  }

  public static Furnace value (@NotNull String tier) {
    return switch (tier.toLowerCase()) {
      case "netherite" -> NETHERITE;
      case "emerald" -> EMERALD;
      case "diamond" -> DIAMOND;
      case "gold" -> GOLD;
      default -> IRON;
    };
  }
}
