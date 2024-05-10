package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.builder;

import es.degrassi.forge.core.common.recipe.BaseMultiblockControllerRecipe;
import es.degrassi.forge.core.common.recipe.builder.MachineBuilder;

public abstract class MultiblockRecipeBuilder<T extends BaseMultiblockControllerRecipe<T>> extends MachineBuilder<T> {
  public MultiblockRecipeBuilder(int time) {
    super(time);
  }

  public MultiblockRecipeBuilder(T recipe) {
    super(recipe);
  }
}
