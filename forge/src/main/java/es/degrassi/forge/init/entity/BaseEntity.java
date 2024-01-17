package es.degrassi.forge.init.entity;

import es.degrassi.forge.init.entity.type.IDegrassiEntity;
import es.degrassi.forge.init.gui.component.*;
import es.degrassi.forge.init.gui.element.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseEntity extends BlockEntity implements IDegrassiEntity {
  private final ComponentManager componentManager;
  private final ElementManager elementManager;

  public BaseEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
    super(blockEntityType, blockPos, blockState);
    this.componentManager = new ComponentManager(this);
    this.elementManager = new ElementManager(this);
  }

  public ComponentManager getComponentManager() {
    return componentManager;
  }

  public ElementManager getElementManager() {
    return elementManager;
  }
}
