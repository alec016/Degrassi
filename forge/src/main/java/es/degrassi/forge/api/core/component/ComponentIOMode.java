package es.degrassi.forge.api.core.component;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.impl.core.components.config.SideConfig;
import java.util.Locale;

public enum ComponentIOMode {

  INPUT(true, false, SideConfig.Template.DEFAULT_ALL_INPUT),
  OUTPUT(false, true, SideConfig.Template.DEFAULT_ALL_OUTPUT),
  BOTH(true, true, SideConfig.Template.DEFAULT_ALL_BOTH),
  NONE(false, false, SideConfig.Template.DEFAULT_ALL_NONE);

  public static final NamedCodec<ComponentIOMode> CODEC = NamedCodec.enumCodec(ComponentIOMode.class);
  private final boolean isInput;
  private final boolean isOutput;
  private final SideConfig.Template baseConfig;

  ComponentIOMode(boolean isInput, boolean isOutput, SideConfig.Template baseConfig) {
    this.isInput = isInput;
    this.isOutput = isOutput;
    this.baseConfig = baseConfig;
  }

  public boolean isInput() {
    return this.isInput;
  }

  public boolean isOutput() {
    return this.isOutput;
  }

  public SideConfig.Template getBaseConfig() {
    return this.baseConfig;
  }

  public static ComponentIOMode value (String value) {
    return valueOf(value.toUpperCase(Locale.ENGLISH));
  }

  public String toString () {
    return super.toString().toLowerCase(Locale.ENGLISH);
  }
}
