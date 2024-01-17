package es.degrassi.forge.integration.jei.ingredients;

import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import mezz.jei.api.ingredients.IIngredientType;

public final class DegrassiTypes {
  public static final IIngredientType<EnergyGuiElement> ENERGY = () ->  EnergyGuiElement.class;
  public static final IIngredientType<ProgressGuiElement> PROGRESS = () -> ProgressGuiElement.class;
}
