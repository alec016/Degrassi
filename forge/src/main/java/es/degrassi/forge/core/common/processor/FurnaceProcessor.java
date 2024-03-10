package es.degrassi.forge.core.common.processor;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import es.degrassi.forge.core.init.RecipeRegistration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;

public class FurnaceProcessor extends MachineProcessor<FurnaceRecipe> {
  public FurnaceProcessor(MachineEntity<FurnaceRecipe> entity, boolean reset) {
    super(entity, reset);
  }

  public FurnaceProcessor(MachineEntity<FurnaceRecipe> entity) {
    this(entity, false);
  }

  @Override
  protected void init() {
    initialized = true;
    RecipeManager recipeManager = Objects.requireNonNull(entity.getLevel()).getRecipeManager();
    recipes = new ArrayList<>();
    recipes.addAll(recipeManager.getAllRecipesFor(RecipeRegistration.FURNACE_TYPE.get()));
    List<SmeltingRecipe> smeltingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMELTING);
    List<BlastingRecipe> blastingRecipes = recipeManager.getAllRecipesFor(RecipeType.BLASTING);
    List<SmokingRecipe> smokingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMOKING);
    RequirementManager manager = new RequirementManager();
    smeltingRecipes.forEach(smeltingRecipe -> {
      int time = smeltingRecipe.getCookingTime();
      ResourceLocation recipeId = smeltingRecipe.getId();
      ItemStack input = smeltingRecipe.getIngredients().get(0).getItems()[0];
      ItemStack result = smeltingRecipe.getResultItem(null);
      float xp = smeltingRecipe.getExperience();
      List<IRequirement<?>> requirements = manager
        .clear()
        .requireEnergyPerTick(100, "energy")
        .produceExperience(xp, "experience")
        .requireItem(input.getItem(), input.getCount(), "input")
        .produceItem(result.getItem(), result.getCount(), "output")
        .get();
      if (recipes.stream().filter(recipe -> recipe.getId().equals(recipeId)).toList().isEmpty())
        recipes.add(new FurnaceRecipe(recipeId, time, requirements));
    });
    blastingRecipes.forEach(blastingRecipe -> {
      int time = blastingRecipe.getCookingTime();
      ResourceLocation recipeId = blastingRecipe.getId();
      ItemStack input = blastingRecipe.getIngredients().get(0).getItems()[0];
      ItemStack result = blastingRecipe.getResultItem(null);
      float xp = blastingRecipe.getExperience();
      List<IRequirement<?>> requirements = manager
        .clear()
        .requireEnergyPerTick(100, "energy")
        .produceExperience(xp, "experience")
        .requireItem(input.getItem(), input.getCount(), "input")
        .produceItem(result.getItem(), result.getCount(), "output")
        .get();

      if (recipes.stream().filter(recipe -> recipe.getId().equals(recipeId)).toList().isEmpty())
        recipes.add(new FurnaceRecipe(recipeId, time, requirements));
    });
    smokingRecipes.forEach(smokingRecipe -> {
      int time = smokingRecipe.getCookingTime();
      ResourceLocation recipeId = smokingRecipe.getId();
      ItemStack input = smokingRecipe.getIngredients().get(0).getItems()[0];
      ItemStack result = smokingRecipe.getResultItem(null);
      float xp = smokingRecipe.getExperience();
      List<IRequirement<?>> requirements = manager
        .clear()
        .requireEnergyPerTick(100, "energy")
        .produceExperience(xp, "experience")
        .requireItem(input.getItem(), input.getCount(), "input")
        .produceItem(result.getItem(), result.getCount(), "output")
        .get();
      if (recipes.stream().filter(recipe -> recipe.getId().equals(recipeId)).toList().isEmpty())
        recipes.add(new FurnaceRecipe(recipeId, time, requirements));
    });
    if(this.futureRecipeID != null && this.entity.getLevel() != null) {
      this.entity.getLevel().getRecipeManager()
        .byKey(this.futureRecipeID)
        .filter(recipe -> recipe instanceof FurnaceRecipe)
        .map(recipe -> (FurnaceRecipe) recipe)
        .ifPresent(this::setRecipe);
      this.futureRecipeID = null;
    }
  }

  @Override
  public void setRecipe(FurnaceRecipe recipe) {
    this.currentRecipe = recipe == null ? null : recipe.copy();
    entity.setChanged();
  }
}
