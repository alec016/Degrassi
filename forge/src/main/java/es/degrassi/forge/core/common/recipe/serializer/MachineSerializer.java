package es.degrassi.forge.core.common.recipe.serializer;

import es.degrassi.forge.core.common.recipe.MachineRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public abstract class MachineSerializer<T extends MachineRecipe<T>> implements RecipeSerializer<T> {
}
