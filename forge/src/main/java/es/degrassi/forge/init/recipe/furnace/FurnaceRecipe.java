package es.degrassi.forge.init.recipe.furnace;

import com.google.gson.JsonArray;
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
import java.util.stream.Stream;

public class FurnaceRecipe implements IDegrassiRecipe {
  private final ResourceLocation id;
  private final ItemStack output;
  private final NonNullList<Ingredient> recipeItems;
  private int time;
  private boolean timeModified = false;
  private boolean energyModified = false;
  private boolean xpModified = false;
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

  public int getEnergyRequired() {
    return this.energyRequired;
  }

  public void setEnergyRequired(int energyRequired) {
    this.energyRequired = energyRequired;
    this.energyModified = true;
  }

  public boolean isEnergyModified() {
    return this.energyModified;
  }

  public boolean isTimeModified() {
    return this.timeModified;
  }

  public void setTime(int time) {
    this.time = time;
    this.timeModified = true;
  }

  public float getExperience() {
    return this.xp;
  }

  public void setExperience(float xp) {
    this.xp = xp;
    this.xpModified = true;
  }

  public boolean isXpModified() {
    return xpModified;
  }

  public boolean isModified() {
    return modified;
  }

  public void modify() {
    this.modified = true;
  }

  public void unModified() {
    this.modified = false;
  }

  public FurnaceRecipe copy() {
    return new FurnaceRecipe(id, output, recipeItems, time, energyRequired, xp);
  }

  @Override
  public String toString() {
    JsonArray array = new JsonArray(this.recipeItems.toArray().length);
    List<Ingredient> list = Stream.of(this.recipeItems.toArray()).map(ingredient -> (Ingredient) ingredient).toList();
    list.forEach(item -> array.add(item.toString()));
    JsonObject json = GsonHelper.parse(
      "{" +
        "\"id\": \"" + id.getNamespace() + ":" + id.getPath() + "\"" +
        ", \"ingredients\": " + array +
        ", \"output\": \"" + output.getDisplayName() + "\"" +
        ", \"time\": " + time +
        ", \"energy\": " + energyRequired +
        ", \"xp\": " + xp +
        "}"
    );
    return json.toString();
  }
}
