package es.degrassi.forge.core.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.recipe.serializer.MachineSerializer;
import es.degrassi.forge.core.init.RecipeRegistration;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MachineRecipeGeneratorBuilder extends RequirementManager implements RecipeBuilder {
  private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
  private final MachineSerializer<? extends MachineRecipe<?>> serializer;
  private final int time;
  private final String type;
  private MachineRecipeGeneratorBuilder(String type, int time, MachineSerializer<? extends MachineRecipe<?>> serializer) {
    this.serializer = serializer;
    this.time = time;
    this.type = type;
  }

  public static MachineRecipeGeneratorBuilder furnace(int time) {
    return new MachineRecipeGeneratorBuilder("furnace", time, RecipeRegistration.FURNACE_SERIALIZER.get());
  }

  @Override
  public @NotNull RecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
    this.advancement.addCriterion(criterionName, criterionTrigger);
    return this;
  }

  @Override
  public @NotNull RecipeBuilder group(@Nullable String groupName) {
    return this;
  }

  @Override
  public @NotNull Item getResult() {
    return Items.AIR;
  }

  @Override
  public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
    finishedRecipeConsumer.accept(
      new Result(
        recipeId.withPrefix(type + "/"),
        get(),
        this.time,
        this.advancement,
        recipeId.withPrefix("recipes/" + type + "/"),
        this.serializer
      )
    );
  }

  static class Result implements FinishedRecipe {
    private final ResourceLocation id;
    private final List<IRequirement<?>> requirements;
    private final int time;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;
    private final MachineSerializer<? extends MachineRecipe<?>> serializer;

    public Result(ResourceLocation id, List<IRequirement<?>> requirements, int time, Advancement.Builder advancement, ResourceLocation advancementId, MachineSerializer<? extends MachineRecipe<?>> serializer) {
      this.id = id;
      this.requirements = requirements;
      this.time = time;
      this.advancement = advancement;
      this.advancementId = advancementId;
      this.serializer = serializer;
    }
    @Override
    public void serializeRecipeData(JsonObject json) {
      json.addProperty("time", time);
      JsonArray arrayOfRequirements = new JsonArray();
      requirements.forEach(req -> arrayOfRequirements.add(req.toJson()));
      json.add("requirements", arrayOfRequirements);
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
}
