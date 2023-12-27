package es.degrassi.forge.integration.jei;

import es.degrassi.forge.init.recipe.recipes.FurnaceRecipe;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
import es.degrassi.forge.init.recipe.recipes.UpgradeMakerRecipe;
import es.degrassi.forge.init.recipe.recipes.generators.JewelryGeneratorRecipe;
import es.degrassi.forge.integration.jei.categories.FurnaceRecipeCategory;
import es.degrassi.forge.integration.jei.categories.MelterRecipeCategory;
import es.degrassi.forge.integration.jei.categories.UpgradeMakerRecipeCategory;
import es.degrassi.forge.integration.jei.categories.generators.JewelryGeneratorRecipeCategory;
import mezz.jei.api.recipe.RecipeType;

public class DegrassiJEIRecipeTypes {
  public static RecipeType<FurnaceRecipe> FURNACE_TYPE = new RecipeType<>(FurnaceRecipeCategory.UID, FurnaceRecipe.class);
  public static RecipeType<MelterRecipe> MELTER_TYPE = new RecipeType<>(MelterRecipeCategory.UID, MelterRecipe.class);
  public static RecipeType<UpgradeMakerRecipe> UPGRADE_MAKER_TYPE = new RecipeType<>(UpgradeMakerRecipeCategory.UID, UpgradeMakerRecipe.class);
  public static RecipeType<JewelryGeneratorRecipe> JEWELRY_GENERATOR_TYPE = new RecipeType<>(JewelryGeneratorRecipeCategory.UID, JewelryGeneratorRecipe.class);
}
