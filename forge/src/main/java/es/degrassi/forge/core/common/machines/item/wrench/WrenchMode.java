package es.degrassi.forge.core.common.machines.item.wrench;

public enum WrenchMode {
  CONFIG,
  ROTATE;

  public boolean config() {
    return this == CONFIG;
  }

  public boolean rotate() {
    return this == ROTATE;
  }
}
