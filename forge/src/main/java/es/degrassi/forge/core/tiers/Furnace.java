package es.degrassi.forge.core.tiers;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;

public enum Furnace {
  IRON(10_000, EntityRegistration.IRON_FURNACE, Component.translatable("block.degrassi.iron_furnace")),
  GOLD(50_000, EntityRegistration.GOLD_FURNACE, Component.translatable("block.degrassi.gold_furnace")),
  DIAMOND(100_000, EntityRegistration.DIAMOND_FURNACE, Component.translatable("block.degrassi.diamond_furnace")),
  EMERALD(500_000, EntityRegistration.EMERALD_FURNACE, Component.translatable("block.degrassi.emerald_furnace")),
  NETHERITE(1_000_000, EntityRegistration.NETHERITE_FURNACE, Component.translatable("block.degrassi.netherite_furnace"));

  private final int capacity;
  private final RegistrySupplier<BlockEntityType<FurnaceEntity>> type;
  private final Component name;

  Furnace(int capacity, RegistrySupplier<BlockEntityType<FurnaceEntity>> type, Component name) {
    this.capacity = capacity;
    this.type = type;
    this.name = name;
  }

  public int getCapacity() {
    return capacity;
  }

  public RegistrySupplier<BlockEntityType<FurnaceEntity>> getType() {
    return type;
  }

  public Component getName() {
    return name;
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
