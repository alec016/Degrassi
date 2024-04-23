package es.degrassi.forge.core.integration.jei.helper;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.integration.jei.wrapper.IngredientTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ExperienceIngredientHelper implements IIngredientHelper<ExperienceComponent> {
  @Override
  public IIngredientType<ExperienceComponent> getIngredientType() {
    return IngredientTypes.EXPERIENCE;
  }

  @Override
  public String getDisplayName(ExperienceComponent ingredient) {
    return "Insufficient experience";
  }

  @Override
  public String getUniqueId(ExperienceComponent ingredient, UidContext context) {
    return "" + ingredient.getExperienceStored() + ingredient.getMode() + true;
  }

  @Override
  public ResourceLocation getResourceLocation(ExperienceComponent ingredient) {
    return new DegrassiLocation("experience");
  }

  @Override
  public ExperienceComponent copyIngredient(ExperienceComponent ingredient) {
    ExperienceComponent experience = new ExperienceComponent(ingredient.getManager(), ingredient.getCapacity(), ingredient.getEntity(), ingredient.getId(), ingredient.getMode());
    experience.setCapacity(ingredient.getCapacity());
    experience.setExperience(ingredient.getExperienceStored());
    return experience;
  }

  @Override
  public String getErrorInfo(@Nullable ExperienceComponent ingredient) {
    return "";
  }
}