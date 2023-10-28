package es.degrassi.forge.init.recipe;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.Recipe;

public interface IDegrassiRecipe extends Recipe<SimpleContainer> {

  int getTime();

  boolean showInJei();

  int getEnergyRequired();

  boolean isModified();

  void modify();

  IDegrassiRecipe copy();

  void setTime(int i);

  void setEnergyRequired(int i);
}
