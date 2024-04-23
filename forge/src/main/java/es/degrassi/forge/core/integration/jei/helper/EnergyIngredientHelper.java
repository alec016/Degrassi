package es.degrassi.forge.core.integration.jei.helper;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.integration.jei.wrapper.IngredientTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class EnergyIngredientHelper implements IIngredientHelper<EnergyComponent> {
  @Override
  public IIngredientType<EnergyComponent> getIngredientType() {
    return IngredientTypes.ENERGY;
  }

  @Override
  public String getDisplayName(EnergyComponent ingredient) {
    return "Insufficient energy";
  }

  @Override
  public String getUniqueId(EnergyComponent ingredient, UidContext context) {
    return "" + ingredient.getEnergyStored() + ingredient.getMode() + true;
  }

  @Override
  public ResourceLocation getResourceLocation(EnergyComponent ingredient) {
    return new DegrassiLocation("energy");
  }

  @Override
  public EnergyComponent copyIngredient(EnergyComponent ingredient) {
    EnergyComponent energy = new EnergyComponent(ingredient.getManager(), ingredient.getMaxEnergyStored(), ingredient.getMaxInput(), ingredient.getMaxOutput(), ingredient.getEntity(), ingredient.getId(), ingredient.getMode());
    energy.setEnergy(ingredient.getEnergyStored());
    return energy;
  }

  @Override
  public String getErrorInfo(@Nullable EnergyComponent ingredient) {
    return "";
  }
}
