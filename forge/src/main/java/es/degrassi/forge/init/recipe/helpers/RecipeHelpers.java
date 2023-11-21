package es.degrassi.forge.init.recipe.helpers;

import es.degrassi.forge.init.recipe.helpers.generators.JewelryGeneratorRecipeHelper;

public class RecipeHelpers {
  public static final MelterRecipeHelper MELTER = new MelterRecipeHelper();
  public static final FurnaceRecipeHelper FURNACE = new FurnaceRecipeHelper();
  public static final UpgradeMakerRecipeHelper UPGRADE_MAKER = new UpgradeMakerRecipeHelper();
  public static final JewelryGeneratorRecipeHelper JEWELRY_GENERATOR = new JewelryGeneratorRecipeHelper();

  public static void init() {
    MELTER.init();
    FURNACE.init();
    UPGRADE_MAKER.init();
    JEWELRY_GENERATOR.init();
  }
}
