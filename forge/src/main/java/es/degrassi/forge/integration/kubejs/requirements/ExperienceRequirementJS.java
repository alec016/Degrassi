package es.degrassi.forge.integration.kubejs.requirements;

import es.degrassi.forge.integration.kubejs.recipes.builder.AbstractRecipeBuilderJS;
import es.degrassi.forge.integration.kubejs.recipes.builder.RecipeBuilderJS;
import es.degrassi.forge.requirements.ExperienceRequirement;

@SuppressWarnings("unused")
public interface ExperienceRequirementJS<T extends AbstractRecipeBuilderJS<?, ?>> extends RecipeBuilderJS<T> {
  default T produceXp(int amount) {
    return this.addRequirement(new ExperienceRequirement(amount));
  }
}
