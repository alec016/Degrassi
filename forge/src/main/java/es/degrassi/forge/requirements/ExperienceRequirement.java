package es.degrassi.forge.requirements;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.init.recipe.CraftingResult;
import es.degrassi.forge.init.registration.RequirementRegistry;
import es.degrassi.forge.init.gui.component.ExperienceComponent;

public class ExperienceRequirement implements IRequirement<ExperienceComponent> {
  public static final NamedCodec<ExperienceRequirement> CODEC = NamedCodec.record(energyRequirementInstance ->
    energyRequirementInstance.group(
      NamedCodec.INT.fieldOf("amount").forGetter(requirement -> requirement.amount)
    ).apply(energyRequirementInstance, (ExperienceRequirement::new)), "Energy requirement"
  );
  private final int amount;
  public ExperienceRequirement(int amount) {
    this.amount = amount;
  }

  @Override
  public RequirementType<ExperienceRequirement> getType() {
    return RequirementRegistry.EXPERIENCE_REQUIREMENT.get();
  }

  @Override
  public boolean test(ExperienceComponent handler, ICraftingContext context) {
    int amount = (int) context.getIntegerModifiedValue(this.amount, this, null);
    return handler.canExtract(amount);
  }

  @Override
  public CraftingResult processStart(ExperienceComponent handler, ICraftingContext context) {
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd(ExperienceComponent component, ICraftingContext context) {
    return CraftingResult.pass();
  }

  public int getXP() {
    return this.amount;
  }
}
