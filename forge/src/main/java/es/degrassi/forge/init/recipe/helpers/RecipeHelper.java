package es.degrassi.forge.init.recipe.helpers;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.entity.type.IEnergyEntity;
import es.degrassi.forge.init.entity.type.IRecipeEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class RecipeHelper<T extends Recipe<?>, E extends BaseEntity & IEnergyEntity & IRecipeEntity> {
  public final NonNullList<T> recipes = NonNullList.create();
  public final Map<ResourceLocation, T> recipesMap = new HashMap<>();

  public abstract boolean hasRecipe(@NotNull E entity);

  public abstract void craftItem(@NotNull E entity);

  public abstract void init();


  public void extractEnergy(@NotNull E entity) {
    entity.getEnergyStorage().setEnergy(entity.getEnergyStorage().getEnergyStored() - entity.getRecipe().getEnergyRequired());
  }

  public boolean hasEnoughEnergy(@NotNull E entity) {
    return entity.getEnergyStorage().getEnergyStored() >= entity.getRecipe().getEnergyRequired();
  }

  public boolean canInsertItemIntoOutputSlot(@NotNull SimpleContainer inventory, @NotNull ItemStack stack, int slot) {
    return inventory.getItem(slot).getItem() == stack.getItem() || inventory.getItem(slot).isEmpty();
  }

  public boolean canInsertAmountIntoOutputSlot(@NotNull SimpleContainer inventory, int slot) {
    return inventory.getItem(slot).getMaxStackSize() > inventory.getItem(slot).getCount();
  }

  public boolean canInsertAmountIntoOutputSlot(@NotNull FluidTank storage) {
    return storage.getCapacity() > storage.getFluidAmount();
  }
}
