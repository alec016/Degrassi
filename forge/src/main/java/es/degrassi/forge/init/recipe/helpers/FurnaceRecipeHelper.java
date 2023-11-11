package es.degrassi.forge.init.recipe.helpers;

import es.degrassi.forge.init.entity.furnace.*;
import es.degrassi.forge.init.item.upgrade.SpeedUpgrade;
import es.degrassi.forge.init.item.upgrade.types.IFurnaceUpgrade;
import es.degrassi.forge.init.recipe.recipes.FurnaceRecipe;
import es.degrassi.forge.init.registration.RecipeRegistry;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.util.DegrassiLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FurnaceRecipeHelper extends RecipeHelper<FurnaceRecipe, FurnaceEntity> {

  @Override
  public boolean hasRecipe(@NotNull FurnaceEntity entity) {
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

    if (!entity.recipe.isModified()) modifyRecipe(entity, inventory);

    return canInsertAmountIntoOutputSlot(inventory, 3) && canInsertItemIntoOutputSlot(inventory, entity.recipe.getResultItem(), 3) && hasEnoughEnergy(entity);
  }

  @Override
  public void craftItem(@NotNull FurnaceEntity entity) {
    entity.itemHandler.extractItem(2, 1, false);
    entity.itemHandler.setStackInSlot(3, new ItemStack(
      entity.recipe.getResultItem().getItem(),
      entity.itemHandler.getStackInSlot(3).getCount() + 1
    ));

    entity.xp.addXp(entity.recipe.getExperience());

    entity.resetProgress();
  }

  @Override
  public void init() {
    super.init();
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
      if (!has.get()) {
        FurnaceRecipe r = new FurnaceRecipe(recipe.getId(), recipe.getResultItem(), recipe.getIngredients(), recipe.getCookingTime(), recipe.getExperience());
        recipes.add(r);
        recipesMap.put(recipe.getId(), r);
      }
    });
    DegrassiLogger.INSTANCE.info("FurnaceRecipeHelper");
    furnaceRecipes.forEach(recipe -> {
      DegrassiLogger.INSTANCE.info(recipe);
      if (recipe == null) return;
      AtomicBoolean has = new AtomicBoolean(false);
      recipes.forEach(r -> {
        if (r.getId().toString().equals(recipe.getId().toString())) has.set(true);
      });
      if (!has.get()) {
        recipes.add(recipe);
        recipesMap.put(recipe.getId(), recipe);
      }
    });
  }

  private void modifyRecipe(FurnaceEntity entity, SimpleContainer inventory) {
    if (entity instanceof IronFurnaceEntity) {
      entity.recipe.setTime(entity.recipe.getTime() * (100 - DegrassiConfig.get().furnaceConfig.iron_furnace_speed) / 100);
      entity.recipe.setEnergyRequired(entity.recipe.getEnergyRequired() * (100 + DegrassiConfig.get().furnaceConfig.iron_furnace_energy) / 100);
      entity.recipe.setExperience(entity.recipe.getExperience() * (100 + DegrassiConfig.get().furnaceConfig.iron_furnace_xp) / 100);
    } else if(entity instanceof GoldFurnaceEntity) {
      entity.recipe.setTime(entity.recipe.getTime() * (100 - DegrassiConfig.get().furnaceConfig.gold_furnace_speed) / 100);
      entity.recipe.setEnergyRequired(entity.recipe.getEnergyRequired() * (100 + DegrassiConfig.get().furnaceConfig.gold_furnace_energy) / 100);
      entity.recipe.setExperience(entity.recipe.getExperience() * (100 + DegrassiConfig.get().furnaceConfig.gold_furnace_xp) / 100);
    } else if (entity instanceof DiamondFurnaceEntity) {
      entity.recipe.setTime(entity.recipe.getTime() * (100 - DegrassiConfig.get().furnaceConfig.diamond_furnace_speed) / 100);
      entity.recipe.setEnergyRequired(entity.recipe.getEnergyRequired() * (100 + DegrassiConfig.get().furnaceConfig.diamond_furnace_energy) / 100);
      entity.recipe.setExperience(entity.recipe.getExperience() * (100 + DegrassiConfig.get().furnaceConfig.diamond_furnace_xp) / 100);
    } else if (entity instanceof EmeraldFurnaceEntity) {
      entity.recipe.setTime(entity.recipe.getTime() * (100 - DegrassiConfig.get().furnaceConfig.emerald_furnace_speed) / 100);
      entity.recipe.setEnergyRequired(entity.recipe.getEnergyRequired() * (100 + DegrassiConfig.get().furnaceConfig.emerald_furnace_energy) / 100);
      entity.recipe.setExperience(entity.recipe.getExperience() * (100 + DegrassiConfig.get().furnaceConfig.emerald_furnace_xp) / 100);
    } else if (entity instanceof NetheriteFurnaceEntity) {
      entity.recipe.setTime(entity.recipe.getTime() * (100 - DegrassiConfig.get().furnaceConfig.netherite_furnace_speed) / 100);
      entity.recipe.setEnergyRequired(entity.recipe.getEnergyRequired() * (100 + DegrassiConfig.get().furnaceConfig.netherite_furnace_energy) / 100);
      entity.recipe.setExperience(entity.recipe.getExperience() * (100 + DegrassiConfig.get().furnaceConfig.netherite_furnace_xp) / 100);
    }

    ItemStack slot0 = inventory.getItem(0);
    ItemStack slot1 = inventory.getItem(1);

    int energyRequired = entity.recipe.getEnergyRequired();
    int timeRequired = entity.recipe.getTime();
    for(int i = 0; i < inventory.getContainerSize(); i++) {
      ItemStack slot = inventory.getItem(i);
      if (slot.getCount() > 0 && slot.getItem() instanceof IFurnaceUpgrade upgrade) {
        switch(upgrade.getUpgradeType()) {
          case SPEED -> {
            SpeedUpgrade up = (SpeedUpgrade) upgrade;
            double speedModifier = slot0.getCount() * up.getModifier();
            timeRequired -= (int) Math.max(1, entity.recipe.getTime() * speedModifier);
            energyRequired += (int) Math.floor(entity.recipe.getEnergyRequired() * up.getEnergyValue() * speedModifier);
          }
          case ENERGY -> {
            double energyModifier = Math.min(slot0.getCount(), slot1.getCount()) * upgrade.getModifier();
            energyRequired -= (int) Math.floor(entity.recipe.getEnergyRequired() * energyModifier);
          }
        }
      }
    }

    entity.recipe.setEnergyRequired(Math.max(1, energyRequired));
    entity.recipe.setTime(Math.max(1, timeRequired));
    entity.recipe.modify();
    entity.progressStorage.setMaxProgress(entity.recipe.getTime());
  }
}
