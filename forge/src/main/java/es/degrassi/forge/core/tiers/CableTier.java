package es.degrassi.forge.core.tiers;

import es.degrassi.forge.core.common.cables.CableType;
import es.degrassi.forge.core.init.EntityRegistration;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public enum CableTier implements StringRepresentable {
  BASIC(new Energy("basic", 1_000, 100)),
  ADVANCE(new Energy("advance", 10_000, 1_000)),
  EXTREME(new Energy("extreme", 1_000_000, 100_000)),
  EMPTY;

  private final Energy energy;
  CableTier () {
    this.energy = null;
  }
  CableTier(Energy energy) {
    this.energy = energy;
  }

  public static CableTier empty() {
    return EMPTY;
  }

  public static Tier of(String tier, CableType type) {
    if (type == CableType.ENERGY) return Energy.of(tier);
    return new Tier();
  }

  public static CableTier of(String tier) {
    return switch (tier) {
      case "basic" -> BASIC;
      case "advance" -> ADVANCE;
      case "extreme" -> EXTREME;
      default -> EMPTY;
    };
  }

  public Energy energy() {
    return energy;
  }

  @Override
  public @NotNull String getSerializedName() {
    return name().toLowerCase();
  }

  public static class Tier {
    public Tier() {
    }
    public String getTexture() {
      return "";
    }
    public BlockEntityType<?> getType() {
      return null;
    }
  }

  public static class Energy extends Tier {
    private final String tier;
    private final int capacity, transfer;
    public Energy(String tier, int capacity, int transfer) {
      super();
      this.tier = tier;
      this.capacity = capacity;
      this.transfer = transfer;
    }

    public int getCapacity() {
      return capacity;
    }

    public int getTransfer() {
      return transfer;
    }

    public String getTexture() {
      return tier + "_";
    }

    public static Energy of(String tier) {
      return switch (tier) {
        case "basic" -> BASIC.energy;
        case "advance" -> ADVANCE.energy;
        case "extreme" -> EXTREME.energy;
        default -> null;
      };
    }

    public BlockEntityType<?> getType() {
      return switch (tier) {
        case "basic" -> EntityRegistration.BASIC_ENERGY_CABLE.get();
        case "advance" -> EntityRegistration.ADVANCE_ENERGY_CABLE.get();
        case "extreme" -> EntityRegistration.EXTREME_ENERGY_CABLE.get();
        default -> null;
      };
    }
  }
}
