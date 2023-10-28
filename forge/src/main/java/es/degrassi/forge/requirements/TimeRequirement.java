package es.degrassi.forge.requirements;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.init.recipe.CraftingResult;
import es.degrassi.forge.init.registration.RequirementRegistry;
import es.degrassi.forge.util.storage.ProgressStorage;

public class TimeRequirement implements IRequirement<ProgressStorage> {
  public static final NamedCodec<TimeRequirement> CODEC = NamedCodec.record(energyRequirementInstance ->
    energyRequirementInstance.group(
      NamedCodec.INT.fieldOf("time").forGetter(requirement -> requirement.time)
    ).apply(energyRequirementInstance, (TimeRequirement::new)), "Time requirement"
  );
  private int time;
  public TimeRequirement(int time) {
    this.time = time;
  }

  public int getTime() {
    return time;
  }

  @Override
  public RequirementType<? extends IRequirement<?>> getType() {
    return RequirementRegistry.TIME_REQUIREMENT.get();
  }

  @Override
  public boolean test(ProgressStorage handler, ICraftingContext context) {
    return false;
  }

  @Override
  public CraftingResult processStart(ProgressStorage handler, ICraftingContext context) {
    return null;
  }

  @Override
  public CraftingResult processEnd(ProgressStorage component, ICraftingContext context) {
    return null;
  }

  public void setTime(int i) {
    this.time = i;
  }
}
