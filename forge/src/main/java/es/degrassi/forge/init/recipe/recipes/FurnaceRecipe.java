package es.degrassi.forge.init.recipe.recipes;

import com.google.gson.JsonObject;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.registration.RecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import java.util.List;

@SuppressWarnings("unused")
public class FurnaceRecipe implements IDegrassiRecipe {
  private final ResourceLocation id;
  private final ItemStack output;
  private final NonNullList<Ingredient> recipeItems;
  private int time;
  private int energyRequired;
  private float xp;
  private boolean modified = false;
  private boolean showInJei = true;

  public FurnaceRecipe (
    ResourceLocation id,
    ItemStack output,
    NonNullList<Ingredient> ingredients,
    int time,
    float xp
  ) {
    this(id, output, ingredients, time, 10, xp);
  }

  public FurnaceRecipe (
    ResourceLocation id,
    ItemStack output,
    NonNullList<Ingredient> ingredients,
    int time,
    int energyRequired,
    float xp
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

  public float getExperience() {
    return this.xp;
  }

  public void setExperience(float xp) {
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
  public String toString() {
    List<Ingredient> list = this.recipeItems.stream().toList();
    List<String> ing = list.stream().map(ingredient -> ingredient.getItems()[0].getDisplayName().getString()).toList();
    String array = ing.toString();
    JsonObject json = GsonHelper.parse(
      "{" +
        "\n\t\"id\": \"" + id.getNamespace() + ":" + id.getPath() + "\"" +
        ",\n\t\"ingredients\": \"" + array + "\"" +
        ",\n\t\"output\": \"" + output.getDisplayName().getString() + "\"" +
        ",\n\t\"time\": " + time +
        ",\n\t\"energy\": " + energyRequired +
        ",\n\t\"xp\": " + xp +
        "\n}"
    );
    return json.toString();
  }
}
