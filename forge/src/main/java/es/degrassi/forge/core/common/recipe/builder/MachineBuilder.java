package es.degrassi.forge.core.common.recipe.builder;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public abstract class MachineBuilder<T extends MachineRecipe> {
  private List<IRequirement<?>> requirements = new ArrayList<>();
  private final int time;

  public MachineBuilder(int time) {
    this.time = time;
  }

  public MachineBuilder(T recipe) {
    this(recipe.getTime());
    this.requirements = recipe.getRequirements();
  }

  public MachineBuilder<T> withRequirement(IRequirement<?> requirement) {
    this.requirements.add(requirement);
    return this;
  }

  public int getTime() {
    return time;
  }

  public List<IRequirement<?>> getRequirements() {
    return requirements;
  }

  public abstract T build(ResourceLocation id);
}
