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

public class UpgradeMakerRecipe implements IDegrassiRecipe {

  private final ResourceLocation id;
  private final NonNullList<Ingredient> recipeItems;
  private int time;
  private int energy;
  private boolean modified = false;
  private boolean inProgress = false;
  private final FluidStack fluidInput;
  private final ItemStack output;

  public UpgradeMakerRecipe(
    ResourceLocation id,
    int time,
    int energy,
    NonNullList<Ingredient> ingredients,
    FluidStack fluidInput,
    ItemStack itemOutput
  ) {
    this.id = id;
    this.time = time;
    this.energy = energy;
    this.recipeItems = ingredients;
    this.fluidInput = fluidInput;
    this.output = itemOutput;
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
    return energy;
  }

  @Override
  public boolean isModified() {
    return modified;
  }

  @Override
  public void modify() {
    this.modified = true;
  }

  @Override
  public UpgradeMakerRecipe copy() {
    return new UpgradeMakerRecipe(id, time, energy, recipeItems, fluidInput, output);
  }

  @Override
  public void setTime(int time) {
    this.time = time;
  }

  @Override
  public void setEnergyRequired(int energy) {
    this.energy = energy;
  }

  @Override
  public boolean matches(@NotNull SimpleContainer container, Level level) {
    if (level.isClientSide()) return false;

    return recipeItems.get(0).test(container.getItem(2)) && recipeItems.get(1).test(container.getItem(3));
  }

  @Override
  public @NotNull ItemStack assemble(@NotNull SimpleContainer container) {
    return output;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  @Override
  public @NotNull ItemStack getResultItem() {
    return output;
  }

  @Override
  public @NotNull ResourceLocation getId() {
    return id;
  }

  @Override
  public @NotNull RecipeSerializer<?> getSerializer() {
    return RecipeRegistry.UPGRADE_MAKER_SERIALIZER.get();
  }

  @Override
  public @NotNull RecipeType<?> getType() {
    return RecipeRegistry.UPGRADE_MAKER_RECIPE_TYPE.get();
  }

  @Override
  public FluidStack getFluid() {
    return fluidInput;
  }

  @Override
  public @NotNull NonNullList<Ingredient> getIngredients() {
    return recipeItems;
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
    RecipeHelpers.UPGRADE_MAKER.startProcess((UpgradeMakerEntity) entity);
  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void tick(T entity) {
    RecipeHelpers.UPGRADE_MAKER.tickProcess((UpgradeMakerEntity) entity);

  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void endProcess(T entity) {
    RecipeHelpers.UPGRADE_MAKER.endProcess((UpgradeMakerEntity) entity);
  }

  @Override
  public String toString() {
    return "UpgradeMakerRecipe: " + GsonHelper.parse(
      "{" +
        "\n\t\"id\": \"" + this.id + "\"" +
        ",\n\t\"inputs\": \"" + this.recipeItems.stream().toList().stream().map(ingredient -> ingredient.getItems()[0].getDisplayName().getString()).toList() + "\"" +
        ",\n\t\"fluid\": \"" + fluidInput.getDisplayName().getString() + "\"" +
        ",\n\t\"output\": \"" + this.output.getDisplayName().getString() + "\"" +
        ",\n\t\"time\": " + this.time +
        ",\n\t\"energy\": " + this.energy +
        "\n}"
    );
  }
}
