package es.degrassi.forge.core.common.component;

public enum ComponentIOMode {
  EXTRACT,
  RECEIVE,
  ALL,
  NONE;

  public boolean extract() {
    return this == EXTRACT;
  }

  public boolean receive() {
    return this == RECEIVE;
  }

  public boolean receiveWillAll() {
    return receive() || all();
  }

  public boolean extractWithAll() {
    return extract() || all();
  }

  public boolean all() {
    return this == ALL;
  }

  public boolean none() {
    return this == NONE;
  }

  public String serialize() {
    return name().toLowerCase();
  }

  public static ComponentIOMode deserialize(String value) {
    return switch (value.toLowerCase()) {
      case "receive" -> RECEIVE;
      case "extract" -> EXTRACT;
      case "none" -> NONE;
      default -> ALL;
    };
  }
}
