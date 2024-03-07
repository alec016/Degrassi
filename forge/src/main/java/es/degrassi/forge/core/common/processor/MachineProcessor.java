package es.degrassi.forge.core.common.processor;

import es.degrassi.forge.api.core.common.IProcessor;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import net.minecraft.nbt.CompoundTag;

public class MachineProcessor implements IProcessor {
  private final MachineEntity entity;
  public MachineProcessor(MachineEntity entity) {
    this.entity = entity;
  }

  public MachineEntity getEntity() {
    return entity;
  }

  @Override
  public void processTick() {
//    manager.get().forEach(IRequirement::processTick);
  }

  @Override
  public void processStart() {
//    manager.get().forEach(IRequirement::processStart);
  }

  @Override
  public void processEnd() {
//    manager.get().forEach(IRequirement::processEnd);
  }

  @Override
  public CompoundTag serializeNBT() {
    return new CompoundTag();
  }

  @Override
  public void deserializeNBT(CompoundTag arg) {

  }
}
