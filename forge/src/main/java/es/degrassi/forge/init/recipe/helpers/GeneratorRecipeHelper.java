package es.degrassi.forge.init.recipe.helpers;

import com.google.common.collect.*;
import es.degrassi.forge.init.block.generators.GeneratorBlock;
import es.degrassi.forge.init.entity.*;
import es.degrassi.forge.init.entity.generators.*;
import es.degrassi.forge.init.item.upgrade.UpgradeUpgradeType;
import es.degrassi.forge.init.item.upgrade.types.IGeneratorUpgrade;
import es.degrassi.forge.init.recipe.recipes.*;
import es.degrassi.forge.init.registration.*;
import es.degrassi.forge.util.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import net.minecraft.client.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.NotNull;

public class GeneratorRecipeHelper<E extends GeneratorEntity<E, GeneratorBlock>> extends RecipeHelper<GeneratorRecipe, E> {
  @Override
  public boolean hasRecipe(@NotNull E entity) {
    Level level = entity.getLevel();
    if (level == null || level.isClientSide()) return false;

    SimpleContainer inventory = new SimpleContainer(entity.getItemHandler().getSlots());
    for (int i = 0; i < entity.getItemHandler().getSlots(); i++) {
      inventory.setItem(i, entity.getItemHandler().getStackInSlot(i));
    }

    getRecipesForMachine(entity.getDelegate()).forEach(recipe -> {
      if (recipe == null) return;
      if (recipe.matches(inventory, level))
        entity.setRecipe(recipe.copy());
    });

    if (entity.getRecipe() == null) return false;

    if (!entity.getRecipe().isModified()) modifyRecipe(entity, inventory);

    return canInsertAmountIntoOutputSlot(entity.getEnergyStorage(), entity.getRecipe().getEnergyRequired());
  }

  @Override
  public void startProcess(@NotNull E entity) {
    GeneratorRecipe recipe = entity.getRecipe();
    recipe.startProgress();
    entity.getItemHandler().extractItem(2, 1, false);
    entity.getProgressStorage().increment(false);
  }

  @Override
  public void tickProcess(@NotNull E entity) {
    insertEnergy(entity);
    entity.getProgressStorage().increment(false);
  }

  @Override
  public void endProcess(@NotNull E entity) {
    entity.resetProgress();
  }

  public List<GeneratorRecipe> getRecipesForMachine(GeneratorBlock block) {
    return recipes.stream().filter(recipe -> recipe.canUseRecipe(block.kjs$getId())).toList();
  }

  @Override
  public void init() {
    super.init();
    Level level = Objects.requireNonNull(Minecraft.getInstance().level);
    List<GeneratorRecipe> generatorRecipes = level.getRecipeManager().getAllRecipesFor(RecipeRegistry.GENERATOR_RECIPE_TYPE.get());

    DegrassiLogger.INSTANCE.info("GeneratorRecipeHelper$count: {}", generatorRecipes.size());
    generatorRecipes.forEach(recipe -> {
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

  private void modifyRecipe(@NotNull E entity, @NotNull SimpleContainer inventory) {
    ItemStack slot0 = inventory.getItem(0);
    ItemStack slot1 = inventory.getItem(1);

    int energyRequired = entity.getRecipe().getEnergyRequired();
    int transfer = entity.getEnergyStorage().getMaxExtract();
    for(int i = 0; i < inventory.getContainerSize(); i++) {
      ItemStack slot = inventory.getItem(i);
      if (slot.getCount() > 0 && slot.getItem() instanceof IGeneratorUpgrade upgrade) {
        if (Objects.requireNonNull(upgrade.getUpgradeType()) == UpgradeUpgradeType.GENERATION) {
          double energyModifier = slot0.getCount() * upgrade.getModifier();
          energyRequired += (int) Math.floor(entity.getRecipe().getEnergyRequired() * energyModifier);
        } else if (Objects.requireNonNull(upgrade.getUpgradeType()) == UpgradeUpgradeType.TRANSFER) {
          double transferModifier = slot1.getCount() * upgrade.getModifier();
          transfer += (int) Math.floor(entity.getEnergyStorage().getMaxExtract() * transferModifier);
        }
      }
    }

    entity.getRecipe().setEnergyRequired(Math.max(1, energyRequired));
    entity.getRecipe().modify();
    entity.setMaxProgress(entity.getRecipe().getTime());
    entity.setTransferRate(transfer);
  }
}
