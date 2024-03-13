package es.degrassi.forge.core.common.recipe.builder;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.impl.codec.RegistrarCodec;
import es.degrassi.forge.api.utils.DegrassiLogger;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class FurnaceBuilder extends MachineBuilder<FurnaceRecipe> {

  public static final NamedCodec<FurnaceBuilder> CODEC = NamedCodec.record(
    recipeBuilder -> recipeBuilder.group(
      NamedCodec.INT.fieldOf("time").forGetter(FurnaceBuilder::getTime),
      NamedCodec.INT.fieldOf("energy").forGetter(builder -> builder.energy),
      NamedCodec.FLOAT.fieldOf("experience").forGetter(builder -> builder.experience),
      RegistrarCodec.ITEM.fieldOf("input").forGetter(builder -> builder.input),
      RegistrarCodec.ITEM.fieldOf("output").forGetter(builder -> builder.output),
      NamedCodec.INT.optionalFieldOf("inputAmount", 1).forGetter(builder -> builder.inputAmount),
      NamedCodec.INT.optionalFieldOf("outputAmount", 1).forGetter(builder -> builder.outputAmount)
    ).apply(recipeBuilder, (
      time,
      energy,
      experience,
      itemInput,
      itemOutput,
      inputAmount,
      outputAmount
    ) -> new FurnaceBuilder(time)
      .energy(energy)
      .experience(experience)
      .input(itemInput, inputAmount)
      .output(itemOutput, outputAmount)),
    "furnace recipe"
  );

  private int energy, inputAmount, outputAmount;
  private float experience;
  private Item input, output;

  public FurnaceBuilder(int time) {
    super(time);
  }

  public FurnaceBuilder(FurnaceRecipe recipe) {
    super(recipe);
  }

  public FurnaceBuilder energy(int energy) {
    this.energy = energy;
    requireEnergy(energy, "energy");
    return this;
  }

  public FurnaceBuilder experience(float experience) {
    this.experience = experience;
    produceExperience(experience, "experience");
    return this;
  }

  public FurnaceBuilder input(Item input, int amount) {
    this.input = input;
    this.inputAmount = amount;
    requireItem(input, amount, "input");
    return this;
  }

  public FurnaceBuilder output(Item item, int amount) {
    this.output = item;
    this.outputAmount = amount;
    produceItem(item, amount, "output");
    return this;
  }

  @Override
  public FurnaceRecipe build(ResourceLocation id) {
    FurnaceRecipe recipe = new FurnaceRecipe(id, getTime(), getRequirements());
    DegrassiLogger.INSTANCE.info("FurnaceRecipeBuilder$build -> recipe: {}", recipe);
    return recipe;
  }
}
