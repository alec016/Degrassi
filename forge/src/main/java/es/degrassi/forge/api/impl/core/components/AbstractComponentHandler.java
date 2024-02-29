package es.degrassi.forge.api.impl.core.components;

import es.degrassi.forge.api.core.component.ComponentIOMode;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.api.core.component.IComponentManager;
import es.degrassi.forge.api.core.component.handler.IComponentHandler;
import java.util.List;

public abstract class AbstractComponentHandler<T extends IComponent> implements IComponentHandler<T> {

  private final IComponentManager manager;
  private final List<T> components;

  public AbstractComponentHandler(IComponentManager manager, List<T> components) {
    this.manager = manager;
    this.components = components;
  }

  public IComponentManager getManager() {
    return this.manager;
  }

  @Override
  public ComponentIOMode getMode() {
    return ComponentIOMode.NONE;
  }

  @Override
  public List<T> getComponents() {
    return this.components;
  }
}
