package es.degrassi.forge.init.recipe.builder;

import com.google.common.collect.*;
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

public class GeneratorRecipeBuilder extends AbstractRecipeBuilder<GeneratorRecipe> {
  private int time;
  private int energy;
  private IIngredient<Item> inputIngredient;
  private ItemStack input;
  private int inputAmount;
  private final List<String> machineIds;

  public static final NamedCodec<GeneratorRecipeBuilder> CODEC = NamedCodec.record(recipeBuilderInstance ->
    recipeBuilderInstance.group(
      NamedCodec.INT.fieldOf("time").forGetter(builder -> builder.time),
      NamedCodec.INT.fieldOf("energy").forGetter(builder -> builder.energy),
      IIngredient.ITEM.fieldOf("input").forGetter(builder -> builder.inputIngredient),
      NamedCodec.INT.optionalFieldOf("inputAmount", 1).forGetter(builder -> builder.inputAmount),
      NamedCodec.STRING.listOf().fieldOf("machines").forGetter(builder -> builder.machineIds)
    ).apply(recipeBuilderInstance, (time, energy, input, inputAmount, machineIds) -> {
      DegrassiLogger.INSTANCE.info("GeneratorRecipeBuilderCODED[time: {}, energy: {}, input: {}, inputAmount: {}, machineIds: {}]",
        time, energy, input, inputAmount, machineIds
      );
      return new GeneratorRecipeBuilder(time)
        .energy(energy)
        .input(new ItemStack(input.getAll().get(0), inputAmount), input, inputAmount)
        .addMachines(machineIds);
    }), "Generator recipe builder"
  );

  public GeneratorRecipeBuilder(int time) {
    this.time = time;
    this.machineIds = Lists.newLinkedList();
  }

  public GeneratorRecipeBuilder(GeneratorRecipe recipe) {
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

  public GeneratorRecipeBuilder input(ItemStack input, IIngredient<Item> ingredient, int amount) {
    this.input = input;
    this.inputIngredient = ingredient;
    this.inputAmount = amount;
    return this;
  }

  private void addMachine(String machineId) {
    this.machineIds.add(machineId);
  }

  public GeneratorRecipeBuilder addMachines(List<String> machineIds) {
    machineIds.forEach(this::addMachine);
    return this;
  }

  public AbstractRecipeBuilder<GeneratorRecipe> addRequirement(IRequirement<?> requirement) {
    this.requirements.add(requirement);
    if (requirement instanceof ItemRequirement r && r.getMode() == IRequirement.ModeIO.INPUT) input = r.getItem();
    else if (requirement instanceof EnergyRequirement r && r.getMode() == IRequirement.ModeIO.OUTPUT) energy = r.getAmount();
    else if (requirement instanceof MachineRequirement r) {
      r.getMachines().forEach(machine -> {
        if (machineIds.stream().filter(m -> m.equals(machine)).toList().isEmpty())
          machineIds.add(machine);
      });
    }
    return this;
  }

  @Override
  public GeneratorRecipe build(ResourceLocation id) {
    GeneratorRecipe recipe = new GeneratorRecipe(id, NonNullList.withSize(1, Ingredient.of(input)), energy, time, machineIds);
    DegrassiLogger.INSTANCE.info("GeneratorRecipeBuilder$build: " + recipe);
    return recipe;
  }
}
