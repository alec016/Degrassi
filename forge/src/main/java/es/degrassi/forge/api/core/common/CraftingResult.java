package es.degrassi.forge.api.core.common;

public enum CraftingResult {
  PASS,
  SUCCESS,
  ERROR;

  public boolean success() {
    return this == PASS || this == SUCCESS;
  }

  public boolean error() {
    return this == ERROR;
  }
}
