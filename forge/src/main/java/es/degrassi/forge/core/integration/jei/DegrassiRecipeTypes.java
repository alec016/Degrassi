package es.degrassi.forge.core.integration.jei;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import mezz.jei.api.recipe.RecipeType;

public class DegrassiRecipeTypes {
  public static final RecipeType<FurnaceRecipe> FURNACE = new RecipeType<>(new DegrassiLocation("furnace"), FurnaceRecipe.class);
}
