package es.degrassi.forge.core.data.recipe;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.recipe.serializer.MachineSerializer;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Result implements FinishedRecipe {
  private final ResourceLocation id;
  protected final List<IRequirement<?>> requirements;
  protected final RequirementManager manager;
  private final int time;
  private final Advancement.Builder advancement;
  private final ResourceLocation advancementId;
  private final MachineSerializer<? extends MachineRecipe<?>> serializer;

  public Result(ResourceLocation id, RequirementManager manager, int time, Advancement.Builder advancement, ResourceLocation advancementId, MachineSerializer<? extends MachineRecipe<?>> serializer) {
    this.id = id;
    this.manager = manager;
    this.requirements = manager.get();
    this.time = time;
    this.advancement = advancement;
    this.advancementId = advancementId;
    this.serializer = serializer;
  }
  @Override
  public void serializeRecipeData(JsonObject json) {
    json.addProperty("time", time);
  }

  @Override
  public @NotNull ResourceLocation getId() {
    return id;
  }

  @Override
  public @NotNull MachineSerializer<? extends MachineRecipe<?>> getType() {
    return serializer;
  }

  @Nullable
  @Override
  public JsonObject serializeAdvancement() {
    return this.advancement.serializeToJson();
  }

  @Nullable
  @Override
  public ResourceLocation getAdvancementId() {
    return advancementId;
  }
}
