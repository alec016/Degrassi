package es.degrassi.forge.core.common.component;

public enum ComponentIOMode {
  OUTPUT,
  INPUT,
  BOTH,
  NONE;

  public boolean output() {
    return this == OUTPUT;
  }

  public boolean input() {
    return this == INPUT;
  }

  public boolean inputWillAll() {
    return input() || all();
  }

  public boolean outputWithAll() {
    return output() || all();
  }

  public boolean all() {
    return this == BOTH;
  }

  public boolean none() {
    return this == NONE;
  }

  public String serialize() {
    return name().toLowerCase();
  }

  public static ComponentIOMode deserialize(String value) {
    return switch (value.toLowerCase()) {
      case "input" -> INPUT;
      case "output" -> OUTPUT;
      case "none" -> NONE;
      default -> BOTH;
    };
  }
}
