package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IRequirement;
import java.util.List;

public abstract class ChestRecipe extends MachineRecipe<ChestRecipe> {
  public ChestRecipe(int time, List<IRequirement<?>> requirements) {
    super(time, requirements);
  }
}
