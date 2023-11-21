package es.degrassi.forge.integration.jei.ingredients;

import es.degrassi.forge.init.gui.renderer.EnergyInfoArea;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import mezz.jei.api.ingredients.IIngredientType;

public final class DegrassiTypes {
  public static final IIngredientType<EnergyInfoArea> ENERGY = () ->  EnergyInfoArea.class;
  public static final IIngredientType<ProgressComponent> PROGRESS = () -> ProgressComponent.class;
}
