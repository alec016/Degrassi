package es.degrassi.forge.core.data.recipe;

import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.data.recipe.builder.FurnaceBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MachineRecipeGeneratorBuilder extends RequirementManager implements RecipeBuilder {
  protected final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
  protected final int time;
  protected MachineRecipeGeneratorBuilder(int time) {
    this.time = time;
  }

  public static MachineRecipeGeneratorBuilder furnace(int time) {
    return new FurnaceBuilder(time);
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
}
