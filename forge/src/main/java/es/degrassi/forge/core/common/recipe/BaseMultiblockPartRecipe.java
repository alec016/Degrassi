package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IRequirement;
import java.util.List;

public abstract class BaseMultiblockPartRecipe extends MachineRecipe<BaseMultiblockPartRecipe> {
  public BaseMultiblockPartRecipe(int time, List<IRequirement<?>> requirements) {
    super(time, requirements);
  }
}
