package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.processor;

import es.degrassi.forge.core.common.machines.multiblock.controller.block.BaseMultiblockControllerBlock;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.BaseMultiblockControllerEntity;
import es.degrassi.forge.core.common.processor.MachineProcessor;
import es.degrassi.forge.core.common.recipe.BaseMultiblockControllerRecipe;

public abstract class MultiblockProcessor<
  R extends BaseMultiblockControllerRecipe<R>,
  E extends BaseMultiblockControllerEntity<R, ? extends BaseMultiblockControllerBlock, E>
> extends MachineProcessor<R, E> {

  public MultiblockProcessor(E entity, boolean reset) {
    super(entity, reset);
  }
}
