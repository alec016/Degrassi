package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IRequirement;
import java.util.List;

public abstract class BaseMultiblockControllerRecipe<T extends BaseMultiblockControllerRecipe<T>> extends MachineRecipe<T> {
  public BaseMultiblockControllerRecipe(int time, List<IRequirement<?>> requirements) {
    super(time, requirements);
  }
}
