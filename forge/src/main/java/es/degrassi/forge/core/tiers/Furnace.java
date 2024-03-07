package es.degrassi.forge.core.tiers;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;

public enum Furnace {
  IRON(
    10_000,
    1_000,
    1,
    1,
    EntityRegistration.IRON_FURNACE,
    Component.translatable("block.degrassi.iron_furnace")
  ),
  GOLD(
    50_000,
    5_000,
    1.5,
    0.75,
    EntityRegistration.GOLD_FURNACE,
    Component.translatable("block.degrassi.gold_furnace")
  ),
  DIAMOND(
    100_000,
    10_000,
    2.5,
    0.55,
    EntityRegistration.DIAMOND_FURNACE,
    Component.translatable("block.degrassi.diamond_furnace")
  ),
  EMERALD(
    500_000,
    50_000,
    3.75,
    0.3,
    EntityRegistration.EMERALD_FURNACE,
    Component.translatable("block.degrassi.emerald_furnace")
  ),
  NETHERITE(
    1_000_000,
    100_000,
    4.5,
    0.1,
    EntityRegistration.NETHERITE_FURNACE,
    Component.translatable("block.degrassi.netherite_furnace")
  );

  private final int energyCapacity;
  private final float experienceCapacity;
  private final RegistrySupplier<BlockEntityType<FurnaceEntity>> type;
  private final Component name;
  private final double recipeModifier, speedModifier;

  Furnace(int energyCapacity, float experienceCapacity, double recipeModifier, double speedModifier, RegistrySupplier<BlockEntityType<FurnaceEntity>> type, Component name) {
    this.energyCapacity = energyCapacity;
    this.experienceCapacity = experienceCapacity;
    this.type = type;
    this.name = name;
    this.recipeModifier = recipeModifier;
    this.speedModifier = speedModifier;
  }

  public int getEnergyCapacity() {
    return energyCapacity;
  }
  public float getExperienceCapacity() {
    return experienceCapacity;
  }

  public RegistrySupplier<BlockEntityType<FurnaceEntity>> getType() {
    return type;
  }

  public Component getName() {
    return name;
  }

  public double getRecipeModifier () {
    return recipeModifier;
  }

  public double getSpeedModifier() {
    return speedModifier;
  }

  public static Furnace value (String tier) {
    if (tier.equalsIgnoreCase("iron")) return IRON;
    if (tier.equalsIgnoreCase("gold")) return GOLD;
    if (tier.equalsIgnoreCase("diamond")) return DIAMOND;
    if (tier.equalsIgnoreCase("emerald")) return EMERALD;
    if (tier.equalsIgnoreCase("netherite")) return NETHERITE;
    return null;
  }
}
