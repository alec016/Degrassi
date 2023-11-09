package es.degrassi.forge.integration.kubejs.events;

import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import es.degrassi.forge.integration.kubejs.recipes.builder.FurnaceRecipeBuilderJS;
import es.degrassi.forge.integration.kubejs.recipes.builder.MelterRecipeBuilderJS;
import es.degrassi.forge.integration.kubejs.recipes.builder.UpgradeMakerRecipeBuilderJS;
import es.degrassi.forge.util.DegrassiLogger;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class DegrassiRecipesEvents extends RecipesEventJS {

  private final List<MelterRecipeBuilderJS> melterBuilders = new ArrayList<>();
  private final List<FurnaceRecipeBuilderJS> furnaceBuilders = new ArrayList<>();
  private final List<UpgradeMakerRecipeBuilderJS> upgradeMakerBuilders = new ArrayList<>();

  public DegrassiRecipesEvents() {}

  public MelterRecipeBuilderJS melter(int time) {
    DegrassiLogger.INSTANCE.info("DegrassiRecipesEvents$melter");
    return new MelterRecipeBuilderJS(time);
  }

  public FurnaceRecipeBuilderJS furnace(int time) {
    DegrassiLogger.INSTANCE.info("DegrassiRecipesEvents$furnace");
    return new FurnaceRecipeBuilderJS(time);
  }

  public UpgradeMakerRecipeBuilderJS upgradeMaker(int time) {
    DegrassiLogger.INSTANCE.info("DegrassiRecipesEvents$upgradeMaker");
    return new UpgradeMakerRecipeBuilderJS(time);
  }

  public List<UpgradeMakerRecipeBuilderJS> getUpgradeMakerBuilders() {
    return upgradeMakerBuilders;
  }

  public List<MelterRecipeBuilderJS> getMelterBuilders() {
    return melterBuilders;
  }

  public List<FurnaceRecipeBuilderJS> getFurnaceBuilders() {
    return furnaceBuilders;
  }

}
