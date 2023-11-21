package es.degrassi.forge.integration.jei.renderer.helpers;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.renderer.EnergyInfoArea;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyIngredientHelper implements IIngredientHelper<EnergyInfoArea> {
  @Override
  public @NotNull IIngredientType<EnergyInfoArea> getIngredientType() {
    return DegrassiTypes.ENERGY;
  }

  @Override
  public @NotNull String getDisplayName(@NotNull EnergyInfoArea ingredient) {
    return "Insufficient energy";
  }

  @Override
  public @NotNull String getUniqueId(@NotNull EnergyInfoArea ingredient, @NotNull UidContext context) {
    return "" + ingredient.getStorage().getEnergyStored() + ingredient.vertical + true;
  }

  @Override
  public @NotNull ResourceLocation getResourceLocation(@NotNull EnergyInfoArea ingredient) {
    return new DegrassiLocation("energy");
  }

  @Override
  public @NotNull EnergyInfoArea copyIngredient(@NotNull EnergyInfoArea ingredient) {
    return new EnergyInfoArea(ingredient.getX(), ingredient.getY(), ingredient.getStorage(), ingredient.getWidth(), ingredient.getHeight());
  }

  @Override
  public @NotNull String getErrorInfo(@Nullable EnergyInfoArea ingredient) {
    return "";
  }
}
