package es.degrassi.forge.core.tiers;

import es.degrassi.forge.core.init.EntityRegistration;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public enum EnergyCableTier implements StringRepresentable {
  BASIC(1_000, 100),
  ADVANCE(10_000, 1_000),
  EXTREME(1_000_000, 100_000);

  private final int capacity, transfer;
  EnergyCableTier(int capacity, int transfer) {
    this.capacity = capacity;
    this.transfer = transfer;
  }

  public int getCapacity() {
    return capacity;
  }

  public int getTransfer() {
    return transfer;
  }

  public static EnergyCableTier of(String value) {
    return switch (value.toLowerCase()) {
      case "basic" -> BASIC;
      case "advance" -> ADVANCE;
      case "extreme" -> EXTREME;
      default -> null;
    };
  }

  public String getTexture() {
    return switch (this) {
      case BASIC -> "basic_";
      case ADVANCE -> "advance_";
      case EXTREME -> "extreme_";
    };
  }

  public BlockEntityType<?> getType() {
    return switch (this) {
      case BASIC -> EntityRegistration.BASIC_ENERGY_CABLE.get();
      case ADVANCE -> EntityRegistration.ADVANCE_ENERGY_CABLE.get();
      case EXTREME -> EntityRegistration.EXTREME_ENERGY_CABLE.get();
    };
  }

  @Override
  public @NotNull String getSerializedName() {
    return name().toLowerCase();
  }
}
