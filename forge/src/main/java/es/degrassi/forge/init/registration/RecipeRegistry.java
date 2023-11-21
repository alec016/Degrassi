package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.recipe.recipes.FurnaceRecipe;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
import es.degrassi.forge.init.recipe.recipes.UpgradeMakerRecipe;
import es.degrassi.forge.init.recipe.recipes.generators.JewelryGeneratorRecipe;
import es.degrassi.forge.init.recipe.serializers.FurnaceRecipeSerializer;
import es.degrassi.forge.init.recipe.serializers.MelterRecipeSerializer;
import es.degrassi.forge.init.recipe.serializers.UpgradeMakerRecipeSerializer;
import es.degrassi.forge.init.recipe.serializers.generators.JewelryGeneratorRecipeSerializer;
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
  public static final RegistrySupplier<RecipeType<UpgradeMakerRecipe>> UPGRADE_MAKER_RECIPE_TYPE = RECIPE_TYPES.register("upgrade_maker", () -> new RecipeType<>() {
    public String toString() {
      return "upgrade_maker";
    }
  });
  public static final RegistrySupplier<RecipeType<JewelryGeneratorRecipe>> JEWELRY_GENERATOR_RECIPE_TYPE = RECIPE_TYPES.register("jewelry_generator", () -> new RecipeType<>() {
    public String toString() {
      return "jewelry_generator";
    }
  });

  // serializers
  public static final RegistrySupplier<RecipeSerializer<MelterRecipe>> MELTER_SERIALIZER = SERIALIZERS.register("melter", MelterRecipeSerializer::new);
  public static final RegistrySupplier<RecipeSerializer<FurnaceRecipe>> FURNACE_SERIALIZER = SERIALIZERS.register("furnace", FurnaceRecipeSerializer::new);
  public static final RegistrySupplier<RecipeSerializer<UpgradeMakerRecipe>> UPGRADE_MAKER_SERIALIZER = SERIALIZERS.register("upgrade_maker", UpgradeMakerRecipeSerializer::new);
  public static final RegistrySupplier<RecipeSerializer<JewelryGeneratorRecipe>> JEWELRY_GENERATOR_SERIALIZER = SERIALIZERS.register("jewelry_generator", JewelryGeneratorRecipeSerializer::new);

  public static void register() {
    SERIALIZERS.register();
    RECIPE_TYPES.register();
  }
}
