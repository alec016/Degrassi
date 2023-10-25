package es.degrassi.forge.integration.kubejs.requirements;

import es.degrassi.forge.integration.kubejs.recipes.builder.RecipeBuilderJS;
import es.degrassi.forge.requirements.EnergyRequirement;
import es.degrassi.forge.requirements.IRequirement;

@SuppressWarnings("unused")
public interface EnergyRequirementJS extends RecipeBuilderJS {
  default RecipeBuilderJS requireEnergy(int energy) {
    return this.addRequirement(new EnergyRequirement(IRequirement.ModeIO.INPUT, energy));
  }
  default RecipeBuilderJS produceEnergy(int energy) {
    return this.addRequirement(new EnergyRequirement(IRequirement.ModeIO.OUTPUT, energy));
  }
}
