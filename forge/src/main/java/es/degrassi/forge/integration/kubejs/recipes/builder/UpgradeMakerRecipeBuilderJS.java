package es.degrassi.forge.integration.kubejs.recipes.builder;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import es.degrassi.forge.init.recipe.builder.UpgradeMakerRecipeBuilder;
import es.degrassi.forge.init.registration.RecipeRegistry;
import es.degrassi.forge.integration.kubejs.recipes.DegrassiRecipeSchemas;
import es.degrassi.forge.integration.kubejs.requirements.EnergyRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.FluidRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.ItemRequirementJS;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class UpgradeMakerRecipeBuilderJS extends AbstractRecipeBuilderJS<UpgradeMakerRecipeBuilder, UpgradeMakerRecipeBuilderJS>
  implements ItemRequirementJS<UpgradeMakerRecipeBuilderJS>, EnergyRequirementJS<UpgradeMakerRecipeBuilderJS>, FluidRequirementJS<UpgradeMakerRecipeBuilderJS>
{
  private final int time;

  public UpgradeMakerRecipeBuilderJS() {
    this(0);
  }

  public UpgradeMakerRecipeBuilderJS(int time) {
    super(RecipeRegistry.UPGRADE_MAKER_RECIPE_TYPE.getId());
    this.time = time;
  }

  @Override
  public @Nullable Recipe<?> createRecipe() {
    if (this.removed) return null;
    if (!this.newRecipe) return getOriginalRecipe();

    UpgradeMakerRecipeBuilder builder = makeBuilder();

    Arrays.stream(getValue(DegrassiRecipeSchemas.REQUIREMENTS)).forEach(builder::addRequirement);

    return builder.build(getOrCreateId());
  }

  @Override
  public UpgradeMakerRecipeBuilder makeBuilder() {
    if (time < 1) return makeBuilder(getValue(DegrassiRecipeSchemas.TIME).intValue());
    return makeBuilder(time);
  }

  public UpgradeMakerRecipeBuilder makeBuilder(int time) {
    return new UpgradeMakerRecipeBuilder(time);
  }

  @Override
  @HideFromJS
  public UpgradeMakerRecipeBuilderJS produceEnergy(int energy) {
    return this;
  }

  @Override
  @HideFromJS
  public UpgradeMakerRecipeBuilderJS produceFluid(@NotNull FluidStackJS stack) {
    return this;
  }
}
