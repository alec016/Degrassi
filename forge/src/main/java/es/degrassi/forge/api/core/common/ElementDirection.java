package es.degrassi.forge.api.core.common;

public enum ElementDirection {
  LEFT,
  RIGHT,
  TOP,
  BOTTOM;

  public boolean left() {
    return this == LEFT;
  }

  public boolean right() {
    return this == RIGHT;
  }

  public boolean top() {
    return this == TOP;
  }

  public boolean bottom() {
    return this == BOTTOM;
  }

  public ElementDirection opposite() {
    return switch (this) {
      case TOP -> BOTTOM;
      case BOTTOM -> TOP;
      case LEFT -> RIGHT;
      case RIGHT -> LEFT;
    };
  }
}
