package es.degrassi.forge.integration.kubejs.recipes.builder;

import es.degrassi.forge.requirements.IRequirement;

public interface RecipeBuilderJS<T extends AbstractRecipeBuilderJS<?, ?>> {

  T addRequirement(IRequirement<?> requirement);

  T error(String error, Object... args);
}
