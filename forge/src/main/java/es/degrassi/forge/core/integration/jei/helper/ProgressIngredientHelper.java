package es.degrassi.forge.core.integration.jei.helper;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.integration.jei.wrapper.IngredientTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ProgressIngredientHelper implements IIngredientHelper<ProgressComponent> {
  @Override
  public IIngredientType<ProgressComponent> getIngredientType() {
    return IngredientTypes.PROGRESS;
  }

  @Override
  public String getDisplayName(ProgressComponent ingredient) {
    return "Not in progress";
  }

  @Override
  public String getUniqueId(ProgressComponent ingredient, UidContext context) {
    return ingredient.getProgress() + "/" + ingredient.getMaxProgress();
  }

  @Override
  public ResourceLocation getResourceLocation(ProgressComponent ingredient) {
    return new DegrassiLocation("progress");
  }

  @Override
  public ProgressComponent copyIngredient(ProgressComponent ingredient) {
    ProgressComponent progress = new ProgressComponent(ingredient.getManager(), ingredient.getEntity());
    progress.setMaxProgress(ingredient.getMaxProgress());
    progress.setProgress(ingredient.getProgress());
    return progress;
  }

  @Override
  public String getErrorInfo(@Nullable ProgressComponent ingredient) {
    return "";
  }
}
