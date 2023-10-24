package es.degrassi.forge.integration.kubejs.recipes;

import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import es.degrassi.forge.integration.kubejs.recipes.builder.FurnaceRecipeBuilderJS;

public interface FurnaceRecipeSchema extends DegrassiRecipeSchemas {
  RecipeSchema FURNACE_MACHINE = new RecipeSchema(FurnaceRecipeBuilderJS.class, FurnaceRecipeBuilderJS::new, TIME, REQUIREMENTS);
}
