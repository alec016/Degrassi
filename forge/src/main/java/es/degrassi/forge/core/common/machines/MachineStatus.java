package es.degrassi.forge.core.common.machines;

public enum MachineStatus {
  IDLE, RUNNING, ERROR;
  public static MachineStatus value(String value) {
    if (value.equalsIgnoreCase("idle")) return IDLE;
    if (value.equalsIgnoreCase("running")) return RUNNING;
    if (value.equalsIgnoreCase("error")) return ERROR;
    return null;
  }

  public boolean isError() {
    return this == ERROR;
  }
}
