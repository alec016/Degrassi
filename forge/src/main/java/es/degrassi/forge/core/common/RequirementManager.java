package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
import java.util.List;

public class RequirementManager extends Manager<IRequirement<?>> {
  public RequirementManager (MachineEntity entity) {
    super(entity);
  }

  public RequirementManager(List<IRequirement<?>> requirements, MachineEntity entity) {
    super(requirements, entity);
  }

  private RequirementManager addEnergy(int amount, RequirementMode mode) {
    get().add(new EnergyRequirement(amount, mode));
    return this;
  }

  public RequirementManager requireEnergy(int amount) {
    return addEnergy(amount, RequirementMode.INPUT);
  }

  public RequirementManager requireEnergyPerTick(int amount) {
    return addEnergy(amount, RequirementMode.INPUT_PER_TICK);
  }

  public RequirementManager produceEnergy(int amount) {
    return addEnergy(amount, RequirementMode.OUTPUT);
  }

  public RequirementManager produceEnergyPerTick(int amount) {
    return addEnergy(amount, RequirementMode.OUTPUT_PER_TICK);
  }

  @Override
  public String toString() {
    return "Requirement" + super.toString();
  }
}
