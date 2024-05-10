package es.degrassi.forge.core.integration.jei.helper;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.integration.jei.wrapper.IngredientTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class FluidIngredientHelper implements IIngredientHelper<FluidComponent> {
  @Override
  public IIngredientType<FluidComponent> getIngredientType() {
    return IngredientTypes.FLUID;
  }

  @Override
  public String getDisplayName(FluidComponent ingredient) {
    return "Insufficient fluid: " + ingredient.getFluid().getDisplayName().getString();
  }

  @Override
  public String getUniqueId(FluidComponent ingredient, UidContext context) {
    return ingredient.getFluid().getDisplayName().getString() + ingredient.getFluid().getAmount() + ingredient.getMode() + true;
  }

  @Override
  public ResourceLocation getResourceLocation(FluidComponent ingredient) {
    return new DegrassiLocation("fluid");
  }

  @Override
  public FluidComponent copyIngredient(FluidComponent ingredient) {
    FluidComponent fluid = new FluidComponent(ingredient.getManager(), ingredient.getId(), ingredient.isWhitelist(), ingredient.getCapacity(), ingredient.getEntity(), ingredient.getMode(), ingredient.getFilter().toArray(Fluid[]::new));
    fluid.setFluid(ingredient.getFluid());
    return fluid;
  }

  @Override
  public String getErrorInfo(@Nullable FluidComponent ingredient) {
    return "";
  }
}
