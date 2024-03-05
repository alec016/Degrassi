package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.RequirementManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineEntity extends BlockEntity {
  private ComponentManager componentManager;
  private RequirementManager requirementManager;
  private ElementManager elementManager;

  public MachineEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
    super(type, pos, blockState);
    this.componentManager = new ComponentManager(this);
    this.elementManager = new ElementManager(this);
    this.requirementManager = new RequirementManager(this);
  }

  public ComponentManager getComponentManager() {
    return componentManager;
  }

  public ElementManager getElementManager() {
    return elementManager;
  }

  public RequirementManager getRequirementManager() {
    return requirementManager;
  }

  public abstract Component getName();
}