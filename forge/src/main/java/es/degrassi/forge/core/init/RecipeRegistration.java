package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import es.degrassi.forge.core.common.recipe.serializer.FurnaceSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeRegistration {
  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Degrassi.MODID, Registries.RECIPE_SERIALIZER);
  public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Degrassi.MODID, Registries.RECIPE_TYPE);

  public static final RegistrySupplier<FurnaceSerializer> FURNACE_SERIALIZER = RECIPE_SERIALIZERS.register(
    "furnace",
    FurnaceSerializer::new
  );


  public static final RegistrySupplier<RecipeType<FurnaceRecipe>> FURNACE_TYPE = RECIPE_TYPES.register(
    "furnace",
    () -> new RecipeType<>() {
      @Override
      public String toString() {
        return "furnace";
      }
    }
  );
}
