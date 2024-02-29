package es.degrassi.forge.api.impl.core.components.builder;

import es.degrassi.forge.api.impl.util.EnumButton;
import java.util.Arrays;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class BoolComponentBuilderProperty extends AbstractComponentBuilderProperty<Boolean> {
  public BoolComponentBuilderProperty(String name, Boolean defaultValue) {
    super(name, defaultValue);
  }

  @Override
  public Class<Boolean> getType() {
    return Boolean.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public AbstractWidget getAsWidget(int x, int y, int width, int height) {
    return new EnumButton<>(
      x,
      y,
      width,
      height,
      button -> this.set(((EnumButton<Boolean>) button).getValue()),
      Supplier::get,
      value -> Component.literal(value.toString()),
      Arrays.asList(true, false),
      get()
    );
  }
}
