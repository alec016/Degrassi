package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.MelterRecipe;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.builder.MelterRecipeBuilder;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.serializer.MelterRecipeSerializer;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.recipe.builder.MachineBuilder;
import es.degrassi.forge.core.common.recipe.serializer.FurnaceSerializer;
import es.degrassi.forge.core.common.recipe.serializer.MachineSerializer;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeRegistration {
  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Degrassi.MODID, Registries.RECIPE_SERIALIZER);
  public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Degrassi.MODID, Registries.RECIPE_TYPE);

  public static final RegistrySupplier<FurnaceSerializer> FURNACE_SERIALIZER = registerSerializer("furnace", FurnaceSerializer::new);

  public static final RegistrySupplier<MelterRecipeSerializer> MELTER_SERIALIZER = registerSerializer("melter", MelterRecipeSerializer::new);

  public static final RegistrySupplier<RecipeType<FurnaceRecipe>> FURNACE_TYPE = registerRecipeType("furnace");

  public static final RegistrySupplier<RecipeType<MelterRecipe>> MELTER_TYPE = registerRecipeType("melter");



  private static <T extends MachineSerializer<?>> RegistrySupplier<T> registerSerializer(String id, Supplier<T> supplier) {
    return RECIPE_SERIALIZERS.register(id, supplier);
  }

  private static <T extends MachineRecipe<T>> RegistrySupplier<RecipeType<T>> registerRecipeType(String id) {
    return RECIPE_TYPES.register(id, () -> new RecipeType<T>() {
      @Override
      public String toString() {
        return id;
      }
    });
  }
}
