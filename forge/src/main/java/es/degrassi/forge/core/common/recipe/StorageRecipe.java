package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IRequirement;
import java.util.List;

public abstract class StorageRecipe extends MachineRecipe<StorageRecipe> {
  public StorageRecipe(int time , List<IRequirement<?>> requirements) {
    super(time , requirements);
  }
}
