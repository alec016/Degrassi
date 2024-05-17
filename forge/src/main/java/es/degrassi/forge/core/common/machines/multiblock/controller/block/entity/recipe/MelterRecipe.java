package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.recipe.BaseMultiblockControllerRecipe;
import es.degrassi.forge.core.init.RecipeRegistration;
import java.util.List;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

@Getter
@SuppressWarnings("unchecked")
public class MelterRecipe extends BaseMultiblockControllerRecipe<MelterRecipe> {
  private final ResourceLocation id;

  public MelterRecipe(ResourceLocation id, int time, List<? extends IRequirement<?>> requirements) {
    super(time, (List<IRequirement<?>>) requirements);
    this.id = id;
  }

  @Override
  public MelterRecipe copy() {
    MelterRecipe recipe = new MelterRecipe(id, getTime(), getRequirements().stream().map(IRequirement::copy).toList());
    recipe.startRequirements.addAll(startRequirements);
    recipe.endRequirements.addAll(endRequirements);
    recipe.tickRequirements.addAll(tickRequirements);
    return recipe;
  }

  @Override
  public @NotNull RecipeSerializer<MelterRecipe> getSerializer() {
    return RecipeRegistration.MELTER_SERIALIZER.get();
  }

  @Override
  public @NotNull RecipeType<MelterRecipe> getType() {
    return RecipeRegistration.MELTER_TYPE.get();
  }
}
