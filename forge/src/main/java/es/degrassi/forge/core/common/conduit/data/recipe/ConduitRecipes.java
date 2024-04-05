package es.degrassi.forge.core.common.conduit.data.recipe;

import java.util.function.Consumer;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

public class ConduitRecipes extends RecipeProvider {
  public ConduitRecipes(PackOutput output) {
    super(output);
  }

  @Override
  public void buildRecipes(Consumer<FinishedRecipe> writer) {

  }
}
