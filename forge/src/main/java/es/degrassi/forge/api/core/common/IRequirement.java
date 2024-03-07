package es.degrassi.forge.api.core.common;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.impl.codec.RegistrarCodec;

public interface IRequirement<R extends IComponent> extends IType {
  NamedCodec<IRequirement<?>> CODEC = NamedCodec.record(
    requirementInstance -> requirementInstance.group(
      RegistrarCodec.REQUIREMENT.<IRequirement<?>>dispatch(IRequirement::getType, RequirementType::getCodec, "Requirement").forGetter(requirement -> requirement)
    ).apply(requirementInstance, (requirement) -> requirement),
    "requirement"
  );
  RequirementType<? extends IRequirement<?>> getType();

  CraftingResult processStart(R component);
  CraftingResult processEnd(R component);
  default CraftingResult processTick(R component) {
    return CraftingResult.PASS;
  }

  boolean matches(R component);
  NamedCodec<? extends IRequirement<R>> getCodec();
}
