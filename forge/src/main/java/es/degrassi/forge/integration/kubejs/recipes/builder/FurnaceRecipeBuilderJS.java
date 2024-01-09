package es.degrassi.forge.integration.kubejs.recipes.builder;

import dev.latvian.mods.rhino.util.HideFromJS;
import es.degrassi.forge.init.recipe.builder.FurnaceRecipeBuilder;
import es.degrassi.forge.init.registration.RecipeRegistry;
import es.degrassi.forge.integration.kubejs.recipes.DegrassiRecipeSchemas;
import es.degrassi.forge.integration.kubejs.requirements.EnergyRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.ExperienceRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.ItemRequirementJS;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class FurnaceRecipeBuilderJS extends AbstractRecipeBuilderJS<FurnaceRecipeBuilder, FurnaceRecipeBuilderJS>
  implements ItemRequirementJS<FurnaceRecipeBuilderJS>, EnergyRequirementJS<FurnaceRecipeBuilderJS>, ExperienceRequirementJS<FurnaceRecipeBuilderJS>
{

  private final int time;
  public FurnaceRecipeBuilderJS() {
    this(0);
  }

  public FurnaceRecipeBuilderJS(int time) {
    super(RecipeRegistry.FURNACE_RECIPE_TYPE.getId());
    this.time = time;
  }

  @Override
  public @Nullable Recipe<?> createRecipe() {
    if (this.removed) return null;
    if (!this.newRecipe) return getOriginalRecipe();

    FurnaceRecipeBuilder builder = makeBuilder();

    Arrays.stream(getValue(DegrassiRecipeSchemas.REQUIREMENTS)).forEach(builder::addRequirement);

    return builder.build(getOrCreateId());
  }

  @Override
  public FurnaceRecipeBuilder makeBuilder() {
    if (time < 1) return makeBuilder(getValue(DegrassiRecipeSchemas.TIME).intValue());
    return makeBuilder(time);
  }

  public FurnaceRecipeBuilder makeBuilder(int time) {
    return new FurnaceRecipeBuilder(time);
  }

  @Override
  @HideFromJS
  public FurnaceRecipeBuilderJS produceEnergy(int energy) {
    return this;
  }
}
