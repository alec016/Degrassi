package es.degrassi.forge.api.core.element;

import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.api.core.component.IComponentManager;
import es.degrassi.forge.api.core.component.handler.IComponentHandler;
import java.util.Optional;

public interface IComponentGuiElement<T extends IComponent> {
  ComponentType<T> getComponentType();
  String getID();

  @SuppressWarnings({"unchecked"})
  default Optional<T> getComponent(IComponentManager manager) {
    return manager.getComponent(getComponentType()).flatMap(component -> {
      if(component instanceof IComponentHandler handler)
        return handler.getComponentForID(getID());
      return Optional.of(component);
    });
  }
}
