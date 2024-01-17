package es.degrassi.forge.api.gui;

import es.degrassi.forge.init.gui.component.IComponent;
import java.util.List;

public interface IComponentManager extends IManager {
  List<IComponent> getComponents();

  void addComponent(IComponent component);
}
