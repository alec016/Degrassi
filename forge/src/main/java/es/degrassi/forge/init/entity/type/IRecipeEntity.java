package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.init.recipe.IDegrassiRecipe;

public interface IRecipeEntity<R extends IDegrassiRecipe> extends IDegrassiEntity {
  R getRecipe();

  void setRecipe(R recipe);
}
