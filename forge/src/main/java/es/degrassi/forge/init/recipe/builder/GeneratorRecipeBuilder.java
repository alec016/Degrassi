package es.degrassi.forge.init.recipe.builder;

import es.degrassi.forge.api.codec.*;
import es.degrassi.forge.api.ingredient.*;
import es.degrassi.forge.init.recipe.recipes.*;
import es.degrassi.forge.requirements.*;
import es.degrassi.forge.util.*;
import java.util.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

public class GeneratorRecipeBuilder extends AbstractRecipeBuilder<GeneratorRecipe<?>> {
  private int time;
  private int energy;
  private IIngredient<Item> inputIngredient;
  private ItemStack input;
  private int inputAmount;
  private List<String> machineIds;

  public static final NamedCodec<GeneratorRecipeBuilder> CODEC = NamedCodec.record(recipeBuilderInstance ->
    recipeBuilderInstance.group(
      NamedCodec.INT.fieldOf("time").forGetter(builder -> builder.time),
      NamedCodec.INT.fieldOf("energy").forGetter(builder -> builder.energy),
      IIngredient.ITEM.fieldOf("input").forGetter(builder -> builder.inputIngredient),
      NamedCodec.INT.optionalFieldOf("inputAmount", 1).forGetter(builder -> builder.inputAmount),
      NamedCodec.STRING.listOf().fieldOf("machineIds").forGetter(builder -> builder.machineIds)
    ).apply(recipeBuilderInstance, (time, energy, input, inputAmount, machineIds) -> {
      DegrassiLogger.INSTANCE.info("GeneratorRecipeBuilderCODED[ time: " + time + ", energy: " + energy + ", input: " + input + ", amount: " + inputAmount + " ]");
      GeneratorRecipeBuilder builder = new GeneratorRecipeBuilder(time);
      builder
        .energy(energy)
        .input(new ItemStack(input.getAll().get(0), inputAmount));
      machineIds.forEach(builder::addMachine);
      return builder;
    }), "Generator recipe builder"
  );

  public GeneratorRecipeBuilder(int time) {
    super();
    this.time = time;
  }

  public GeneratorRecipeBuilder(GeneratorRecipe<?> recipe) {
    super(recipe);
    this.time = recipe.getTime();
    this.machineIds = recipe.getMachineIds();
    this.energy = recipe.getEnergyRequired();
    this.input = recipe.getIngredients().get(0).getItems()[0];
  }

  public GeneratorRecipeBuilder time (int time) {
    this.time = time;
    return this;
  }

  public GeneratorRecipeBuilder energy(int energy) {
    this.energy = energy;
    return this;
  }

  public GeneratorRecipeBuilder input(ItemStack input) {
    this.input = input;
    return this;
  }

  public GeneratorRecipeBuilder addMachine(String machineId) {
    this.machineIds.add(machineId);
    return this;
  }

  public AbstractRecipeBuilder<GeneratorRecipe<?>> addRequirement(IRequirement<?> requirement) {
    this.requirements.add(requirement);
    if (requirement instanceof ItemRequirement r && r.getMode() == IRequirement.ModeIO.INPUT) input = r.getItem();
    else if (requirement instanceof EnergyRequirement r && r.getMode() == IRequirement.ModeIO.OUTPUT) energy = r.getAmount();
    return this;
  }

  @Override
  public GeneratorRecipe<?> build(ResourceLocation id) {
    GeneratorRecipe<?> recipe = new GeneratorRecipe<>(id, NonNullList.withSize(1, Ingredient.of(input)), time, energy, machineIds);
    DegrassiLogger.INSTANCE.info("JewelryGeneratorRecipeBuilder$build: " + recipe);
    return recipe;
  }
}
