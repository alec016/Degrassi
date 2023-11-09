package es.degrassi.forge.integration.kubejs.recipes.builder;

import dev.latvian.mods.rhino.util.HideFromJS;
import es.degrassi.forge.init.recipe.builder.MelterRecipeBuilder;
import es.degrassi.forge.init.registration.RecipeRegistry;
import es.degrassi.forge.integration.kubejs.recipes.DegrassiRecipeSchemas;
import es.degrassi.forge.integration.kubejs.requirements.EnergyRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.FluidRequirementJS;
import es.degrassi.forge.integration.kubejs.requirements.ItemRequirementJS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class MelterRecipeBuilderJS extends AbstractRecipeBuilderJS<MelterRecipeBuilder, MelterRecipeBuilderJS>
  implements ItemRequirementJS<MelterRecipeBuilderJS>, EnergyRequirementJS<MelterRecipeBuilderJS>, FluidRequirementJS<MelterRecipeBuilderJS>
{

  private final int time;

  public MelterRecipeBuilderJS() {
    this(0);
  }

  public MelterRecipeBuilderJS(int time) {
    super(RecipeRegistry.MELTER_RECIPE_TYPE.getId());
    this.time = time;
  }

  @Override
  public @Nullable Recipe<?> createRecipe() {
    if (this.removed) return null;
    if (!this.newRecipe) return getOriginalRecipe();

    MelterRecipeBuilder builder = makeBuilder();

    Arrays.stream(getValue(DegrassiRecipeSchemas.REQUIREMENTS)).forEach(builder::addRequirement);

    return builder.build(getOrCreateId());
  }

  @Override
  public MelterRecipeBuilder makeBuilder() {
    if (time < 1) return makeBuilder(getValue(DegrassiRecipeSchemas.TIME).intValue());
    return makeBuilder(time);
  }

  public MelterRecipeBuilder makeBuilder(int time) {
    return new MelterRecipeBuilder(time);
  }

  @Override
  @HideFromJS
  public MelterRecipeBuilderJS produceEnergy(int energy) {
    return this;
  }

  @Override
  @HideFromJS
  public MelterRecipeBuilderJS produceItem(ItemStack stack) {
    return this;
  }

  @Override
  @HideFromJS
  public MelterRecipeBuilderJS produceItem(@NotNull ItemStack stack, int slot) {
    return this;
  }
}
