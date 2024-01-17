package es.degrassi.forge.integration.jei.renderer.helpers;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyIngredientHelper implements IIngredientHelper<EnergyGuiElement> {
  @Override
  public @NotNull IIngredientType<EnergyGuiElement> getIngredientType() {
    return DegrassiTypes.ENERGY;
  }

  @Override
  public @NotNull String getDisplayName(@NotNull EnergyGuiElement ingredient) {
    return "Insufficient energy";
  }

  @Override
  public @NotNull String getUniqueId(@NotNull EnergyGuiElement ingredient, @NotNull UidContext context) {
    return "" + ingredient.getStorage().getEnergyStored() + ingredient.vertical + true;
  }

  @Override
  public @NotNull ResourceLocation getResourceLocation(@NotNull EnergyGuiElement ingredient) {
    return new DegrassiLocation("energy");
  }

  @Override
  public @NotNull EnergyGuiElement copyIngredient(@NotNull EnergyGuiElement ingredient) {
    return new EnergyGuiElement(ingredient.getX(), ingredient.getY(), ingredient.getStorage(), ingredient.getWidth(), ingredient.getHeight());
  }

  @Override
  public @NotNull String getErrorInfo(@Nullable EnergyGuiElement ingredient) {
    return "";
  }
}
