package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.List;

public class ElementManager extends Manager<IElement<?>> {
  public ElementManager(MachineEntity entity) {
    super(entity);
  }

  public ElementManager(List<IElement<?>> elements, MachineEntity entity) {
    super(elements, entity);
  }
}
