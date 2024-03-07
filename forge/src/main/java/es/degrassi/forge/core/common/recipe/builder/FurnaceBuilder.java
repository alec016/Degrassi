package es.degrassi.forge.core.common.recipe.builder;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import java.util.Collections;
import net.minecraft.resources.ResourceLocation;

public class FurnaceBuilder extends MachineBuilder<FurnaceRecipe> {

  public static final NamedCodec<FurnaceBuilder> CODEC = NamedCodec.record(
    recipeBuilder -> recipeBuilder.group(
      NamedCodec.INT.fieldOf("time").forGetter(FurnaceBuilder::getTime),
      IRequirement.CODEC.listOf().optionalFieldOf("requirements", Collections.emptyList()).forGetter(FurnaceBuilder::getRequirements)
    ).apply(recipeBuilder, (time, requirements) -> {
      FurnaceBuilder builder = new FurnaceBuilder(time);
      requirements.forEach(builder::withRequirement);
      return builder;
    }),
    "furnace recipe"
  );

  public FurnaceBuilder(int time) {
    super(time);
  }

  public FurnaceBuilder(FurnaceRecipe recipe) {
    super(recipe);
  }

  @Override
  public FurnaceRecipe build(ResourceLocation id) {
    return new FurnaceRecipe(id, getTime(), getRequirements());
  }
}
