package es.degrassi.forge.api.core.common;

public enum ElementDirection {
  HORIZONTAL,
  VERTICAL;

  public boolean isHorizontal() {
    return this == HORIZONTAL;
  }

  public boolean isVertical() {
    return this == VERTICAL;
  }
}
