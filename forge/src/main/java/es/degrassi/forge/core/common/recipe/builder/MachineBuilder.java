package es.degrassi.forge.core.common.recipe.builder;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public abstract class MachineBuilder<T extends MachineRecipe<T>> extends RequirementManager {
  private final int time;

  public MachineBuilder(int time) {
    this.time = time;
  }

  public MachineBuilder(T recipe) {
    this(recipe.getTime());
    get().addAll(recipe.getRequirements());
  }

  public int getTime() {
    return time;
  }

  public List<IRequirement<?>> getRequirements() {
    return get();
  }

  public abstract T build(ResourceLocation id);
}
