package es.degrassi.forge.data.recipes;

import com.google.gson.JsonObject;
import es.degrassi.forge.init.registration.RecipeRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class DegrassiMelterRecipeBuilder implements RecipeBuilder {
  private final Fluid result;
  private final int amount;
  private Item input;
  private int inputAmount, time, energy;
  private final Advancement.Builder advancement = Advancement.Builder.advancement();

  public DegrassiMelterRecipeBuilder(Fluid result, int amount) {
    this.result = result;
    this.amount = amount;
  }

  public DegrassiMelterRecipeBuilder(Fluid result) {
    this(result, 1000);
  }

  @Override
  public @NotNull DegrassiMelterRecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
    this.advancement.addCriterion(criterionName, criterionTrigger);
    return this;
  }

  public DegrassiMelterRecipeBuilder time(int time) {
    this.time = time;
    return this;
  }

  public DegrassiMelterRecipeBuilder energy(int energy) {
    this.energy = energy;
    return this;
  }

  public DegrassiMelterRecipeBuilder input(Item input, int count) {
    this.input = input;
    this.inputAmount = count;
    return this;
  }

  public DegrassiMelterRecipeBuilder input(Item input) {
    return input(input, 1);
  }

  @Override
  public @NotNull DegrassiMelterRecipeBuilder group(@Nullable String groupName) {
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
      new DegrassiMelterRecipeBuilder.Result(
        recipeId,
        this.result,
        this.amount,
        this.time,
        this.energy,
        List.of(Ingredient.of(new ItemStack(this.input, this.inputAmount))),
        this.advancement,
        recipeId
      )
    );
  }

  private void ensureValid(ResourceLocation id) {
//    if (this.advancement.getCriteria().isEmpty()) {
//      throw new IllegalStateException("No way of obtaining recipe " + id);
//    }
  }


  @SuppressWarnings("deprecation")
  public static class Result implements FinishedRecipe {
    private final ResourceLocation id;
    private final Fluid result;
    private final int count, time, energy;
    private final List<Ingredient> ingredients;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;

    public Result(ResourceLocation id, Fluid result, int count, int time, int energy, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId) {
      this.id = id;
      this.time = time;
      this.energy = energy;
      this.result = result;
      this.count = count;
      this.ingredients = ingredients;
      this.advancement = advancement;
      this.advancementId = advancementId;
    }

    @Override
    public void serializeRecipeData(@NotNull JsonObject json) {
      json.add("input", ingredients.get(0).toJson());
      JsonObject res = new JsonObject();
      res.addProperty("fluid", Registry.FLUID.getKey(result).toString());
      json.add("output", res);
      if (count != 1000)
        json.addProperty("outputAmount", count);
      json.addProperty("time", time);
      json.addProperty("energy", energy);
    }

    @Override
    public @NotNull ResourceLocation getId() {
      return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getType() {
      return RecipeRegistry.MELTER_SERIALIZER.get();
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
