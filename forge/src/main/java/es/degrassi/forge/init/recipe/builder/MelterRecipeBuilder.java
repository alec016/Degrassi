package es.degrassi.forge.init.recipe.builder;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.ingredient.IIngredient;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
import es.degrassi.forge.requirements.*;
import es.degrassi.forge.util.DegrassiLogger;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class MelterRecipeBuilder extends AbstractRecipeBuilder<MelterRecipe> {
  private int time;
  private int energy;
  private NonNullList<Ingredient> ingredients;
  private FluidStack output;
  private IIngredient<Fluid> outputIngredient;
  private IIngredient<Item> inputIngredient;
  private int inputAmount;
  private int outputAmount;

  public static final NamedCodec<MelterRecipeBuilder> CODEC = NamedCodec.record(recipeBuilderInstance ->
    recipeBuilderInstance.group(
      NamedCodec.INT.fieldOf("time").forGetter(builder -> builder.time),
      NamedCodec.INT.fieldOf("energy").forGetter(builder -> builder.energy),
      IIngredient.ITEM.fieldOf("input").forGetter(builder -> builder.inputIngredient),
      NamedCodec.INT.optionalFieldOf("inputAmount", 1).forGetter(builder -> builder.inputAmount),
      IIngredient.FLUID.fieldOf("output").forGetter(builder -> builder.outputIngredient),
      NamedCodec.INT.optionalFieldOf("outputAmount", 1000).forGetter(builder -> builder.outputAmount)
    ).apply(recipeBuilderInstance, (time, energy, input, inputAmount, output, outputAmount) -> {
      DegrassiLogger.INSTANCE.info("MelterRecipeBuilderCODED[ time: " + time + ", energy: " + energy + ", input: " + input + ", amount: " + inputAmount + ", output: " + output + ", amount: " + outputAmount + " ]");
      return new MelterRecipeBuilder(time)
        .energy(energy)
        .input(new ItemStack(input.getAll().get(0), inputAmount))
        .output(new FluidStack(output.getAll().get(0), outputAmount));
    }), "Melter recipe builder"
  );

  public MelterRecipeBuilder(int time) {
    super();
    this.time = time;
  }

  public MelterRecipeBuilder(MelterRecipe recipe) {
    super(recipe);
    this.time = recipe.getTime();
  }

  @Override
  public MelterRecipe build(ResourceLocation id) {
    MelterRecipe recipe = new MelterRecipe(id, time, energy, ingredients, output);
    DegrassiLogger.INSTANCE.info("MelterRecipeBuilder$build: " + recipe);
    return recipe;
  }

  public MelterRecipeBuilder energy(int energy) {
    this.energy = energy;
    return this;
  }

  public MelterRecipeBuilder inputIngredient(IIngredient<Item> input) {
    this.inputIngredient = input;
    return this;
  }

  public MelterRecipeBuilder outputIngredient(IIngredient<Fluid> output) {
    this.outputIngredient = output;
    return this;
  }
  public MelterRecipeBuilder inputAmount(int input) {
    this.inputAmount = input;
    return this;
  }

  public MelterRecipeBuilder outputAmount(int output) {
    this.outputAmount = output;
    return this;
  }

  public AbstractRecipeBuilder<MelterRecipe> addRequirement(IRequirement<?> requirement) {
    this.requirements.add(requirement);
    if (requirement instanceof ItemRequirement r) {
      if (Objects.requireNonNull(r.getMode()) == IRequirement.ModeIO.INPUT) {
        inputIngredient = r.getItemIngredient();
        inputAmount = r.getItemAmount();
        input(new ItemStack(inputIngredient.getAll().get(0), inputAmount));
      }
    }
    if (requirement instanceof EnergyRequirement r) energy = r.getAmount();
    if (requirement instanceof FluidRequirement r) {
      if (Objects.requireNonNull(r.getMode()) == IRequirement.ModeIO.OUTPUT) {
        outputIngredient = r.getFluidIngredient();
        outputAmount = r.getFluidAmount();
        output(new FluidStack(outputIngredient.getAll().get(0), outputAmount));
      }
    }
    if (requirement instanceof TimeRequirement r) time = r.getTime();

    return this;
  }

  public NonNullList<Ingredient> ingredients() {
    return ingredients;
  }

  public MelterRecipeBuilder input(ItemStack input) {
    if (this.ingredients == null) this.ingredients = NonNullList.create();
    this.ingredients.add(Ingredient.of(input));
    return this;
  }

  public MelterRecipeBuilder ingredients(NonNullList<Ingredient> ingredients) {
    this.ingredients = ingredients;
    return this;
  }

  public FluidStack output() {
    return output;
  }

  public MelterRecipeBuilder output(FluidStack output) {
    this.output = output;
    return this;
  }
}
