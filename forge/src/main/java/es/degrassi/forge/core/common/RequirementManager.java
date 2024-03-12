package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
import es.degrassi.forge.core.common.requirement.ExperienceRequirement;
import es.degrassi.forge.core.common.requirement.ItemRequirement;
import java.util.List;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public final class RequirementManager extends Manager<IRequirement<?>> {
  public RequirementManager (MachineEntity<?> entity) {
    super(entity);
  }

  public RequirementManager() {
    super(null);
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

  private RequirementManager addExperience(float amount, RequirementMode mode, String id) {
    get().add(new ExperienceRequirement(amount, mode, id));
    return this;
  }

  public RequirementManager requireExperience(float amount, String id) {
    return addExperience(amount, RequirementMode.INPUT, id);
  }

  public RequirementManager requireExperiencePerTick(float amount, String id) {
    return addExperience(amount, RequirementMode.INPUT_PER_TICK, id);
  }

  public RequirementManager produceExperience(float amount, String id) {
    return addExperience(amount, RequirementMode.OUTPUT, id);
  }

  public RequirementManager produceExperiencePerTick(float amount, String id) {
    return addExperience(amount, RequirementMode.OUTPUT_PER_TICK, id);
  }

  private RequirementManager addItem(Item item, int amount, String id, RequirementMode mode) {
    get().add(new ItemRequirement(item, amount, id, mode));
    return this;
  }

  public RequirementManager requireItem(Item item, int amount, String id) {
    return addItem(item, amount, id, RequirementMode.INPUT);
  }

  public RequirementManager requireItem(Item item, String id) {
    return requireItem(item , 1, id);
  }

  public RequirementManager produceItem(Item item, int amount, String id) {
    return addItem(item, amount, id, RequirementMode.OUTPUT);
  }

  public RequirementManager produceItem(Item item, String id) {
    return produceItem(item, 1, id);
  }

  @Override
  public String toString() {
    return "Requirement" + super.toString();
  }
}
