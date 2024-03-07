package es.degrassi.forge.api.core.common;

import es.degrassi.forge.api.codec.NamedCodec;

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

  public boolean isPerTick() {
    return perTick;
  }

  public boolean isOutput() {
    return this == OUTPUT || this == OUTPUT_PER_TICK;
  }

  public boolean isInput() {
    return this == INPUT || this == INPUT_PER_TICK;
  }

  public static RequirementMode value(String value) {
    if (value.equalsIgnoreCase("input")) return INPUT;
    if (value.equalsIgnoreCase("input_per_tick")) return INPUT_PER_TICK;
    if (value.equalsIgnoreCase("output")) return OUTPUT;
    if (value.equalsIgnoreCase("output_per_tick")) return OUTPUT_PER_TICK;
    return null;
  }

  @Override
  public String toString() {
    return string;
  }
}
