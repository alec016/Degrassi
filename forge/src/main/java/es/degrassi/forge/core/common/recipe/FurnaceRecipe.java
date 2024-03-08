package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.init.RecipeRegistration;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class FurnaceRecipe extends MachineRecipe {
  private final ResourceLocation id;

  public FurnaceRecipe(ResourceLocation id, int time, List<IRequirement<?>> requirements) {
    super(time, requirements);
    this.id = id;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public RecipeSerializer<FurnaceRecipe> getSerializer() {
    return RecipeRegistration.FURNACE_SERIALIZER.get();
  }

  @Override
  public RecipeType<FurnaceRecipe> getType() {
    return RecipeRegistration.FURNACE_TYPE.get();
  }
}
