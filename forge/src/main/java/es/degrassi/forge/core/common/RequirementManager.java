package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
import java.util.List;

public final class RequirementManager extends Manager<IRequirement<?>> {
  public RequirementManager (MachineEntity<?> entity) {
    super(entity);
  }

  public RequirementManager(List<IRequirement<?>> requirements, MachineEntity<?> entity) {
    super(requirements, entity);
  }

  private RequirementManager addEnergy(int amount, RequirementMode mode, String id) {
    get().add(new EnergyRequirement(amount, mode, id));
    return this;
  }

  public RequirementManager requireEnergy(int amount, String id) {
    return addEnergy(amount, RequirementMode.INPUT, id);
  }

  public RequirementManager requireEnergyPerTick(int amount, String id) {
    return addEnergy(amount, RequirementMode.INPUT_PER_TICK, id);
  }

  public RequirementManager produceEnergy(int amount, String id) {
    return addEnergy(amount, RequirementMode.OUTPUT, id);
  }

  public RequirementManager produceEnergyPerTick(int amount, String id) {
    return addEnergy(amount, RequirementMode.OUTPUT_PER_TICK, id);
  }

  @Override
  public String toString() {
    return "Requirement" + super.toString();
  }
}
