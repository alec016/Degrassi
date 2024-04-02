package es.degrassi.forge.core.common.machines;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MachineStatus {
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
}
