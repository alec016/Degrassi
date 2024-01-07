package es.degrassi.forge.integration.kubejs.recipes.builder;

import dev.latvian.mods.kubejs.fluid.*;
import dev.latvian.mods.rhino.util.*;
import es.degrassi.forge.init.recipe.builder.*;
import es.degrassi.forge.init.registration.*;
import es.degrassi.forge.integration.kubejs.recipes.*;
import es.degrassi.forge.integration.kubejs.requirements.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public class GeneratorRecipeBuilderJS extends AbstractRecipeBuilderJS<GeneratorRecipeBuilder, GeneratorRecipeBuilderJS>
  implements ItemRequirementJS<GeneratorRecipeBuilderJS>, EnergyRequirementJS<GeneratorRecipeBuilderJS>,
  FluidRequirementJS<GeneratorRecipeBuilderJS>
{

  private final int time;

  public GeneratorRecipeBuilderJS() {
    this(0);
  }

  public GeneratorRecipeBuilderJS(int time) {
    super(RecipeRegistry.GENERATOR_RECIPE_TYPE.getId());
    this.time = time;
  }

  @Override
  public GeneratorRecipeBuilder makeBuilder() {
    if (time < 1) return makeBuilder(getValue(DegrassiRecipeSchemas.TIME).intValue());
    return makeBuilder(time);
  }

  public GeneratorRecipeBuilder makeBuilder(int time) {
    return new GeneratorRecipeBuilder(time);
  }

  @HideFromJS
  public GeneratorRecipeBuilderJS requireEnergy(int energy) {
    return this;
  }

  @HideFromJS
  public GeneratorRecipeBuilderJS produceFluid(@NotNull FluidStackJS stack) {
    return this;
  }

  @HideFromJS
  public GeneratorRecipeBuilderJS produceItem(ItemStack stack) {
    return this;
  }

  @HideFromJS
  public GeneratorRecipeBuilderJS produceItem(@NotNull ItemStack stack, int slot) {
    return this;
  }
}
