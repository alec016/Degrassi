package es.degrassi.forge.api.impl.core.components.builder;

import es.degrassi.forge.api.core.component.builder.IComponentBuilderProperty;

public abstract class AbstractComponentBuilderProperty<T> implements IComponentBuilderProperty<T> {

  private String name;
  private T property;

  public AbstractComponentBuilderProperty(String name, T defaultValue) {
    this.name = name;
    this.property = defaultValue;
  }

  @Override
  public T get() {
    return property;
  }

  @Override
  public void set(T property) {
    this.property = property;
  }

  @Override
  public String getName() {
    return name;
  }
}
