package es.degrassi.comon.init.recipe.furnace;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.degrassi.Degrassi;
import es.degrassi.comon.init.recipe.IDegrassiRecipe;
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
      int energy = 10;
      if (pSerializedRecipe.has("energy")) {
        energy = GsonHelper.getAsInt(pSerializedRecipe, "energy");
      }
      // int time = GsonHelper.getAsInt(pSerializedRecipe, "time");
      float xp = GsonHelper.getAsFloat(pSerializedRecipe, "xp");
      JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
      NonNullList<Ingredient> input = NonNullList.withSize(1, Ingredient.EMPTY);

      for (int i = 0; i < input.size(); i++) {
        input.set(i, Ingredient.fromJson(ingredients.get(i)));
      }
      FurnaceRecipe recipe = new FurnaceRecipe(pRecipeId, output, input, time, energy, xp);
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
      int energy = pBuffer.readInt();
      float xp = pBuffer.readFloat();

      FurnaceRecipe recipe = new FurnaceRecipe(pRecipeId, output, inputs, time, energy, xp);
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
      pBuffer.writeInt(recipe.getTime());
      pBuffer.writeInt(recipe.getEnergyRequired());
      pBuffer.writeFloat(recipe.getExperience());
    }
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
