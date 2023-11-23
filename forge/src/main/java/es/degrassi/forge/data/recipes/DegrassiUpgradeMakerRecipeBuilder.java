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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class DegrassiUpgradeMakerRecipeBuilder implements RecipeBuilder {
  private final Item result;
  private Item input1, input2;
  private Fluid fluidInput;
  private final int count;
  private int time, energy, input1Amount, input2Amount, fluidInputAmount;
  private final Advancement.Builder advancement = Advancement.Builder.advancement();


  public DegrassiUpgradeMakerRecipeBuilder(@NotNull ItemLike result, int count) {
    this.result = result.asItem();
    this.count = count;
  }

  public DegrassiUpgradeMakerRecipeBuilder(@NotNull ItemLike result) {
    this(result, 1);
  }

  @Override
  public @NotNull DegrassiUpgradeMakerRecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
//    this.advancement.addCriterion(criterionName, criterionTrigger);
    return this;
  }

  @Override
  public @NotNull DegrassiUpgradeMakerRecipeBuilder group(@Nullable String groupName) {
    return this;
  }

  @Override
  public @NotNull Item getResult() {
    return result;
  }

  public DegrassiUpgradeMakerRecipeBuilder time(int time) {
    this.time = time;
    return this;
  }

  public DegrassiUpgradeMakerRecipeBuilder energy(int energy) {
    this.energy = energy;
    return this;
  }

  public DegrassiUpgradeMakerRecipeBuilder input1(Item input, int count) {
    this.input1 = input;
    this.input1Amount = count;
    return this;
  }

  public DegrassiUpgradeMakerRecipeBuilder input1(Item input) {
    return input1(input, 1);
  }

  public DegrassiUpgradeMakerRecipeBuilder input2(Item input, int count) {
    this.input2 = input;
    this.input2Amount = count;
    return this;
  }

  public DegrassiUpgradeMakerRecipeBuilder input2(Item input) {
    return input2(input, 1);
  }

  public DegrassiUpgradeMakerRecipeBuilder fluid(Fluid input, int count) {
    this.fluidInput = input;
    this.fluidInputAmount = count;
    return this;
  }

  public DegrassiUpgradeMakerRecipeBuilder fluid(Fluid input) {
    return fluid(input, 1000);
  }
  @Override
  public void save(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
    this.ensureValid(recipeId);
//    this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
    finishedRecipeConsumer.accept(
      new DegrassiUpgradeMakerRecipeBuilder.Result(
        recipeId,
        this.result,
        this.count,
        this.input1,
        this.input1Amount,
        this.input2,
        this.input2Amount,
        this.fluidInput,
        this.fluidInputAmount,
        this.time,
        this.energy,
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
    private final Item result, input1, input2;
    private final Fluid fluidInput;
    private final int count, time, energy, input1Amount, input2Amount, fluidInputAmount;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;

    public Result(ResourceLocation id, Item result, int outputAmount, Item input1, int input1Amount, Item input2, int input2Amount, Fluid fluidInput, int fluidInputAmount, int time, int energy, Advancement.Builder advancement, ResourceLocation advancementId) {
      this.id = id;
      this.time = time;
      this.energy = energy;
      this.result = result;
      this.count = outputAmount;
      this.advancement = advancement;
      this.advancementId = advancementId;
      this.fluidInput = fluidInput;
      this.fluidInputAmount = fluidInputAmount;
      this.input1 = input1;
      this.input2 = input2;
      this.input1Amount = input1Amount;
      this.input2Amount = input2Amount;
    }

    @Override
    public void serializeRecipeData(@NotNull JsonObject json) {
      JsonObject input1 = new JsonObject();
      input1.addProperty("item", Registry.ITEM.getKey(this.input1).toString());
      json.add("input1", input1);
      if (this.input1Amount != 1)
        json.addProperty("inputAmount1", this.input1Amount);

      JsonObject input2 = new JsonObject();
      input2.addProperty("item", Registry.ITEM.getKey(this.input2).toString());
      json.add("input2", input2);
      if (this.input2Amount != 1)
        json.addProperty("inputAmount2", this.input2Amount);

      JsonObject fluid = new JsonObject();
      fluid.addProperty("fluid", Registry.FLUID.getKey(fluidInput).toString());
      json.add("fluidInput", fluid);
      if (this.fluidInputAmount != 1000)
        json.addProperty("inputFluidAmount", this.fluidInputAmount);

      JsonObject res = new JsonObject();
      res.addProperty("item", Registry.ITEM.getKey(result).toString());
      json.add("output", res);
      if (count != 1)
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
      return RecipeRegistry.UPGRADE_MAKER_SERIALIZER.get();
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
