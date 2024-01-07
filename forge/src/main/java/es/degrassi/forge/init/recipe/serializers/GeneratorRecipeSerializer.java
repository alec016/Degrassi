package es.degrassi.forge.init.recipe.serializers;

import com.google.gson.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import es.degrassi.forge.init.recipe.builder.*;
import es.degrassi.forge.init.recipe.helpers.*;
import es.degrassi.forge.init.recipe.recipes.*;
import es.degrassi.forge.util.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

public class GeneratorRecipeSerializer implements RecipeSerializer<GeneratorRecipe<?>> {
  @Override
  public @NotNull GeneratorRecipe<?> fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
    DegrassiLogger.INSTANCE.info("Parsing recipe json: {}", recipeId);
    DataResult<Pair<GeneratorRecipeBuilder, JsonElement>> result = GeneratorRecipeBuilder.CODEC.decode(JsonOps.INSTANCE, json);
    if(result.result().isPresent()) {
      DegrassiLogger.INSTANCE.info("Successfully read recipe json: {}", recipeId);
      GeneratorRecipe<?> recipe = result.result().get().getFirst().build(recipeId);
      RecipeHelpers.GENERATORS.recipesMap.put(recipeId, recipe);
      RecipeHelpers.GENERATORS.recipes.add(recipe);
      return recipe;
    } else if(result.error().isPresent()) {
      DegrassiLogger.INSTANCE.error("Error while parsing recipe json: {}, skipping...\n{}", recipeId, result.error().get().message());
      throw new JsonParseException("Error while parsing Custom Machine Recipe json: " + recipeId + " error: " + result.error().get().message());
    }
    throw new IllegalStateException("No success nor error when parsing Furnace Recipe json: " + recipeId + "This can't happen");
  }

  @Override
  public @Nullable GeneratorRecipe<?> fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
    DegrassiLogger.INSTANCE.info("Receiving recipe: {} from server.", recipeId);
    DataResult<GeneratorRecipeBuilder> result = GeneratorRecipeBuilder.CODEC.read(NbtOps.INSTANCE, buffer.readAnySizeNbt());
    if(result.result().isPresent()) {
      DegrassiLogger.INSTANCE.info("Sucessfully received recipe: {} from server.", recipeId);
      GeneratorRecipe<?> recipe = result.result().get().build(recipeId);
      RecipeHelpers.GENERATORS.recipesMap.put(recipeId, recipe);
      RecipeHelpers.GENERATORS.recipes.add(recipe);
      return recipe;
    } else if(result.error().isPresent()) {
      DegrassiLogger.INSTANCE.error("Error while parsing recipe json: {}, skipping...\n{}", recipeId, result.error().get().message());
      throw new IllegalArgumentException("Error while receiving Custom Machine Recipe from server: " + recipeId + " error: " + result.error().get().message());
    }
    throw new IllegalStateException("No success nor error when receiving Custom Machine Recipe: " + recipeId + "from server. This can't happen");
  }

  @Override
  public void toNetwork(@NotNull FriendlyByteBuf buffer, GeneratorRecipe<?> recipe) {
    DegrassiLogger.INSTANCE.info("Sending recipe: {} to clients", recipe.getId());
    DataResult<Tag> result = GeneratorRecipeBuilder.CODEC.encodeStart(NbtOps.INSTANCE, new GeneratorRecipeBuilder(recipe));
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
