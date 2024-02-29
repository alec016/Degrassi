package es.degrassi.forge.api.core.component.builder;

import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.api.core.component.IComponentTemplate;
import java.util.List;

public interface IComponentBuilder<T extends IComponent> {
  IComponentBuilder<T> fromComponent(IComponent component);
  ComponentType<T> getType();
  List<IComponentBuilderProperty<?>> getProperties();
  IComponentTemplate<T> build();
}
