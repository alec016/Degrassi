package es.degrassi.forge.api.impl.core.components.builder;

import es.degrassi.forge.api.core.component.ComponentIOMode;
import es.degrassi.forge.api.impl.util.EnumButton;
import java.util.Arrays;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class ModeComponentBuilderProperty extends AbstractComponentBuilderProperty<ComponentIOMode> {
  public ModeComponentBuilderProperty(String name, ComponentIOMode defaultValue) {
    super(name, defaultValue);
  }

  @Override
  public Class<ComponentIOMode> getType() {
    return ComponentIOMode.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public AbstractWidget getAsWidget(int x, int y, int width, int height) {
    return new EnumButton<>(
      x,
      y,
      width,
      height,
      button -> this.set(((EnumButton<ComponentIOMode>)button).getValue()),
      Supplier::get,
      mode -> Component.literal(mode.toString()),
      Arrays.asList(ComponentIOMode.values()),
      get()
    );
  }
}
