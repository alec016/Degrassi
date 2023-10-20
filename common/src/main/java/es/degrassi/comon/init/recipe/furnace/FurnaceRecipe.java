package es.degrassi.comon.init.recipe.furnace;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.degrassi.Degrassi;
import es.degrassi.comon.init.recipe.helpers.furnace.FurnaceRecipeHelper;
import es.degrassi.comon.util.DegrassiLogger;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.stream.Stream;

public class FurnaceRecipe implements Recipe<SimpleContainer> {
  private final ResourceLocation id;
  private final ItemStack output;
  private final NonNullList<Ingredient> recipeItems;
  private int time;
  private boolean modified;
  private int energyRequired;

  public FurnaceRecipe (
    ResourceLocation id,
    ItemStack output,
    NonNullList<Ingredient> ingredients,
    int time
  ) {
    this(id, output, ingredients, time, 10);
  }

  public FurnaceRecipe (
    ResourceLocation id,
    ItemStack output,
    NonNullList<Ingredient> ingredients,
    int time,
    int energyRequired
  ) {
    this.id = id;
    this.output = output;
    this.recipeItems = ingredients;
    this.time = time;
    this.energyRequired = energyRequired;
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
    return Serializer.INSTANCE;
  }

  @Override
  public @NotNull RecipeType<?> getType() {
    return Type.INSTANCE;
  }

  public int getTime() {
    return time;
  }

  public int getEnergyRequired() {
    return this.energyRequired;
  }

  public void setEnergyRequired(int energyRequired) {
    this.energyRequired = energyRequired;
  }

  public boolean isModified() {
    return this.modified;
  }

  public void setTime(int time) {
    this.time = time;
    this.modified = true;
  }

  public static class Type implements RecipeType<FurnaceRecipe> {
    private Type(){}
    public static final Type INSTANCE = new Type();
    public static final String ID = "furnace";
  }

  public static class Serializer implements RecipeSerializer<FurnaceRecipe> {
    public static final Serializer INSTANCE = new Serializer();
    public static final ResourceLocation ID = new ResourceLocation(Degrassi.MODID, "furnace");

    @Override
    public @NotNull FurnaceRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
      FurnaceRecipe r = FurnaceRecipeHelper.recipesMap.get(pRecipeId);
      if (r != null) {
        DegrassiLogger.INSTANCE.info("fromMap: " + r);
        return r;
      }
      ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
      int time = GsonHelper.getAsInt(pSerializedRecipe, "time");
      JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
      NonNullList<Ingredient> input = NonNullList.withSize(1, Ingredient.EMPTY);

      for (int i = 0; i < input.size(); i++) {
        input.set(i, Ingredient.fromJson(ingredients.get(i)));
      }
      FurnaceRecipe recipe = new FurnaceRecipe(pRecipeId, output, input, time);
      FurnaceRecipeHelper.recipesMap.put(pRecipeId, recipe);
      DegrassiLogger.INSTANCE.info("fromJson: " + recipe);
      return recipe;
    }

    @Override
    public @NotNull FurnaceRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
      FurnaceRecipe r = FurnaceRecipeHelper.recipesMap.get(pRecipeId);
      if (r != null) {
        DegrassiLogger.INSTANCE.info("fromMap: " + r);
        return r;
      }
      NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
      inputs = NonNullList.of(Ingredient.EMPTY, (Ingredient[]) inputs.stream().map((ing) -> Ingredient.fromNetwork(pBuffer)).toArray());

      ItemStack output = pBuffer.readItem();
      int time = pBuffer.readInt();

      FurnaceRecipe recipe = new FurnaceRecipe(pRecipeId, output, inputs, time);
      FurnaceRecipeHelper.recipesMap.put(pRecipeId, recipe);
      DegrassiLogger.INSTANCE.info("fromNetwork: " + recipe);
      return recipe;
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull FurnaceRecipe recipe) {
      DegrassiLogger.INSTANCE.info("toNetwork: " + recipe);

      pBuffer.writeInt(recipe.getIngredients().size());

      for(Ingredient ing : recipe.getIngredients()) {
        ing.toNetwork(pBuffer);
      }

      pBuffer.writeItem(recipe.getResultItem());
    }
  }

  @Override
  public String toString() {
    JsonArray array = new JsonArray(this.recipeItems.toArray().length);
    List<Ingredient> list = Stream.of(this.recipeItems.toArray()).map(ingredient -> (Ingredient) ingredient).toList();
    list.forEach(item -> array.add(item.toString()));
    JsonObject json = GsonHelper.parse(
      "{" +
        "\"id\": \"" + id.getNamespace() + ":" + id.getPath() +
        "\", \"ingredients\": " + array +
        ", \"output\": \"" + output.getDisplayName() +
        "\"}"
    );
    return json.toString();
  }
}
