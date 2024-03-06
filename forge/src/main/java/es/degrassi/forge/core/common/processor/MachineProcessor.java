package es.degrassi.forge.core.common.processor;

import es.degrassi.forge.api.core.common.IProcessor;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import net.minecraft.nbt.CompoundTag;

public class MachineProcessor implements IProcessor {
  private final RequirementManager manager;
  private final MachineEntity entity;
  public MachineProcessor(RequirementManager manager, MachineEntity entity) {
    this.manager = manager;
    this.entity = entity;
  }

  public MachineEntity getEntity() {
    return entity;
  }

  public RequirementManager getManager() {
    return manager;
  }

  @Override
  public void processTick() {

  }

  @Override
  public void processStart() {

  }

  @Override
  public void processEnd() {

  }

  @Override
  public CompoundTag serializeNBT() {
    return new CompoundTag();
  }

  @Override
  public void deserializeNBT(CompoundTag arg) {

  }
}
