package es.degrassi.forge.init.recipe.builder;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.ingredient.IIngredient;
import es.degrassi.forge.init.recipe.recipes.FurnaceRecipe;
import es.degrassi.forge.requirements.EnergyRequirement;
import es.degrassi.forge.requirements.ExperienceRequirement;
import es.degrassi.forge.requirements.IRequirement;
import es.degrassi.forge.requirements.ItemRequirement;
import es.degrassi.forge.util.DegrassiLogger;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("unused")
public class FurnaceRecipeBuilder extends AbstractRecipeBuilder<FurnaceRecipe> {
  private int time;
  private float xp;
  private int energy;
  private IIngredient<Item> outputIngredient;
  private IIngredient<Item> inputIngredient;
  private ItemStack input;
  private ItemStack output;
  private int inputAmount;
  private int outputAmount;

  public static final NamedCodec<FurnaceRecipeBuilder> CODEC = NamedCodec.record(recipeBuilderInstance ->
    recipeBuilderInstance.group(
      NamedCodec.INT.fieldOf("time").forGetter(builder -> builder.time),
      NamedCodec.FLOAT.fieldOf("xp").forGetter(builder -> builder.xp),
      NamedCodec.INT.fieldOf("energy").forGetter(builder -> builder.energy),
      IIngredient.ITEM.fieldOf("input").forGetter(builder -> builder.inputIngredient),
      NamedCodec.INT.optionalFieldOf("inputAmount", 1).forGetter(builder -> builder.inputAmount),
      IIngredient.ITEM.fieldOf("output").forGetter(builder -> builder.outputIngredient),
      NamedCodec.INT.optionalFieldOf("outputAmount", 1).forGetter(builder -> builder.outputAmount)
    ).apply(recipeBuilderInstance, (time, xp, energy, input, inputAmount, output, outputAmount) -> {
      DegrassiLogger.INSTANCE.info("FurnaceRecipeBuilderCODED[ time: " + time + ", energy: " + energy + ", input: " + input + ", amount: " + inputAmount + ", output: " + output + ", amount: " + outputAmount + " ]");
      FurnaceRecipeBuilder builder = new FurnaceRecipeBuilder(time);
      builder
        .xp(xp)
        .energy(energy)
        .input(new ItemStack(input.getAll().get(0), inputAmount))
        .output(new ItemStack(output.getAll().get(0), outputAmount));
      return builder;
    }), "Furnace recipe builder"
  );

  public FurnaceRecipeBuilder(int time) {
    super();
    this.time = time;
  }

  public FurnaceRecipeBuilder(FurnaceRecipe recipe) {
    super(recipe);
    this.time = recipe.getTime();
  }

  @Override
  public FurnaceRecipe build(ResourceLocation id) {
    FurnaceRecipe recipe = new FurnaceRecipe(id, output, NonNullList.withSize(1, Ingredient.of(input)), time, energy, xp);
    DegrassiLogger.INSTANCE.info("FurnaceRecipeBuilder$build: " + recipe);
    return recipe;
  }

  public FurnaceRecipeBuilder xp(float xp) {
    this.xp = xp;
    return this;
  }

  public FurnaceRecipeBuilder time (int time) {
    this.time = time;
    return this;
  }

  public FurnaceRecipeBuilder energy(int energy) {
    this.energy = energy;
    return this;
  }

  public FurnaceRecipeBuilder input(ItemStack input) {
    this.input = input;
    return this;
  }

  public FurnaceRecipeBuilder output(ItemStack output) {
    this.output = output;
    return this;
  }

  public AbstractRecipeBuilder<FurnaceRecipe> addRequirement(IRequirement<?> requirement) {
    this.requirements.add(requirement);
    if (requirement instanceof ItemRequirement r) {
      switch(r.getMode()) {
        case INPUT -> input = r.getItem();
        case OUTPUT -> output = r.getItem();
      }
    } else if (requirement instanceof EnergyRequirement r) energy = r.getAmount();
    else if (requirement instanceof ExperienceRequirement r) xp = r.getXP();
    return this;
  }
}
