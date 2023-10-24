package es.degrassi.forge.integration.kubejs.recipes.builder;

import es.degrassi.forge.init.recipe.builder.FurnaceRecipeBuilder;
import es.degrassi.forge.init.registration.RecipeRegistry;
import es.degrassi.forge.integration.kubejs.recipes.DegrassiRecipeSchemas;
import es.degrassi.forge.integration.kubejs.requirements.EnergyRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.ExperienceRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.ItemRequirementJS;

public class FurnaceRecipeBuilderJS extends AbstractRecipeBuilderJS<FurnaceRecipeBuilder>
  implements ItemRequirementJS, EnergyRequirementJS, ExperienceRequirementJS
{
  public FurnaceRecipeBuilderJS() {
    super(RecipeRegistry.FURNACE_RECIPE_TYPE.getId());
  }

  @Override
  public FurnaceRecipeBuilder makeBuilder() {
    return new FurnaceRecipeBuilder(getValue(DegrassiRecipeSchemas.TIME).intValue());
  }
}
