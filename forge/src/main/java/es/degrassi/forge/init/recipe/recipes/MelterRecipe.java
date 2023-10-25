package es.degrassi.forge.init.recipe.recipes;

import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class MelterRecipe implements IDegrassiRecipe {
  @Override
  public int getTime() {
    return 0;
  }

  @Override
  public boolean showInJei() {
    return true;
  }

  @Override
  public int getEnergyRequired() {
    return 0;
  }

  @Override
  public boolean matches(SimpleContainer container, Level level) {
    return false;
  }

  @Override
  public ItemStack assemble(SimpleContainer container) {
    return null;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return false;
  }

  @Override
  public ItemStack getResultItem() {
    return null;
  }

  @Override
  public ResourceLocation getId() {
    return null;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public RecipeType<?> getType() {
    return null;
  }
}
