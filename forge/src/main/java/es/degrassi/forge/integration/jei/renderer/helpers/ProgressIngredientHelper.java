package es.degrassi.forge.integration.jei.renderer.helpers;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProgressIngredientHelper implements IIngredientHelper<ProgressComponent> {
  @Override
  public @NotNull IIngredientType<ProgressComponent> getIngredientType() {
    return DegrassiTypes.PROGRESS;
  }

  @Override
  public @NotNull String getDisplayName(@NotNull ProgressComponent ingredient) {
    return "Not in progress";
  }

  @Override
  public @NotNull String getUniqueId(@NotNull ProgressComponent ingredient, @NotNull UidContext context) {
    return ingredient.getStorage().getProgress() + "/" + ingredient.getStorage().getMaxProgress();
  }

  @Override
  public @NotNull ResourceLocation getResourceLocation(@NotNull ProgressComponent ingredient) {
    return new DegrassiLocation("progress");
  }

  @Override
  public @NotNull ProgressComponent copyIngredient(@NotNull ProgressComponent ingredient) {
    return new ProgressComponent(ingredient.getX(), ingredient.getY(), ingredient.getStorage(), ingredient.getWidth(), ingredient.getHeight());
  }

  @Override
  public @NotNull String getErrorInfo(@Nullable ProgressComponent ingredient) {
    return "";
  }
}
