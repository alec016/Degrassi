package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.builder;

import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.impl.codec.RegistrarCodec;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.MelterRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class MelterRecipeBuilder extends MultiblockRecipeBuilder<MelterRecipe> {


  public static final NamedCodec<MelterRecipeBuilder> CODEC = NamedCodec.record(
    recipeBuilder -> recipeBuilder.group(
      NamedCodec.INT.fieldOf("time").forGetter(MelterRecipeBuilder::getTime),
      NamedCodec.INT.fieldOf("energy").forGetter(builder -> builder.energy),
      RegistrarCodec.ITEM.fieldOf("input").forGetter(builder -> builder.input),
      RegistrarCodec.FLUID.fieldOf("output").forGetter(builder -> builder.output),
      NamedCodec.INT.optionalFieldOf("inputAmount", 1).forGetter(builder -> builder.inputAmount),
      NamedCodec.INT.optionalFieldOf("outputAmount", 1).forGetter(builder -> builder.outputAmount)
    ).apply(recipeBuilder, (
      time,
      energy,
      itemInput,
      fluidOutput,
      inputAmount,
      outputAmount
    ) -> new MelterRecipeBuilder(time)
      .energy(energy)
      .input(itemInput, inputAmount)
      .output(fluidOutput, outputAmount)),
    "furnace recipe"
  );

  private int energy, inputAmount, outputAmount;
  private Item input;
  private Fluid output;

  public MelterRecipeBuilder(int time) {
    super(time);
  }

  public MelterRecipeBuilder(MelterRecipe recipe) {
    super(recipe);
  }

  public MelterRecipeBuilder energy(int energy) {
    this.energy = energy;
    requireEnergyPerTick(energy, "energy");
    return this;
  }

  public MelterRecipeBuilder input(Item input, int amount) {
    this.input = input;
    this.inputAmount = amount;
    requireItem(input, amount, "");
    return this;
  }

  public MelterRecipeBuilder output(Fluid fluid, int amount) {
    this.output = fluid;
    this.outputAmount = amount;
    produceFluid(fluid, amount, "fluid_output");
    return this;
  }

  @Override
  public MelterRecipe build(ResourceLocation id) {
    MelterRecipe recipe = new MelterRecipe(id, getTime(), getRequirements());
    DegrassiLogger.INSTANCE.info("MelterRecipeBuilder$build -> recipe: {}", recipe);
    return recipe;
  }
}
