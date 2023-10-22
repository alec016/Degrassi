package es.degrassi.comon.init.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.Recipe;

public interface IDegrassiRecipe extends Recipe<SimpleContainer> {

  int getTime();
}
