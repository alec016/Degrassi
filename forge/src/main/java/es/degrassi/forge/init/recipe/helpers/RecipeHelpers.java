package es.degrassi.forge.init.recipe.helpers;

public class RecipeHelpers {
  public static final MelterRecipeHelper MELTER = new MelterRecipeHelper();
  public static final FurnaceRecipeHelper FURNACE = new FurnaceRecipeHelper();
  public static final UpgradeMakerRecipeHelper UPGRADE_MAKER = new UpgradeMakerRecipeHelper();


  public static void init() {
    MELTER.init();
    FURNACE.init();
    UPGRADE_MAKER.init();
  }
}
