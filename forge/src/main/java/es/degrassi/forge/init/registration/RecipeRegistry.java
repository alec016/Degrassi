package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.Degrassi;
import es.degrassi.comon.init.recipe.furnace.FurnaceRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@SuppressWarnings("unused")
public class RecipeRegistry {
  public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Degrassi.MODID, Registry.RECIPE_SERIALIZER_REGISTRY);
  public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Degrassi.MODID, Registry.RECIPE_TYPE_REGISTRY);

  public static final RegistrySupplier<RecipeSerializer<FurnaceRecipe>> FURNACE_SERIALIZER = SERIALIZERS.register(
    "furnace",
    () -> FurnaceRecipe.Serializer.INSTANCE
  );
  public static final RegistrySupplier<RecipeType<FurnaceRecipe>> FURNACE_RECIPE_TYPE = RECIPE_TYPES.register(
    "furnace",
    () -> FurnaceRecipe.Type.INSTANCE
  );

  public static void register() {
    SERIALIZERS.register();
    RECIPE_TYPES.register();
  }
}
