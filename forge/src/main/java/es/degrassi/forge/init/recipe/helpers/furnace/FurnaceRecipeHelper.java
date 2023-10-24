package es.degrassi.forge.init.recipe.helpers.furnace;

import es.degrassi.forge.init.entity.furnace.*;
import es.degrassi.forge.init.item.upgrade.EnergyUpgrade;
import es.degrassi.forge.init.item.upgrade.SpeedUpgrade;
import es.degrassi.forge.init.recipe.furnace.FurnaceRecipe;
import es.degrassi.forge.init.registration.RecipeRegistry;
import es.degrassi.forge.util.DegrassiLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FurnaceRecipeHelper {
  public static final NonNullList<FurnaceRecipe> recipes = NonNullList.create();
  public static final Map<ResourceLocation, FurnaceRecipe> recipesMap = new HashMap<>();

  public static void extractEnergy(@NotNull FurnaceEntity entity) {
    entity.ENERGY_STORAGE.setEnergy(entity.ENERGY_STORAGE.getEnergyStored() - entity.recipe.getEnergyRequired());
  }

  public static boolean hasEnoughEnergy(@NotNull FurnaceEntity entity) {
    return entity.ENERGY_STORAGE.getEnergyStored() >= entity.recipe.getEnergyRequired();
  }

  public static boolean canInsertItemIntoOutputSlot(@NotNull SimpleContainer inventory, @NotNull ItemStack stack) {
    return inventory.getItem(3).getItem() == stack.getItem() || inventory.getItem(3).isEmpty();
  }

  public static boolean canInsertAmountIntoOutputSlot(@NotNull SimpleContainer inventory) {
    return inventory.getItem(3).getMaxStackSize() > inventory.getItem(3).getCount();
  }

  public static boolean hasRecipe(@NotNull FurnaceEntity entity) {
    Level level = entity.getLevel();
    if (level == null) return false;

    SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
    for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
      inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
    }

    recipes.forEach(recipe -> {
      if (recipe == null) return;
      if (recipe.getIngredients().get(0).test(inventory.getItem(2))) entity.recipe = recipe.copy();
    });

    if (entity.recipe == null) return false;

    boolean can = canInsertAmountIntoOutputSlot(inventory) && canInsertItemIntoOutputSlot(inventory, entity.recipe.getResultItem()) && hasEnoughEnergy(entity);

    if (can && !entity.recipe.isModified()) {
      if (entity instanceof IronFurnaceEntity) {
        entity.recipe.setTime(entity.recipe.getTime() * 95 / 100);
      } else if(entity instanceof GoldFurnaceEntity) {
        entity.recipe.setTime(entity.recipe.getTime() * 80 / 100);
        entity.recipe.setEnergyRequired(entity.recipe.getEnergyRequired() * 120 / 100);
      } else if (entity instanceof DiamondFurnaceEntity) {
        entity.recipe.setTime(entity.recipe.getTime() * 60 / 100);
        entity.recipe.setEnergyRequired(entity.recipe.getEnergyRequired() * 140 / 100);
      } else if (entity instanceof EmeraldFurnaceEntity) {
        entity.recipe.setTime(entity.recipe.getTime() * 40 / 100);
        entity.recipe.setEnergyRequired(entity.recipe.getEnergyRequired() * 160 / 100);
      } else if (entity instanceof NetheriteFurnaceEntity) {
        entity.recipe.setTime(entity.recipe.getTime() * 10 / 100);
        entity.recipe.setEnergyRequired(entity.recipe.getEnergyRequired() * 190 / 100);
      }
      ItemStack slot0 = inventory.getItem(0);
      ItemStack slot1 = inventory.getItem(1);
      int energyRequired = entity.recipe.getEnergyRequired();
      if (slot0.getCount() > 0 && slot0.getItem() instanceof SpeedUpgrade upgrade) {
        double speedModifier = slot0.getCount() * upgrade.getModifier();
        entity.recipe.setTime(entity.recipe.getTime() - (int) Math.floor(entity.recipe.getTime() * speedModifier));
        energyRequired += (int) Math.floor(entity.recipe.getEnergyRequired() * 5 * speedModifier);
      }
      if (slot1.getCount() > 0 && slot1.getItem() instanceof EnergyUpgrade upgrade) {
        double energyModifier = Math.min(slot0.getCount(), slot1.getCount()) * upgrade.getModifier();
        energyRequired -= (int) Math.floor(entity.recipe.getEnergyRequired() * energyModifier);
      }
      entity.recipe.setEnergyRequired(energyRequired);
      entity.recipe.modify();
      entity.progressStorage.setMaxProgress(entity.recipe.getTime());
      DegrassiLogger.INSTANCE.info(entity.recipe);
    }

    return can;
  }

  public static void craftItem(@NotNull FurnaceEntity entity) {
    entity.itemHandler.extractItem(2, 1, false);
    entity.itemHandler.setStackInSlot(3, new ItemStack(
      entity.recipe.getResultItem().getItem(),
      entity.itemHandler.getStackInSlot(3).getCount() + 1
    ));

    entity.xp.addXp(entity.recipe.getExperience());

    entity.resetProgress();
  }

  public static void init() {
    Level level = Objects.requireNonNull(Minecraft.getInstance().level);
    List<SmeltingRecipe> smelting = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING);
    List<BlastingRecipe> blasting = level.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING);
    List<SmokingRecipe> smoking = level.getRecipeManager().getAllRecipesFor(RecipeType.SMOKING);
    List<FurnaceRecipe> furnaceRecipes = level.getRecipeManager().getAllRecipesFor(RecipeRegistry.FURNACE_RECIPE_TYPE.get());
    List<AbstractCookingRecipe> cookingRecipes = new LinkedList<>(blasting);
    smoking.forEach(recipe -> {
      AtomicBoolean has = new AtomicBoolean(false);
      cookingRecipes.forEach(recipe2 -> {
        if (recipe2.getIngredients().get(0).test(recipe.getIngredients().get(0).getItems()[0])) has.set(true);
      });
      if (!has.get()) cookingRecipes.add(recipe);
    });
    smelting.forEach(recipe -> {
      AtomicBoolean has = new AtomicBoolean(false);
      cookingRecipes.forEach(recipe2 -> {
        if (recipe2.getIngredients().get(0).test(recipe.getIngredients().get(0).getItems()[0])) has.set(true);
      });
      if (!has.get()) cookingRecipes.add(recipe);
    });
    cookingRecipes.forEach(recipe -> {
      if (recipe == null) return;
      AtomicBoolean has = new AtomicBoolean(false);
      recipes.forEach(r -> {
        if (r.getId().toString().equals(recipe.getId().toString())) has.set(true);
      });
      if (has.get()) return;
      FurnaceRecipe r = new FurnaceRecipe(recipe.getId(), recipe.getResultItem(), recipe.getIngredients(), recipe.getCookingTime(), recipe.getExperience());
      recipes.add(r);
      recipesMap.put(recipe.getId(), r);
    });
    furnaceRecipes.forEach(recipe -> {
      if (recipe == null) return;
      AtomicBoolean has = new AtomicBoolean(false);
      recipes.forEach(r -> {
        if (r.getId().toString().equals(recipe.getId().toString())) has.set(true);
      });
      if (has.get()) return;
      recipes.add(recipe);
      recipesMap.put(recipe.getId(), recipe);
    });
  }
}
