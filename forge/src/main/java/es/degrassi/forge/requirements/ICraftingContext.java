package es.degrassi.forge.requirements;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface ICraftingContext {
  BaseEntity getEntity();
  IDegrassiRecipe getRecipe();
  double getRemainingTime();
  double getBaseSpeed();
  void setBaseSpeed(double baseSpeed);
  double getModifiedSpeed();
  double getModifiedValue(double value, IRequirement<?> requirement, @Nullable String target);
  long getIntegerModifiedValue(double value, IRequirement<?> requirement, @Nullable String target);
  double getPerTickModifiedValue(double value, IRequirement<?> requirement, @Nullable String target);
  long getPerTickIntegerModifiedValue(double value, IRequirement<?> requirement, @Nullable String target);

}
