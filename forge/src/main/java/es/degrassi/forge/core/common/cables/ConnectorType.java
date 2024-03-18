package es.degrassi.forge.core.common.cables;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ConnectorType implements StringRepresentable {
  NONE, CABLE, BLOCK;

  public static final ConnectorType[] VALUES = values();

  @Override
  public @NotNull String getSerializedName() {
    return name().toLowerCase();
  }
}
