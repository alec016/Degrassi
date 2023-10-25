package es.degrassi.forge.init.recipe.helpers;

import es.degrassi.forge.init.entity.melter.MelterEntity;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
import org.jetbrains.annotations.NotNull;

public class MelterRecipeHelper extends RecipeHelper<MelterRecipe, MelterEntity> {
  @Override
  public boolean hasRecipe(@NotNull MelterEntity entity) {
    return false;
  }

  @Override
  public void craftItem(@NotNull MelterEntity entity) {

  }

  @Override
  public void init() {

  }
}
