package es.degrassi.forge.integration.jei;

import es.degrassi.forge.init.recipe.recipes.*;
import es.degrassi.forge.integration.jei.categories.*;
import mezz.jei.api.recipe.RecipeType;

@SuppressWarnings("rawtypes")
public class DegrassiJEIRecipeTypes {
  public static RecipeType<FurnaceRecipe> IRON_FURNACE_TYPE = new RecipeType<>(FurnaceRecipeCategory.createUID("iron"), FurnaceRecipe.class);
  public static RecipeType<FurnaceRecipe> GOLD_FURNACE_TYPE = new RecipeType<>(FurnaceRecipeCategory.createUID("gold"), FurnaceRecipe.class);
  public static RecipeType<FurnaceRecipe> DIAMOND_FURNACE_TYPE = new RecipeType<>(FurnaceRecipeCategory.createUID("diamond"), FurnaceRecipe.class);
  public static RecipeType<FurnaceRecipe> EMERALD_FURNACE_TYPE = new RecipeType<>(FurnaceRecipeCategory.createUID("emerald"), FurnaceRecipe.class);
  public static RecipeType<FurnaceRecipe> NETHERITE_FURNACE_TYPE = new RecipeType<>(FurnaceRecipeCategory.createUID("netherite"), FurnaceRecipe.class);
  public static RecipeType<MelterRecipe> MELTER_TYPE = new RecipeType<>(MelterRecipeCategory.UID, MelterRecipe.class);
  public static RecipeType<UpgradeMakerRecipe> UPGRADE_MAKER_TYPE = new RecipeType<>(UpgradeMakerRecipeCategory.UID, UpgradeMakerRecipe.class);
  public static RecipeType<GeneratorRecipe> GENERATOR_TYPE = new RecipeType<>(GeneratorRecipeCategory.createUID(""), GeneratorRecipe.class);
  public static RecipeType<GeneratorRecipe> JEWELRY_GENERATOR_TYPE = new RecipeType<>(GeneratorRecipeCategory.createUID("jewelry"), GeneratorRecipe.class);
  public static RecipeType<GeneratorRecipe> COMBUSTION_GENERATOR_TYPE = new RecipeType<>(GeneratorRecipeCategory.createUID("combustion"), GeneratorRecipe.class);
}
