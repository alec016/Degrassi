package es.degrassi.forge.init.recipe;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.Recipe;

public interface IDegrassiRecipe extends Recipe<SimpleContainer> {

  int getTime();

  boolean showInJei();
}
