package es.degrassi.forge.core.integration.jei.wrapper;

import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import mezz.jei.api.ingredients.IIngredientType;

public class IngredientTypes {
  public static final IIngredientType<ProgressComponent> PROGRESS = () -> ProgressComponent.class;
  public static final IIngredientType<EnergyComponent> ENERGY = () -> EnergyComponent.class;
  public static final IIngredientType<ExperienceComponent> EXPERIENCE = () -> ExperienceComponent.class;
}
