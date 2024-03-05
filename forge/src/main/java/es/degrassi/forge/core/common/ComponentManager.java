package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.List;

public class ComponentManager extends Manager<IComponent<?>> {
  public ComponentManager(MachineEntity entity) {
    super(entity);
  }

  public ComponentManager(List<IComponent<?>> components, MachineEntity entity) {
    super(components, entity);
  }

  public ComponentManager addEnergy(int capacity, int maxInput, int maxOutput, String id) {
    get().add(new EnergyComponent(this, capacity, maxInput, maxOutput, getEntity(), id));
    return this;
  }

  public ComponentManager addEnergy(int capacity, int transfer, String id) {
    get().add(new EnergyComponent(this, capacity, transfer, getEntity(), id));
    return this;
  }

  public ComponentManager addEnergy(int capacity, String id) {
    get().add(new EnergyComponent(this, capacity, getEntity(), id));
    return this;
  }
}
