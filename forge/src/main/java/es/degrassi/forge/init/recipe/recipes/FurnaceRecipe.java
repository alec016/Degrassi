package es.degrassi.forge.init.recipe.recipes;

import es.degrassi.forge.init.entity.*;
import es.degrassi.forge.init.entity.type.*;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.recipe.helpers.*;
import es.degrassi.forge.init.registration.RecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class FurnaceRecipe implements IDegrassiRecipe {
  private final ResourceLocation id;
  private final ItemStack output;
  private final NonNullList<Ingredient> recipeItems;
  private int time;
  private int energyRequired;
  private int xp;
  private boolean modified = false;
  private boolean showInJei = true;
  private boolean inProgress = false;

  public FurnaceRecipe (
    ResourceLocation id,
    ItemStack output,
    NonNullList<Ingredient> ingredients,
    int time,
    int xp
  ) {
    this(id, output, ingredients, time, 10, xp);
  }

  public FurnaceRecipe (
    ResourceLocation id,
    ItemStack output,
    NonNullList<Ingredient> ingredients,
    int time,
    int energyRequired,
    int xp
  ) {
    this.id = id;
    this.output = output;
    this.recipeItems = ingredients;
    this.time = time;
    this.energyRequired = energyRequired;
    this.xp = xp;
  }

  @Override
  public boolean matches(@NotNull SimpleContainer pContainer, @NotNull Level pLevel) {
    if (pLevel.isClientSide()) return false;

    return recipeItems.get(0).test(pContainer.getItem(2));
  }

  @Override
  public @NotNull NonNullList<Ingredient> getIngredients() {
    return recipeItems;
  }

  @Override
  public @NotNull ItemStack assemble(@NotNull SimpleContainer pContainer) {
    return output;
  }

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }

  public @NotNull ItemStack getResultItem() {
    return output.copy();
  }

  @Override
  public @NotNull ResourceLocation getId() {
    return id;
  }

  @Override
  public @NotNull RecipeSerializer<?> getSerializer() {
    return RecipeRegistry.FURNACE_SERIALIZER.get();
  }

  @Override
  public @NotNull RecipeType<?> getType() {
    return RecipeRegistry.FURNACE_RECIPE_TYPE.get();
  }

  public int getTime() {
    return time;
  }

  @Override
  public boolean showInJei() {
    return showInJei;
  }

  public void showInJei(boolean show) {
    this.showInJei = show;
  }

  public int getEnergyRequired() {
    return this.energyRequired;
  }

  public void setEnergyRequired(int energyRequired) {
    this.energyRequired = energyRequired;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getExperience() {
    return this.xp;
  }

  public void setExperience(int xp) {
    this.xp = xp;
  }

  public boolean isModified() {
    return modified;
  }

  public void modify() {
    this.modified = true;
  }

  public FurnaceRecipe copy() {
    return new FurnaceRecipe(id, output, recipeItems, time, energyRequired, xp);
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
    RecipeHelpers.FURNACE.startProcess((FurnaceEntity) entity);
  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void tick(T entity) {
    RecipeHelpers.FURNACE.tickProcess((FurnaceEntity) entity);
  }

  @Override
  public <T extends BaseEntity & IProgressEntity & IRecipeEntity<?>> void endProcess(T entity) {
    RecipeHelpers.FURNACE.endProcess((FurnaceEntity) entity);
  }

  @Override
  public String toString() {
    return "FurnaceRecipe: " + GsonHelper.parse(
      "{" +
        "\n\t\"id\": \"" + this.id + "\"" +
        ",\n\t\"ingredients\": \"" + this.recipeItems.stream().toList().stream().map(ingredient -> ingredient.getItems()[0].getDisplayName().getString()).toList() + "\"" +
        ",\n\t\"output\": \"" + this.output.getDisplayName().getString() + "\"" +
        ",\n\t\"time\": " + this.time +
        ",\n\t\"energy\": " + this.energyRequired +
        ",\n\t\"xp\": " + this.xp +
        "\n}"
    );
  }
}
