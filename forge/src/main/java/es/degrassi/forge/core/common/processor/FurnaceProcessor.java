package es.degrassi.forge.core.common.processor;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
import es.degrassi.forge.core.init.RecipeRegistration;
import es.degrassi.forge.core.tiers.Furnace;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;

public class FurnaceProcessor extends MachineProcessor<FurnaceRecipe, FurnaceEntity> {

  public FurnaceProcessor(FurnaceEntity entity, boolean reset) {
    super(entity, reset);
  }

  public FurnaceProcessor(FurnaceEntity entity) {
    this(entity, false);
  }

  @Override
  public void init() {
    initialized = true;
    Level level = entity == null ? Objects.requireNonNull(Minecraft.getInstance().level) : entity.getLevel();
    RecipeManager recipeManager = level == null ? Minecraft.getInstance().level.getRecipeManager() : level.getRecipeManager();
    recipes = new ArrayList<>();
    Furnace tier = entity == null ? Furnace.IRON : entity.getTier();
    recipes.addAll(recipeManager.getAllRecipesFor(RecipeRegistration.FURNACE_TYPE.get()).stream().map(recipe -> {
      recipe = recipe.copy();
      recipe.setTime((int) (recipe.getTime() * tier.getSpeedModifier()));
      recipe.getRequirements().forEach(req -> {
        if (req instanceof EnergyRequirement energy) {
          energy.setAmount((int) (energy.getAmount() * tier.getEnergyModifier()));
        }
      });
      return recipe;
    }).toList());
    List<SmeltingRecipe> smeltingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMELTING);
    List<BlastingRecipe> blastingRecipes = recipeManager.getAllRecipesFor(RecipeType.BLASTING);
    List<SmokingRecipe> smokingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMOKING);
    smeltingRecipes.forEach(smeltingRecipe -> {
      int time = (int) (smeltingRecipe.getCookingTime() * tier.getSpeedModifier());
      ResourceLocation recipeId = smeltingRecipe.getId();
      ItemStack result = smeltingRecipe.getResultItem(null);
      float xp = smeltingRecipe.getExperience();
      if (smeltingRecipe.getIngredients().get(0).getItems().length > 1) {
        List.of(smeltingRecipe.getIngredients().get(0).getItems()).forEach(input -> {
          List<IRequirement<?>> requirements = new RequirementManager()
            .requireEnergyPerTick((int) (100 * tier.getEnergyModifier()), "energy")
            .produceExperience(xp, "experience")
            .requireItem(input.getItem(), input.getCount(), "input")
            .produceItem(result.getItem(), result.getCount(), "output")
            .get();
          String[] inputSA = input.getHoverName().getString().toLowerCase().split(" ");
          StringBuilder inputS = new StringBuilder();
          AtomicInteger index = new AtomicInteger(0);
          List.of(inputSA).forEach(i -> {
            inputS.append(i);
            if (index.get() + 1 != inputSA.length) inputS.append("_");
            index.getAndIncrement();
          });
          recipes.add(new FurnaceRecipe(new ResourceLocation(recipeId.getNamespace(), recipeId.getPath() + "_from_" + inputS), time, requirements));
        });
      } else {
        ItemStack input = smeltingRecipe.getIngredients().get(0).getItems()[0];
        List<IRequirement<?>> requirements = new RequirementManager()
          .requireEnergyPerTick((int) (100 * tier.getEnergyModifier()), "energy")
          .produceExperience(xp, "experience")
          .requireItem(input.getItem(), input.getCount(), "input")
          .produceItem(result.getItem(), result.getCount(), "output")
          .get();
        if (recipes.stream().filter(recipe -> recipe.getId().equals(recipeId)).toList().isEmpty())
          recipes.add(new FurnaceRecipe(recipeId, time, requirements));
      }
    });
    blastingRecipes.forEach(blastingRecipe -> {
      int time = (int) (blastingRecipe.getCookingTime() * tier.getSpeedModifier());
      ResourceLocation recipeId = blastingRecipe.getId();
      ItemStack result = blastingRecipe.getResultItem(null);
      float xp = blastingRecipe.getExperience();
      if (blastingRecipe.getIngredients().get(0).getItems().length > 1) {
        List.of(blastingRecipe.getIngredients().get(0).getItems()).forEach(input -> {
          List<IRequirement<?>> requirements = new RequirementManager()
            .requireEnergyPerTick((int) (100 * tier.getEnergyModifier()), "energy")
            .produceExperience(xp, "experience")
            .requireItem(input.getItem(), input.getCount(), "input")
            .produceItem(result.getItem(), result.getCount(), "output")
            .get();
          String[] inputSA = input.getHoverName().getString().toLowerCase().split(" ");
          StringBuilder inputS = new StringBuilder();
          AtomicInteger index = new AtomicInteger(0);
          List.of(inputSA).forEach(i -> {
            inputS.append(i);
            if (index.get() + 1 != inputSA.length) inputS.append("_");
            index.getAndIncrement();
          });
          recipes.add(new FurnaceRecipe(new ResourceLocation(recipeId.getNamespace(), recipeId.getPath() + "_from_" + inputS), time, requirements));
        });
      } else {
        ItemStack input = blastingRecipe.getIngredients().get(0).getItems()[0];
        List<IRequirement<?>> requirements = new RequirementManager()
          .requireEnergyPerTick((int) (100 * tier.getEnergyModifier()), "energy")
          .produceExperience(xp, "experience")
          .requireItem(input.getItem(), input.getCount(), "input")
          .produceItem(result.getItem(), result.getCount(), "output")
          .get();
        if (recipes.stream().filter(recipe -> recipe.getId().equals(recipeId)).toList().isEmpty())
          recipes.add(new FurnaceRecipe(recipeId, time, requirements));
      }
    });
    smokingRecipes.forEach(smokingRecipe -> {
      int time = (int) (smokingRecipe.getCookingTime() * tier.getSpeedModifier());
      ResourceLocation recipeId = smokingRecipe.getId();
      ItemStack result = smokingRecipe.getResultItem(null);
      float xp = smokingRecipe.getExperience();
      if (smokingRecipe.getIngredients().get(0).getItems().length > 1) {
        List.of(smokingRecipe.getIngredients().get(0).getItems()).forEach(input -> {
          List<IRequirement<?>> requirements = new RequirementManager()
            .requireEnergyPerTick(100, "energy")
            .produceExperience(xp, "experience")
            .requireItem(input.getItem(), input.getCount(), "input")
            .produceItem(result.getItem(), result.getCount(), "output")
            .get();
          String[] inputSA = input.getHoverName().getString().toLowerCase().split(" ");
          StringBuilder inputS = new StringBuilder();
          AtomicInteger index = new AtomicInteger(0);
          List.of(inputSA).forEach(i -> {
            inputS.append(i);
            if (index.get() + 1 != inputSA.length) inputS.append("_");
            index.getAndIncrement();
          });
          recipes.add(new FurnaceRecipe(new ResourceLocation(recipeId.getNamespace(), recipeId.getPath() + "_from_" + inputS), time, requirements));
        });
      } else {
        ItemStack input = smokingRecipe.getIngredients().get(0).getItems()[0];
        List<IRequirement<?>> requirements = new RequirementManager()
          .requireEnergyPerTick(100, "energy")
          .produceExperience(xp, "experience")
          .requireItem(input.getItem(), input.getCount(), "input")
          .produceItem(result.getItem(), result.getCount(), "output")
          .get();
        if (recipes.stream().filter(recipe -> recipe.getId().equals(recipeId)).toList().isEmpty())
          recipes.add(new FurnaceRecipe(recipeId, time, requirements));
      }
    });
    if(this.futureRecipeID != null && this.entity.getLevel() != null) {
      this.recipes.forEach(recipe -> {
        if (recipe.getId().equals(futureRecipeID))
          setRecipe(recipe);
      });
      this.futureRecipeID = null;
    }
  }
}
