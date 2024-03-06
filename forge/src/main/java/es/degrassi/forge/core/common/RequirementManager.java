package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.List;

public class RequirementManager extends Manager<IRequirement<?>> {
  public RequirementManager (MachineEntity entity) {
    super(entity);
  }

  public RequirementManager(List<IRequirement<?>> requirements, MachineEntity entity) {
    super(requirements, entity);
  }

  @Override
  public String toString() {
    return "Requirement" + super.toString();
  }
}
