package es.degrassi.forge.requirements;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.init.recipe.CraftingResult;
import es.degrassi.forge.init.registration.RequirementRegistry;
import es.degrassi.forge.util.storage.ExperienceStorage;

public class ExperienceRequirement implements IRequirement<ExperienceStorage> {
  public static final NamedCodec<ExperienceRequirement> CODEC = NamedCodec.record(energyRequirementInstance ->
    energyRequirementInstance.group(
      NamedCodec.FLOAT.fieldOf("amount").forGetter(requirement -> requirement.amount)
    ).apply(energyRequirementInstance, (ExperienceRequirement::new)), "Energy requirement"
  );
  private final float amount;
  public ExperienceRequirement(float amount) {
    this.amount = amount;
  }

  @Override
  public RequirementType<ExperienceRequirement> getType() {
    return RequirementRegistry.EXPERIENCE_REQUIREMENT.get();
  }

  @Override
  public boolean test(ExperienceStorage handler, ICraftingContext context) {
    int amount = (int) context.getIntegerModifiedValue(this.amount, this, null);
    return handler.canExtract(amount);
  }

  @Override
  public CraftingResult processStart(ExperienceStorage handler, ICraftingContext context) {
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd(ExperienceStorage component, ICraftingContext context) {
    return CraftingResult.pass();
  }

  public float getXP() {
    return this.amount;
  }
}
