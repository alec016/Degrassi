package es.degrassi.forge.init.recipe.recipes.generators;

import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.registration.RecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class JewelryGeneratorRecipe implements IDegrassiRecipe {
  private final ResourceLocation id;
  private final NonNullList<Ingredient> recipeItems;
  private int time;
  private int energyProduced;
  private boolean modified = false;

  public JewelryGeneratorRecipe (
    ResourceLocation id,
    NonNullList<Ingredient> ingredients,
    int energyProduced,
    int time
  ) {
    this.id = id;
    this.recipeItems = ingredients;
    this.time = time;
    this.energyProduced = energyProduced;
  }

  @Override
  public int getTime() {
    return time;
  }

  @Override
  public boolean showInJei() {
    return true;
  }

  @Override
  public int getEnergyRequired() {
    return energyProduced;
  }

  @Override
  public boolean isModified() {
    return modified;
  }

  @Override
  public void modify() {
    modified = true;
  }

  @Override
  public IDegrassiRecipe copy() {
    return new JewelryGeneratorRecipe(id, getIngredients(), energyProduced, time);
  }

  @Override
  public void setTime(int i) {
    this.time = i;
  }

  @Override
  public void setEnergyRequired(int i) {
    energyProduced = i;
  }

  @Override
  public boolean matches(@NotNull SimpleContainer container, @NotNull Level level) {
    if (level.isClientSide()) return false;
    return recipeItems.get(0).test(container.getItem(3));
  }

  @Override
  public @NotNull ItemStack assemble(@NotNull SimpleContainer container) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  @Override
  public @NotNull ItemStack getResultItem() {
    return ItemStack.EMPTY;
  }

  @Override
  public @NotNull ResourceLocation getId() {
    return id;
  }

  @Override
  public @NotNull RecipeSerializer<?> getSerializer() {
    return RecipeRegistry.JEWELRY_GENERATOR_SERIALIZER.get();
  }

  @Override
  public @NotNull RecipeType<?> getType() {
    return RecipeRegistry.JEWELRY_GENERATOR_RECIPE_TYPE.get();
  }
}
