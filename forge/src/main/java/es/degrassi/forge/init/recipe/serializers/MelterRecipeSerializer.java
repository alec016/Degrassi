package es.degrassi.forge.init.recipe.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import es.degrassi.forge.init.recipe.builder.MelterRecipeBuilder;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
import es.degrassi.forge.util.DegrassiLogger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MelterRecipeSerializer implements RecipeSerializer<MelterRecipe> {
  @Override
  public @NotNull MelterRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
    DegrassiLogger.INSTANCE.info("Parsing recipe json: {}", recipeId);
    DataResult<Pair<MelterRecipeBuilder, JsonElement>> result = MelterRecipeBuilder.CODEC.decode(JsonOps.INSTANCE, json);
    if(result.result().isPresent()) {
      DegrassiLogger.INSTANCE.info("Successfully read recipe json: {}", recipeId);
      MelterRecipe recipe = result.result().get().getFirst().build(recipeId);
      RecipeHelpers.MELTER.recipesMap.put(recipeId, recipe);
      RecipeHelpers.MELTER.recipes.add(recipe);
      return recipe;
    } else if(result.error().isPresent()) {
      DegrassiLogger.INSTANCE.error("Error while parsing recipe json: {}, skipping...\n{}", recipeId, result.error().get().message());
      throw new JsonParseException("Error while parsing Custom Machine Recipe json: " + recipeId + " error: " + result.error().get().message());
    }
    throw new IllegalStateException("No success nor error when parsing Furnace Recipe json: " + recipeId + "This can't happen");
  }

  @Override
  public @NotNull MelterRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
    DegrassiLogger.INSTANCE.info("Receiving recipe: {} from server.", recipeId);
    DataResult<MelterRecipeBuilder> result = MelterRecipeBuilder.CODEC.read(NbtOps.INSTANCE, buffer.readAnySizeNbt());
    if(result.result().isPresent()) {
      DegrassiLogger.INSTANCE.info("Sucessfully received recipe: {} from server.", recipeId);
      MelterRecipe recipe = result.result().get().build(recipeId);
      RecipeHelpers.MELTER.recipesMap.put(recipeId, recipe);
      RecipeHelpers.MELTER.recipes.add(recipe);
      return recipe;
    } else if(result.error().isPresent()) {
      DegrassiLogger.INSTANCE.error("Error while parsing recipe json: {}, skipping...\n{}", recipeId, result.error().get().message());
      throw new IllegalArgumentException("Error while receiving Custom Machine Recipe from server: " + recipeId + " error: " + result.error().get().message());
    }
    throw new IllegalStateException("No success nor error when receiving Custom Machine Recipe: " + recipeId + "from server. This can't happen");
  }

  @Override
  public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull MelterRecipe recipe) {
    DegrassiLogger.INSTANCE.info("Sending recipe: {} to clients", recipe.getId());
    DataResult<Tag> result = MelterRecipeBuilder.CODEC.encodeStart(NbtOps.INSTANCE, new MelterRecipeBuilder(recipe));
    if(result.result().isPresent()) {
      DegrassiLogger.INSTANCE.info("Successfully send recipe: {} to clients.", recipe.getId());
      buffer.writeNbt((CompoundTag) result.result().get());
      return;
    } else if(result.error().isPresent()) {
      DegrassiLogger.INSTANCE.error("Error while sending recipe: {} to clients.%n{}", recipe.getId(), result.error().get().message());
      throw new IllegalArgumentException("Error while sending Custom Machine Recipe to clients: " + recipe.getId() + " error: " + result.error().get().message());
    }
    throw new IllegalStateException("No success nor error when sending Custom Machine Recipe: " + recipe.getId() + "to clients. This can't happen");
  }
}
