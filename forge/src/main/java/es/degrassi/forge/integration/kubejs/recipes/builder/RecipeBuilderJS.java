package es.degrassi.forge.integration.kubejs.recipes.builder;

import es.degrassi.forge.requirements.IRequirement;

public interface RecipeBuilderJS {

  RecipeBuilderJS addRequirement(IRequirement<?> requirement);

  RecipeBuilderJS error(String error, Object... args);
}
