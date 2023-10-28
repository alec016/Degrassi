package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.init.recipe.IDegrassiRecipe;

public interface IRecipeEntity {
  IDegrassiRecipe getRecipe();

  void setRecipe(IDegrassiRecipe recipe);
}
