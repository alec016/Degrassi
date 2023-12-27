package es.degrassi.forge.init.recipe.recipes.generators;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class GeneratorRecipe implements IDegrassiRecipe {
  public static final ResourceLocation UID = new DegrassiLocation("jewelry_generator");
  protected final ResourceLocation id;
  protected final NonNullList<Ingredient> recipeItems;
  protected int time;
  protected int energyProduced;
  protected boolean modified = false;

  public GeneratorRecipe (
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
}
