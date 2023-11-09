package es.degrassi.forge.init.recipe.builder;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.ingredient.IIngredient;
import es.degrassi.forge.init.recipe.recipes.UpgradeMakerRecipe;
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

@SuppressWarnings("unused")
public class UpgradeMakerRecipeBuilder extends AbstractRecipeBuilder<UpgradeMakerRecipe> {
  private int time;
  private int energy;
  private NonNullList<Ingredient> ingredients;
  private FluidStack fluidInput;
  private ItemStack itemOutput;
  private IIngredient<Fluid> inputFluidIngredient;
  private IIngredient<Item> inputItemIngredient1;
  private IIngredient<Item> inputItemIngredient2;
  private IIngredient<Item> outputItemIngredient;
  private int inputFluidAmount;
  private int inputItemAmount1;
  private int inputItemAmount2;
  private int outputAmount;

  public static final NamedCodec<UpgradeMakerRecipeBuilder> CODEC = NamedCodec.record(recipeBuilderInstance ->
    recipeBuilderInstance.group(
      NamedCodec.INT.fieldOf("time").forGetter(builder -> builder.time),
      NamedCodec.INT.fieldOf("energy").forGetter(builder -> builder.energy),
      IIngredient.ITEM.fieldOf("input1").forGetter(builder -> builder.inputItemIngredient1),
      NamedCodec.INT.optionalFieldOf("inputAmount1", 1).forGetter(builder -> builder.inputItemAmount1),
      IIngredient.ITEM.fieldOf("input2").forGetter(builder -> builder.inputItemIngredient2),
      NamedCodec.INT.optionalFieldOf("inputAmount2", 1).forGetter(builder -> builder.inputItemAmount2),
      IIngredient.FLUID.fieldOf("fluidInput").forGetter(builder -> builder.inputFluidIngredient),
      NamedCodec.INT.optionalFieldOf("inputFluidAmount", 1000).forGetter(builder -> builder.inputFluidAmount),
      IIngredient.ITEM.fieldOf("output").forGetter(builder -> builder.outputItemIngredient),
      NamedCodec.INT.optionalFieldOf("outputAmount", 1).forGetter(builder -> builder.outputAmount)
    ).apply(recipeBuilderInstance, (time, energy, input1, inputAmount1, input2, inputAmount2, fluidInput, fluidInputAmount, output, outputAmount) -> {
      DegrassiLogger.INSTANCE.info("UpgradeMakerRecipeBuilderCODED[ time: " + time + ", energy: " + energy + ", input1: " + input1 + ", amount1: " + inputAmount1 + ", input2: " + input2 + ", amount2: " + inputAmount2 + ", fluid_input: " + fluidInput + ", fluid_amount: " + fluidInputAmount + ", output: " + output + ", amount: " + outputAmount + " ]");
      return new UpgradeMakerRecipeBuilder(time)
        .energy(energy)
        .input1(new ItemStack(input1.getAll().get(0), inputAmount1))
        .input2(new ItemStack(input2.getAll().get(0), inputAmount2))
        .fluidInput(new FluidStack(fluidInput.getAll().get(0), fluidInputAmount))
        .output(new ItemStack(output.getAll().get(0), outputAmount));
    }), "Upgrade Maker recipe builder"
  );

  public UpgradeMakerRecipeBuilder(int time) {
    super();
    this.time = time;
  }

  public UpgradeMakerRecipeBuilder(UpgradeMakerRecipe recipe) {
    super(recipe);
    this.time = recipe.getTime();
  }

  @Override
  public UpgradeMakerRecipe build(ResourceLocation id) {
    UpgradeMakerRecipe recipe = new UpgradeMakerRecipe(id, time, energy, ingredients, fluidInput, itemOutput);
    DegrassiLogger.INSTANCE.info("UpgradeMakerRecipe$build: " + recipe);
    return recipe;
  }

  @Override
  public AbstractRecipeBuilder<UpgradeMakerRecipe> addRequirement(IRequirement<?> requirement) {
    this.requirements.add(requirement);
    if (requirement instanceof ItemRequirement r) {
      if (Objects.requireNonNull(r.getMode()) == IRequirement.ModeIO.INPUT) {
        if (inputItemIngredient1 == null) {
          inputItemIngredient1 = r.getItemIngredient();
          inputItemAmount1 = r.getItemAmount();
          input1(new ItemStack(inputItemIngredient1.getAll().get(0), inputItemAmount1));
        } else {
          inputItemIngredient2 = r.getItemIngredient();
          inputItemAmount2 = r.getItemAmount();
          input2(new ItemStack(inputItemIngredient2.getAll().get(0), inputItemAmount2));
        }
      } else if (Objects.requireNonNull(r.getMode()) == IRequirement.ModeIO.OUTPUT) {
        outputItemIngredient = r.getItemIngredient();
        outputAmount = r.getItemAmount();
        output(new ItemStack(outputItemIngredient.getAll().get(0), outputAmount));
      }
    }
    if (requirement instanceof EnergyRequirement r) energy = r.getAmount();
    if (requirement instanceof FluidRequirement r) {
      if (Objects.requireNonNull(r.getMode()) == IRequirement.ModeIO.INPUT) {
        inputFluidIngredient = r.getFluidIngredient();
        inputFluidAmount = r.getFluidAmount();
        fluidInput(new FluidStack(inputFluidIngredient.getAll().get(0), inputFluidAmount));
      }
    }
    if (requirement instanceof TimeRequirement r) time = r.getTime();

    return this;
  }

  public NonNullList<Ingredient> ingredients() {
    return ingredients;
  }

  public UpgradeMakerRecipeBuilder ingredients(NonNullList<Ingredient> ingredients) {
    this.ingredients = ingredients;
    return this;
  }

  private UpgradeMakerRecipeBuilder energy(Integer energy) {
    this.energy = energy;
    return this;
  }

  private UpgradeMakerRecipeBuilder input1(ItemStack itemStack) {
    if (this.ingredients == null) this.ingredients = NonNullList.create();
    this.ingredients.add(Ingredient.of(itemStack));
    return this;
  }

  private UpgradeMakerRecipeBuilder input2(ItemStack itemStack) {
    if (this.ingredients == null) this.ingredients = NonNullList.create();
    this.ingredients.add(Ingredient.of(itemStack));
    return this;
  }

  private UpgradeMakerRecipeBuilder output(ItemStack itemStack) {
    itemOutput = itemStack;
    return this;
  }

  private UpgradeMakerRecipeBuilder fluidInput(FluidStack fluidStack) {
    this.fluidInput = fluidStack;
    return this;
  }
}
