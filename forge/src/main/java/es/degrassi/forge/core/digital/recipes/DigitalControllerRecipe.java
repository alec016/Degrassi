package es.degrassi.forge.core.digital.recipes;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;

public abstract class DigitalControllerRecipe extends MachineRecipe<DigitalControllerRecipe> {
  public DigitalControllerRecipe(int time, List<IRequirement<?>> requirements) {
    super(time, requirements);
  }
}
