package es.degrassi.forge.init.recipe;

import es.degrassi.forge.init.entity.*;
import es.degrassi.forge.init.entity.type.*;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;

public interface IDegrassiRecipe extends Recipe<SimpleContainer> {

  int getTime();

  boolean showInJei();

  int getEnergyRequired();

  boolean isModified();

  void modify();

  IDegrassiRecipe copy();

  void setTime(int i);

  void setEnergyRequired(int i);

  boolean isInProgress();

  void startProgress();

  <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void startProcess(T entity);
  <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void tick(T entity);
  <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void endProcess(T entity);

  default FluidStack getFluid() {
    return FluidStack.EMPTY;
  }
}
