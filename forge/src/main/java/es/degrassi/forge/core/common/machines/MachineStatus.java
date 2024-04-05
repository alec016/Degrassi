package es.degrassi.forge.core.common.machines;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MachineStatus implements StringRepresentable {
  IDLE, RUNNING, ERROR;
  public static @Nullable MachineStatus value(@NotNull String value) {
    if (value.equalsIgnoreCase("idle")) return IDLE;
    if (value.equalsIgnoreCase("running")) return RUNNING;
    if (value.equalsIgnoreCase("error")) return ERROR;
    return null;
  }

  public boolean isError() {
    return this == ERROR;
  }

  @Override
  public String getSerializedName() {
    return name().toLowerCase();
  }
}
