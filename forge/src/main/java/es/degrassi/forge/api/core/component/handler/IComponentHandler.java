package es.degrassi.forge.api.core.component.handler;

import es.degrassi.forge.api.core.component.IComponent;
import java.util.List;
import java.util.Optional;

public interface IComponentHandler<T extends IComponent> extends IComponent {
  List<T> getComponents();
  Optional<T> getComponentForID(String id);
}
