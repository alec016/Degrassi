package es.degrassi.forge.init.recipe.helpers;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.entity.type.IEnergyEntity;
import es.degrassi.forge.init.entity.type.IRecipeEntity;
import es.degrassi.forge.init.recipe.*;
import es.degrassi.forge.init.gui.component.EnergyComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class RecipeHelper<T extends IDegrassiRecipe, E extends BaseEntity & IEnergyEntity & IRecipeEntity<T>> {

  public final NonNullList<T> recipes = NonNullList.create();
  public final Map<ResourceLocation, T> recipesMap = new HashMap<>();

  public abstract boolean hasRecipe(@NotNull E entity);

  public abstract void endProcess(@NotNull E entity);
  public abstract void startProcess(@NotNull E entity);
  public abstract void tickProcess(@NotNull E entity);

  public void init() {
    this.recipes.clear();
    this.recipesMap.clear();
  }


  public void extractEnergy(@NotNull E entity) {
    entity.getEnergyStorage().setEnergy(entity.getEnergyStorage().getEnergyStored() - entity.getRecipe().getEnergyRequired());
  }

  public void insertEnergy(@NotNull E entity) {
    entity.getEnergyStorage().setEnergy(entity.getEnergyStorage().getEnergyStored() + entity.getRecipe().getEnergyRequired());
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

  public boolean canInsertAmountIntoOutputSlot(@NotNull EnergyComponent inventory, int energyProduced) {
    return inventory.getEnergyStored() + energyProduced <= inventory.getMaxEnergyStored();
  }

  public boolean canInsertAmountIntoOutputSlot(@NotNull FluidTank storage) {
    return storage.getCapacity() > storage.getFluidAmount();
  }

  public boolean hasEnoughFluid(@NotNull FluidTank storage, @NotNull E entity) {
    return entity.getRecipe().getFluid().getAmount() <= storage.getFluidAmount();
  }

  public void extractFluid(@NotNull FluidTank storage, @NotNull E entity) {
    FluidStack fluid = storage.getFluid();
    fluid.setAmount(fluid.getAmount() - entity.getRecipe().getFluid().getAmount());
    storage.setFluid(fluid);
  }


  public boolean hasSameFluidInTank(@NotNull FluidTank storage, @NotNull E entity) {
    return entity.getRecipe().getFluid().equals(storage.getFluid());
  }
}
