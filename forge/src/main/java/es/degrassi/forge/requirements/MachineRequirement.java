package es.degrassi.forge.requirements;

import es.degrassi.forge.api.codec.*;
import es.degrassi.forge.init.gui.*;
import es.degrassi.forge.init.recipe.*;
import es.degrassi.forge.init.registration.*;
import java.util.*;

public class MachineRequirement implements IRequirement<IComponent> {
  public static final NamedCodec<MachineRequirement> CODEC = NamedCodec.record(machineRequirementInstance ->
    machineRequirementInstance.group(
      NamedCodec.STRING.listOf().fieldOf("machines").forGetter(requirement -> requirement.machines)
    ).apply(machineRequirementInstance, MachineRequirement::new), "Machine requirement"
  );

  private final List<String> machines;

  public MachineRequirement(List<String> machines) {
    this.machines = machines;
  }

  public List<String> getMachines() {
    return machines;
  }

  @Override
  public RequirementType<? extends IRequirement<?>> getType() {
    return RequirementRegistry.MACHINE_REQUIREMENT.get();
  }

  @Override
  public boolean test(IComponent handler, ICraftingContext context) {
    return true;
  }

  @Override
  public CraftingResult processStart(IComponent handler, ICraftingContext context) {
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd(IComponent component, ICraftingContext context) {
    return CraftingResult.pass();
  }
}
