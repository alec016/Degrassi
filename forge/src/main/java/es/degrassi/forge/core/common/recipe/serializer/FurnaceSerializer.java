package es.degrassi.forge.core.common.recipe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import es.degrassi.forge.api.utils.DegrassiLogger;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.recipe.builder.FurnaceBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class FurnaceSerializer extends MachineSerializer<FurnaceRecipe> {
  @Override
  public FurnaceRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
    DegrassiLogger.INSTANCE.info("Parsing recipe json: {}", recipeId);
    DataResult<Pair<FurnaceBuilder, JsonElement>> result = FurnaceBuilder.CODEC.decode(JsonOps.INSTANCE, json);
    if(result.result().isPresent()) {
      DegrassiLogger.INSTANCE.info("Successfully read recipe json: {}", recipeId);
      return result.result().get().getFirst().build(recipeId);
    } else if(result.error().isPresent()) {
      DegrassiLogger.INSTANCE.error("Error while parsing recipe json: {}, skipping...\n{}", recipeId, result.error().get().message());
      throw new JsonParseException("Error while parsing Custom Machine Recipe json: " + recipeId + " error: " + result.error().get().message());
    }
    throw new IllegalStateException("No success nor error when parsing Custom Machine Recipe json: " + recipeId + "This can't happen");
  }

  @Override
  public @Nullable FurnaceRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
    DegrassiLogger.INSTANCE.info("Receiving recipe: {} from server.", recipeId);
    DataResult<FurnaceBuilder> result = FurnaceBuilder.CODEC.read(NbtOps.INSTANCE, buffer.readAnySizeNbt());
    if(result.result().isPresent()) {
      DegrassiLogger.INSTANCE.info("Sucessfully received recipe: {} from server.", recipeId);
      return result.result().get().build(recipeId);
    } else if(result.error().isPresent()) {
      DegrassiLogger.INSTANCE.error("Error while parsing recipe json: {}, skipping...\n{}", recipeId, result.error().get().message());
      throw new IllegalArgumentException("Error while receiving Custom Machine Recipe from server: " + recipeId + " error: " + result.error().get().message());
    }
    throw new IllegalStateException("No success nor error when receiving Custom Machine Recipe: " + recipeId + "from server. This can't happen");
  }

  @Override
  public void toNetwork(FriendlyByteBuf buffer, FurnaceRecipe recipe) {
    DegrassiLogger.INSTANCE.info("Sending recipe: {} to clients", recipe.getId());
    DataResult<Tag> result = FurnaceBuilder.CODEC.encodeStart(NbtOps.INSTANCE, new FurnaceBuilder(recipe));
    if(result.result().isPresent()) {
      DegrassiLogger.INSTANCE.info("Sucessfully send recipe: {} to clients.", recipe.getId());
      buffer.writeNbt((CompoundTag) result.result().get());
      return;
    } else if(result.error().isPresent()) {
      DegrassiLogger.INSTANCE.error("Error while sending recipe: {} to clients.%n{}", recipe.getId(), result.error().get().message());
      throw new IllegalArgumentException("Error while sending Custom Machine Recipe to clients: " + recipe.getId() + " error: " + result.error().get().message());
    }
    throw new IllegalStateException("No success nor error when sending Custom Machine Recipe: " + recipe.getId() + "to clients. This can't happen");
  }
}
