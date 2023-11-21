package es.degrassi.forge.init.recipe.builder.generators;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.ingredient.IIngredient;
import es.degrassi.forge.init.recipe.builder.AbstractRecipeBuilder;
import es.degrassi.forge.init.recipe.recipes.generators.JewelryGeneratorRecipe;
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

public class JewelryGeneratorRecipeBuilder extends AbstractRecipeBuilder<JewelryGeneratorRecipe> {
  private int time;
  private int energy;
  private IIngredient<Item> inputIngredient;
  private ItemStack input;
  private int inputAmount;

  public static final NamedCodec<JewelryGeneratorRecipeBuilder> CODEC = NamedCodec.record(recipeBuilderInstance ->
    recipeBuilderInstance.group(
      NamedCodec.INT.fieldOf("time").forGetter(builder -> builder.time),
      NamedCodec.INT.fieldOf("energy").forGetter(builder -> builder.energy),
      IIngredient.ITEM.fieldOf("input").forGetter(builder -> builder.inputIngredient),
      NamedCodec.INT.optionalFieldOf("inputAmount", 1).forGetter(builder -> builder.inputAmount)
    ).apply(recipeBuilderInstance, (time, energy, input, inputAmount) -> {
      DegrassiLogger.INSTANCE.info("FJewelryGeneratorRecipeBuilderCODED[ time: " + time + ", energy: " + energy + ", input: " + input + ", amount: " + inputAmount + " ]");
      JewelryGeneratorRecipeBuilder builder = new JewelryGeneratorRecipeBuilder(time);
      builder
        .energy(energy)
        .input(new ItemStack(input.getAll().get(0), inputAmount));
      return builder;
    }), "Jewelry Generator recipe builder"
  );

  public JewelryGeneratorRecipeBuilder(int time) {
    super();
    this.time = time;
  }

  public JewelryGeneratorRecipeBuilder(JewelryGeneratorRecipe recipe) {
    super(recipe);
    this.time = recipe.getTime();
  }

  @Override
  public JewelryGeneratorRecipe build(ResourceLocation id) {
    JewelryGeneratorRecipe recipe = new JewelryGeneratorRecipe(id, NonNullList.withSize(1, Ingredient.of(input)), time, energy);
    DegrassiLogger.INSTANCE.info("JewelryGeneratorRecipeBuilder$build: " + recipe);
    return recipe;
  }

  public JewelryGeneratorRecipeBuilder time (int time) {
    this.time = time;
    return this;
  }

  public JewelryGeneratorRecipeBuilder energy(int energy) {
    this.energy = energy;
    return this;
  }

  public JewelryGeneratorRecipeBuilder input(ItemStack input) {
    this.input = input;
    return this;
  }

  public AbstractRecipeBuilder<JewelryGeneratorRecipe> addRequirement(IRequirement<?> requirement) {
    this.requirements.add(requirement);
    if (requirement instanceof ItemRequirement r && r.getMode() == IRequirement.ModeIO.INPUT) input = r.getItem();
    else if (requirement instanceof EnergyRequirement r && r.getMode() == IRequirement.ModeIO.OUTPUT) energy = r.getAmount();
    return this;
  }
}
