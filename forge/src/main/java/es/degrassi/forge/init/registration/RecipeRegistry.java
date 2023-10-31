package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.recipe.recipes.FurnaceRecipe;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
import es.degrassi.forge.init.recipe.serializers.FurnaceRecipeSerializer;
import es.degrassi.forge.init.recipe.serializers.MelterRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@SuppressWarnings("unused")
public class RecipeRegistry {
  public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Degrassi.MODID, Registry.RECIPE_SERIALIZER_REGISTRY);
  public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Degrassi.MODID, Registry.RECIPE_TYPE_REGISTRY);

  // types
  public static final RegistrySupplier<RecipeType<MelterRecipe>> MELTER_RECIPE_TYPE = RECIPE_TYPES.register("melter", () -> new RecipeType<>() {
    public String toString() {
      return "melter";
    }
  });
  public static final RegistrySupplier<RecipeType<FurnaceRecipe>> FURNACE_RECIPE_TYPE = RECIPE_TYPES.register("furnace", () -> new RecipeType<>() {
    public String toString() {
      return "furnace";
    }
  });

  // serializers
  public static final RegistrySupplier<RecipeSerializer<MelterRecipe>> MELTER_SERIALIZER = SERIALIZERS.register("melter", MelterRecipeSerializer::new);
  public static final RegistrySupplier<RecipeSerializer<FurnaceRecipe>> FURNACE_SERIALIZER = SERIALIZERS.register("furnace", FurnaceRecipeSerializer::new);

  public static void register() {
    SERIALIZERS.register();
    RECIPE_TYPES.register();
  }
}
