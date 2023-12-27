package es.degrassi.forge.init.recipe.recipes.generators;

import es.degrassi.forge.init.registration.RecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class JewelryGeneratorRecipe extends GeneratorRecipe {
  public JewelryGeneratorRecipe (
    ResourceLocation id,
    NonNullList<Ingredient> ingredients,
    int energyProduced,
    int time
  ) {
    super(id, ingredients, time, energyProduced);
  }

  @Override
  public @NotNull RecipeSerializer<?> getSerializer() {
    return RecipeRegistry.JEWELRY_GENERATOR_SERIALIZER.get();
  }

  @Override
  public @NotNull RecipeType<?> getType() {
    return RecipeRegistry.JEWELRY_GENERATOR_RECIPE_TYPE.get();
  }

  @Override
  public JewelryGeneratorRecipe copy() {
    return new JewelryGeneratorRecipe(id, getIngredients(), energyProduced, time);
  }
}
