package es.degrassi.forge.init.recipe.helpers;

import es.degrassi.forge.init.entity.UpgradeMakerEntity;
import es.degrassi.forge.init.item.upgrade.SpeedUpgrade;
import es.degrassi.forge.init.item.upgrade.types.IUpgradeMakerUpgrade;
import es.degrassi.forge.init.recipe.recipes.UpgradeMakerRecipe;
import es.degrassi.forge.init.registration.RecipeRegistry;
import es.degrassi.forge.util.DegrassiLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpgradeMakerRecipeHelper extends RecipeHelper<UpgradeMakerRecipe, UpgradeMakerEntity> {
  @Override
  public boolean hasRecipe(@NotNull UpgradeMakerEntity entity) {
    Level level = entity.getLevel();
    if (level == null || level.isClientSide()) return false;

    SimpleContainer inventory = new SimpleContainer(entity.getItemHandler().getSlots());
    for (int i = 0; i < entity.getItemHandler().getSlots(); i++) {
      inventory.setItem(i, entity.getItemHandler().getStackInSlot(i));
    }

    recipes.forEach(recipe -> {
      if (recipe == null) return;
      if (
        recipe.matches(inventory, level)
      ) entity.setRecipe(recipe.copy());
    });

    if (entity.getRecipe() == null) return false;

    if (!entity.getRecipe().isModified()) modifyRecipe(entity, inventory);

    return
      canInsertItemIntoOutputSlot(inventory, entity.getRecipe().getResultItem(), 4) &&
      canInsertAmountIntoOutputSlot(inventory, 4) &&
      hasSameFluidInTank(entity.getFluidStorage(), entity) &&
      hasEnoughFluid(entity.getFluidStorage(), entity) &&
      hasEnoughEnergy(entity);
  }

  @Override
  public void tickProcess(@NotNull UpgradeMakerEntity entity) {
    extractEnergy(entity);
    entity.getProgressStorage().increment(false);
  }

  @Override
  public void startProcess(@NotNull UpgradeMakerEntity entity) {
    UpgradeMakerRecipe recipe = entity.getRecipe();
    recipe.startProgress();
    entity.getItemHandler().extractItem(2, 1, false);
    entity.getItemHandler().extractItem(3, 1, false);
    extractFluid(entity.getFluidStorage(), entity);
    entity.getProgressStorage().increment(false);
  }

  @Override
  public void endProcess(@NotNull UpgradeMakerEntity entity) {
    UpgradeMakerRecipe recipe = entity.getRecipe();
    entity.getItemHandler().setStackInSlot(4, new ItemStack(
      recipe.getResultItem().getItem(),
      entity.getItemHandler().getStackInSlot(4).getCount() + 1
    ));
    entity.resetProgress();
  }

  @Override
  public void init() {
    super.init();
    Level level = Objects.requireNonNull(Minecraft.getInstance().level);
    List<UpgradeMakerRecipe> upgradeMakerRecipes = level.getRecipeManager().getAllRecipesFor(RecipeRegistry.UPGRADE_MAKER_RECIPE_TYPE.get());
    DegrassiLogger.INSTANCE.info("UpgradeMakerRecipeHelper$count: {}", upgradeMakerRecipes.size());
    upgradeMakerRecipes.forEach(recipe -> {
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

  private void modifyRecipe(@NotNull UpgradeMakerEntity entity, @NotNull SimpleContainer inventory) {
    ItemStack slot0 = inventory.getItem(0);
    ItemStack slot1 = inventory.getItem(1);

    int energyRequired = entity.getRecipe().getEnergyRequired();
    int timeRequired = entity.getRecipe().getTime();
    for(int i = 0; i < inventory.getContainerSize(); i++) {
      if (i >= 2) continue;
      ItemStack slot = inventory.getItem(i);
      if (slot.getCount() > 0 && slot.getItem() instanceof IUpgradeMakerUpgrade upgrade) {
        switch(upgrade.getUpgradeType()) {
          case SPEED -> {
            SpeedUpgrade up = (SpeedUpgrade) upgrade;
            double speedModifier = slot0.getCount() * up.getModifier();
            timeRequired -= (int) Math.floor(entity.getRecipe().getTime() * speedModifier);
            energyRequired += (int) Math.floor(entity.getRecipe().getEnergyRequired() * up.getEnergyValue() * speedModifier);
          }
          case ENERGY -> {
            double energyModifier = Math.min(slot0.getCount(), slot1.getCount()) * upgrade.getModifier();
            energyRequired -= (int) Math.floor(entity.getRecipe().getEnergyRequired() * energyModifier);
          }
        }
      }
    }

    entity.getRecipe().setEnergyRequired(Math.max(1, energyRequired));
    entity.getRecipe().setTime(Math.max(1, timeRequired));
    entity.getRecipe().modify();
    entity.getProgressStorage().setMaxProgress(entity.getRecipe().getTime());
  }
}
