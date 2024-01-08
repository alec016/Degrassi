package es.degrassi.forge.integration.kubejs.requirements;

import es.degrassi.forge.integration.kubejs.recipes.builder.AbstractRecipeBuilderJS;
import es.degrassi.forge.integration.kubejs.recipes.builder.RecipeBuilderJS;
import es.degrassi.forge.requirements.MachineRequirement;
import java.util.List;

public interface MachineRequirementJS<T extends AbstractRecipeBuilderJS<?, ?>> extends RecipeBuilderJS<T> {
  default T machine(String machineId) {
    return this.machines(List.of(machineId));
  }

  default T machines(List<String> machineIds) {
    return this.addRequirement(new MachineRequirement(machineIds));
  }
}
