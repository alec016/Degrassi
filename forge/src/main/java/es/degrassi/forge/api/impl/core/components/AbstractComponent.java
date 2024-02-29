package es.degrassi.forge.api.impl.core.components;

import es.degrassi.forge.api.core.component.ComponentIOMode;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.api.core.component.IComponentManager;

public abstract class AbstractComponent implements IComponent {

  private final IComponentManager manager;
  private final ComponentIOMode mode;

  public AbstractComponent(IComponentManager manager, ComponentIOMode mode) {
    this.manager = manager;
    this.mode = mode;
  }

  @Override
  public ComponentIOMode getMode() {
    return this.mode;
  }

  @Override
  public IComponentManager getManager() {
    return this.manager;
  }
}
