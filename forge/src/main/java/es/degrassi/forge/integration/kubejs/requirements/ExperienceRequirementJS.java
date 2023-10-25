package es.degrassi.forge.integration.kubejs.requirements;

import es.degrassi.forge.integration.kubejs.recipes.builder.RecipeBuilderJS;
import es.degrassi.forge.requirements.ExperienceRequirement;

@SuppressWarnings("unused")
public interface ExperienceRequirementJS extends RecipeBuilderJS {
  default RecipeBuilderJS produceXp(float amount) {
    return this.addRequirement(new ExperienceRequirement(amount));
  }
}
