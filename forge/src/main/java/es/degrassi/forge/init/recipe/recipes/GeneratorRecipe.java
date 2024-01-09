package es.degrassi.forge.init.recipe.recipes;

import es.degrassi.forge.init.entity.*;
import es.degrassi.forge.init.entity.generators.*;
import es.degrassi.forge.init.entity.type.*;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.recipe.helpers.*;
import es.degrassi.forge.init.registration.*;
import java.util.*;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.*;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused, unchecked"})
public class GeneratorRecipe implements IDegrassiRecipe {
  protected final ResourceLocation id;
  protected final NonNullList<Ingredient> recipeItems;
  protected int time;
  protected int energyProduced;
  protected boolean modified = false;
  private final List<String> machineIds;
  protected boolean inProgress = false;

  public GeneratorRecipe (
    ResourceLocation id,
    NonNullList<Ingredient> ingredients,
    int energyProduced,
    int time,
    List<String> machineIds
  ) {
    this.id = id;
    this.recipeItems = ingredients;
    this.time = time;
    this.energyProduced = energyProduced;
    this.machineIds = machineIds;
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
    return recipeItems.get(0).test(container.getItem(2));
  }

  public boolean canUseRecipe(String machineId) {
    return !machineIds.stream().filter(id -> id.equals(machineId)).toList().isEmpty();
  }

  public boolean canUseRecipe(ResourceLocation machineId) {
    return canUseRecipe(machineId.toString());
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

  public GeneratorRecipe copy() {
    return new GeneratorRecipe(id, recipeItems, energyProduced, time, machineIds);
  }

  @Override
  public @NotNull RecipeSerializer<?> getSerializer() {
    return RecipeRegistry.GENERATOR_SERIALIZER.get();
  }

  @Override
  public @NotNull RecipeType<?> getType() {
    return RecipeRegistry.GENERATOR_RECIPE_TYPE.get();
  }

  public List<String> getMachineIds() {
    return machineIds;
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
  public @NotNull NonNullList<Ingredient> getIngredients() {
    return recipeItems;
  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void startProcess(T entity) {
    RecipeHelpers.GENERATORS.startProcess((GeneratorEntity<?, ?>) entity);
  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void tick(T entity) {
    RecipeHelpers.GENERATORS.tickProcess((GeneratorEntity<?, ?>) entity);
  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void endProcess(T entity) {
    RecipeHelpers.GENERATORS.endProcess((GeneratorEntity<?, ?>) entity);
  }

  @Override
  public String toString() {
    return "GeneratorRecipe: " + GsonHelper.parse(
      "{" +
        "\n\t\"id\": \"" + this.id + "\"" +
        ",\n\t\"ingredients\": \"" + this.recipeItems.stream().toList().stream().map(ingredient -> ingredient.getItems()[0].getDisplayName().getString()).toList() + "\"" +
        ",\n\t\"time\": " + this.time +
        ",\n\t\"energy\": " + this.energyProduced +
        ", \n\t\"machines\": " + this.machineIds +
        "\n}"
    );
  }
}
