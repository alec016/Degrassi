package es.degrassi.forge.core.data.recipe.builder;

import es.degrassi.forge.core.data.recipe.MachineRecipeGeneratorBuilder;
import es.degrassi.forge.core.data.recipe.result.MelterResult;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MelterBuilder extends MachineRecipeGeneratorBuilder {
  public MelterBuilder(int time) {
    super(time);
  }

  @Override
  public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
    finishedRecipeConsumer.accept(
      new MelterResult(
        recipeId.withPrefix("melter/"),
        this,
        this.time,
        this.advancement,
        recipeId.withPrefix("recipes/melter/")
      )
    );
  }
}
