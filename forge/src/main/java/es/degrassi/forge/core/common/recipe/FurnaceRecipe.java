package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.init.RecipeRegistration;
import java.util.List;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

@Getter
public class FurnaceRecipe extends MachineRecipe<FurnaceRecipe> {
  private final ResourceLocation id;

  @SuppressWarnings("unchecked")
  public FurnaceRecipe(ResourceLocation id, int time, List<? extends IRequirement<?>> requirements) {
    super(time, (List<IRequirement<?>>) requirements);
    this.id = id;
  }

  @Override
  public FurnaceRecipe copy() {
    FurnaceRecipe recipe = new FurnaceRecipe(getId(), getTime(), getRequirements().stream().map(IRequirement::copy).toList());
    recipe.startRequirements.addAll(startRequirements);
    recipe.endRequirements.addAll(endRequirements);
    recipe.tickRequirements.addAll(tickRequirements);
    return recipe;
  }

  @Override
  public @NotNull RecipeSerializer<FurnaceRecipe> getSerializer() {
    return RecipeRegistration.FURNACE_SERIALIZER.get();
  }

  @Override
  public @NotNull RecipeType<FurnaceRecipe> getType() {
    return RecipeRegistration.FURNACE_TYPE.get();
  }
}
