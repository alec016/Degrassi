package es.degrassi.forge.api.core.common;

import es.degrassi.forge.api.codec.NamedCodec;
import java.util.Locale;
import lombok.Getter;

@Getter
public enum RequirementMode {
  INPUT("input"),
  INPUT_PER_TICK(true, "input_per_tick"),
  OUTPUT("output"),
  OUTPUT_PER_TICK(true, "output_per_tick");

  public static final NamedCodec<RequirementMode> CODEC = NamedCodec.enumCodec(RequirementMode.class);

  private final boolean perTick;
  private final String string;

  RequirementMode(String string) {
    this(false, string);
  }

  RequirementMode(boolean perTick, String string) {
    this.perTick = perTick;
    this.string = string;
  }

  public boolean isOutput() {
    return this == OUTPUT || this == OUTPUT_PER_TICK;
  }

  public boolean isInput() {
    return this == INPUT || this == INPUT_PER_TICK;
  }

  public static RequirementMode value(String value) {
    return switch (value.toLowerCase(Locale.ROOT)) {
      case "input" -> INPUT;
      case "input_per_tick" -> INPUT_PER_TICK;
      case "output" -> OUTPUT;
      case "output_per_tick" -> OUTPUT_PER_TICK;
      default -> null;
    };
  }

  @Override
  public String toString() {
    return string;
  }
}
