package es.degrassi.forge.integration.kubejs.requirements;

import es.degrassi.forge.integration.kubejs.recipes.builder.AbstractRecipeBuilderJS;
import es.degrassi.forge.integration.kubejs.recipes.builder.RecipeBuilderJS;
import es.degrassi.forge.requirements.EnergyRequirement;
import es.degrassi.forge.requirements.IRequirement;

@SuppressWarnings("unused")
public interface EnergyRequirementJS<T extends AbstractRecipeBuilderJS<?, ?>> extends RecipeBuilderJS<T> {
  default T requireEnergy(int energy) {
    return this.addRequirement(new EnergyRequirement(IRequirement.ModeIO.INPUT, energy));
  }
  default T produceEnergy(int energy) {
    return this.addRequirement(new EnergyRequirement(IRequirement.ModeIO.OUTPUT, energy));
  }
}
