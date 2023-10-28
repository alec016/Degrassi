package es.degrassi.forge.integration.kubejs.recipes.builder;

import es.degrassi.forge.init.recipe.builder.MelterRecipeBuilder;
import es.degrassi.forge.init.registration.RecipeRegistry;
import es.degrassi.forge.integration.kubejs.recipes.DegrassiRecipeSchemas;
import es.degrassi.forge.integration.kubejs.requirements.EnergyRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.FluidRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.ItemRequirementJS;

public class MelterRecipeBuilderJS extends AbstractRecipeBuilderJS<MelterRecipeBuilder>
  implements ItemRequirementJS, EnergyRequirementJS, FluidRequirementJS
{

  public MelterRecipeBuilderJS() {
    super(RecipeRegistry.MELTER_RECIPE_TYPE.getId());
  }

  @Override
  public MelterRecipeBuilder makeBuilder() {
    return new MelterRecipeBuilder(getValue(DegrassiRecipeSchemas.TIME).intValue());
  }
}
