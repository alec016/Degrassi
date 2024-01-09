package es.degrassi.forge.data.recipes;

import com.google.common.collect.*;
import com.google.gson.*;
import es.degrassi.forge.init.registration.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import net.minecraft.advancements.*;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

public class DegrassiGeneratorRecipeBuilder implements RecipeBuilder {
  private int inputItemCount, time, energy;
  private Item inputItem;
  private final List<String> machines = Lists.newLinkedList();

  private final Advancement.Builder advancement = Advancement.Builder.advancement();

  public DegrassiGeneratorRecipeBuilder(int energy) {
    this.energy = energy;
  }

  @Override
  public @NotNull DegrassiGeneratorRecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
    this.advancement.addCriterion(criterionName, criterionTrigger);
    return this;
  }

  @Override
  public @NotNull RecipeBuilder group(@Nullable String groupName) {
    return this;
  }

  public DegrassiGeneratorRecipeBuilder time(int time) {
    this.time = time;
    return this;
  }

  public DegrassiGeneratorRecipeBuilder energy(int energy) {
    this.energy = energy;
    return this;
  }

  public DegrassiGeneratorRecipeBuilder input(Item input, int count) {
    this.inputItem = input;
    this.inputItemCount = count;
    return this;
  }

  public DegrassiGeneratorRecipeBuilder input(Item input) {
    return input(input, 1);
  }

  public DegrassiGeneratorRecipeBuilder machine(String machine) {
    AtomicBoolean exists = new AtomicBoolean(false);
    this.machines.forEach(m -> {
      if (exists.get()) return;
      if (m.equals(machine)) exists.set(true);
    });

    if (!exists.get()) this.machines.add(machine);
    return this;
  }

  public DegrassiGeneratorRecipeBuilder machines(List<String> machines) {
    machines.forEach(this::machine);
    return this;
  }

  @Override
  public @NotNull Item getResult() {
    return Items.AIR;
  }

  @Override
  public void save(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
    this.ensureValid(recipeId);
//    this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
    finishedRecipeConsumer.accept(
      new DegrassiGeneratorRecipeBuilder.Result(
        recipeId,
        this.time,
        this.energy,
        this.machines,
        List.of(Ingredient.of(new ItemStack(this.inputItem, this.inputItemCount))),
        this.advancement,
        recipeId
      )
    );
  }

  private void ensureValid(ResourceLocation id) {
  }

  public static class Result implements FinishedRecipe {
    private final ResourceLocation id;
    private final int time, energy;
    private final List<Ingredient> ingredients;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;
    private final List<String> machines;

    public Result(ResourceLocation id, int time, int energy, List<String> machines, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId) {
      this.id = id;
      this.time = time;
      this.energy = energy;
      this.ingredients = ingredients;
      this.advancement = advancement;
      this.advancementId = advancementId;
      this.machines = machines;
    }

    @Override
    public void serializeRecipeData(@NotNull JsonObject json) {
      json.add("input", ingredients.get(0).toJson());
      json.addProperty("time", time);
      json.addProperty("energy", energy);
      JsonArray array = new JsonArray();
      machines.forEach(array::add);
      json.add("machines", array);
    }

    @Override
    public @NotNull ResourceLocation getId() {
      return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getType() {
      return RecipeRegistry.GENERATOR_SERIALIZER.get();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
      return advancement.serializeToJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
      return advancementId;
    }
  }
}
