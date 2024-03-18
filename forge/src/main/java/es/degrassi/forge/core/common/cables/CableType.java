package es.degrassi.forge.core.common.cables;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum CableType implements StringRepresentable {
  ENERGY, FLUID, ITEM, FACADE, NONE;

  CableType() {
  }

  public String getTexture() {
    return switch (this) {
      case ENERGY -> "energy/";
      case ITEM -> "item/";
      case FLUID -> "fluid/";
      default -> "";
    };
  }

  public static CableType of(String cableType) {
    return switch (cableType.toLowerCase()) {
      case "energy" -> ENERGY;
      case "fluid" -> FLUID;
      case "item" -> ITEM;
      case "facade" -> FACADE;
      default -> NONE;
    };
  }

  @Override
  public @NotNull String getSerializedName() {
    return name().toLowerCase();
  }
}
