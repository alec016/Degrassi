package es.degrassi.forge.init.recipe.recipes;

import es.degrassi.forge.init.entity.*;
import es.degrassi.forge.init.entity.type.*;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.recipe.helpers.*;
import es.degrassi.forge.init.registration.RecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.*;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MelterRecipe implements IDegrassiRecipe {
  private final NonNullList<Ingredient> recipeItems;
  private int time;
  private int energyRequired;
  private final FluidStack output;
  private final ResourceLocation id;

  boolean modified;
  private boolean inProgress = false;

  public MelterRecipe(
    ResourceLocation id,
    int time,
    int energyRequired,
    NonNullList<Ingredient> ingredients,
    FluidStack output
  ) {
    this.id = id;
    this.output = output;
    this.recipeItems = ingredients;
    this.time = time;
    this.energyRequired = energyRequired;
  }

  @Override
  public @NotNull NonNullList<Ingredient> getIngredients() {
    return recipeItems;
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
    return energyRequired;
  }

  @Override
  public boolean matches(@NotNull SimpleContainer container, @NotNull Level level) {
    if (level.isClientSide()) return false;

    return recipeItems.get(0).test(container.getItem(2));
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

  public FluidStack getFluid() {
    return output;
  }

  @Override
  public @NotNull ResourceLocation getId() {
    return id;
  }

  @Override
  public @NotNull RecipeSerializer<?> getSerializer() {
    return RecipeRegistry.MELTER_SERIALIZER.get();
  }

  @Override
  public @NotNull RecipeType<?> getType() {
    return RecipeRegistry.MELTER_RECIPE_TYPE.get();
  }

  public boolean isModified() {
    return modified;
  }

  public void modify() {
    this.modified = true;
  }

  public MelterRecipe copy() {
    return new MelterRecipe(id, time, energyRequired, recipeItems, output);
  }

  @Override
  public void setTime(int i) {
    this.time = i;
  }

  @Override
  public void setEnergyRequired(int i) {
    this.energyRequired = i;
  }

  @Override
  public boolean isInProgress() {
    return inProgress;
  }

  @Override
  public void startProgress() {
    inProgress = true;
  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void startProcess(T entity) {
    RecipeHelpers.MELTER.startProcess((MelterEntity) entity);
  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void tick(T entity) {
    RecipeHelpers.MELTER.tickProcess((MelterEntity) entity);
  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void endProcess(T entity) {
    RecipeHelpers.MELTER.endProcess((MelterEntity) entity);
  }

  @Override
  public String toString() {
    return "MelterRecipe: " + GsonHelper.parse(
      "{" +
        "\n\t\"id\": \"" + this.id + "\"" +
        ",\n\t\"input\": \"" + this.recipeItems.stream().toList().stream().map(ingredient -> ingredient.getItems()[0].getDisplayName().getString()).toList() + "\"" +
        ",\n\t\"output\": \"" + this.output.getDisplayName().getString() + "\"" +
        ",\n\t\"time\": " + this.time +
        ",\n\t\"energy\": " + this.energyRequired +
        "\n}"
    );
  }
}
