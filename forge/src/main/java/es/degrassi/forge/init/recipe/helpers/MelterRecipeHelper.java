package es.degrassi.forge.init.recipe.helpers;

import es.degrassi.forge.init.entity.melter.MelterEntity;
import es.degrassi.forge.init.entity.renderer.LerpedFloat;
import es.degrassi.forge.init.item.upgrade.types.IMelterUpgrade;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
import es.degrassi.forge.init.registration.RecipeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MelterRecipeHelper extends RecipeHelper<MelterRecipe, MelterEntity> {
  @Override
  public boolean hasRecipe(@NotNull MelterEntity entity) {
    Level level = entity.getLevel();
    if (level == null) return false;

    SimpleContainer inventory = new SimpleContainer(entity.getItemHandler().getSlots());
    for (int i = 0; i < entity.getItemHandler().getSlots(); i++) {
      inventory.setItem(i, entity.getItemHandler().getStackInSlot(i));
    }

    recipes.forEach(recipe -> {
      if (recipe == null) return;
      if (recipe.getIngredients().get(0).test(inventory.getItem(2))) entity.setRecipe(recipe.copy());
    });

    if (entity.getRecipe() == null) return false;

    if (!entity.getRecipe().isModified()) modifyRecipe(entity, inventory);

    return canInsertAmountIntoOutputSlot(entity.getFluidStorage()) && hasEnoughEnergy(entity);
  }

  @Override
  public void craftItem(@NotNull MelterEntity entity) {
    MelterRecipe recipe = (MelterRecipe) entity.getRecipe();
    entity.getEnergyStorage().extractEnergy(recipe.getEnergyRequired(), false);
    entity.getItemHandler().extractItem(2, 1, false);
    entity.getFluidStorage().fill(recipe.getFluid(), IFluidHandler.FluidAction.EXECUTE);
    entity.resetProgress();
    if (entity.getFluidLevel() != null) entity.getFluidLevel().tickChaser();
    else entity.setFluidLevel(LerpedFloat.linear().startWithValue(entity.getFillState()));
  }

  @Override
  public void init() {
    Level level = Objects.requireNonNull(Minecraft.getInstance().level);
    List<MelterRecipe> melterRecipes = level.getRecipeManager().getAllRecipesFor(RecipeRegistry.MELTER_RECIPE_TYPE.get());
    melterRecipes.forEach(recipe -> {
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

  private void modifyRecipe(@NotNull MelterEntity entity, @NotNull SimpleContainer inventory) {
    ItemStack slot0 = inventory.getItem(0);
    ItemStack slot1 = inventory.getItem(1);

    int energyRequired = entity.getRecipe().getEnergyRequired();
    for(int i = 0; i < inventory.getContainerSize(); i++) {
      ItemStack slot = inventory.getItem(i);
      if (slot.getCount() > 0 && slot.getItem() instanceof IMelterUpgrade upgrade) {
        switch(upgrade.getUpgradeType()) {
          case SPEED -> {
            double speedModifier = slot0.getCount() * upgrade.getModifier();
            entity.getRecipe().setTime(Math.max(1, entity.getRecipe().getTime() - (int) Math.floor(entity.getRecipe().getTime() * speedModifier)));
            energyRequired += (int) Math.floor(entity.getRecipe().getEnergyRequired() * 5 * speedModifier);
          }
          case ENERGY -> {
            double energyModifier = Math.min(slot0.getCount(), slot1.getCount()) * upgrade.getModifier();
            energyRequired -= (int) Math.floor(entity.getRecipe().getEnergyRequired() * energyModifier);
          }
        }
      }
    }

    entity.getRecipe().setEnergyRequired(Math.max(1, energyRequired));
    entity.getRecipe().modify();
    entity.getProgressStorage().setMaxProgress(entity.getRecipe().getTime());
  }
}
