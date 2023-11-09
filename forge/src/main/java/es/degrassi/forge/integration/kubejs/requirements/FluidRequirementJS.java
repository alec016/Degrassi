package es.degrassi.forge.integration.kubejs.requirements;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import es.degrassi.forge.api.ingredient.FluidIngredient;
import es.degrassi.forge.api.ingredient.FluidTagIngredient;
import es.degrassi.forge.integration.kubejs.recipes.builder.AbstractRecipeBuilderJS;
import es.degrassi.forge.integration.kubejs.recipes.builder.RecipeBuilderJS;
import es.degrassi.forge.requirements.FluidRequirement;
import es.degrassi.forge.requirements.IRequirement;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface FluidRequirementJS<T extends AbstractRecipeBuilderJS<?, ?>> extends RecipeBuilderJS<T> {
  default T requireFluid(@NotNull FluidStackJS fluid) {
    return this.addRequirement(new FluidRequirement(new FluidIngredient(fluid.getFluid()), (int) fluid.kjs$getAmount(), IRequirement.ModeIO.INPUT));
  }

  default T requireFluidTag(String tag, int amount) {
    try {
      return this.addRequirement(new FluidRequirement(FluidTagIngredient.create(tag), amount, IRequirement.ModeIO.INPUT));
    } catch (IllegalArgumentException e) {
      return error(e.getMessage());
    }
  }

  default T produceFluid(@NotNull FluidStackJS stack) {
    return this.addRequirement(new FluidRequirement(new FluidIngredient(stack.getFluid()), (int) stack.kjs$getAmount(), IRequirement.ModeIO.OUTPUT));
  }
}
