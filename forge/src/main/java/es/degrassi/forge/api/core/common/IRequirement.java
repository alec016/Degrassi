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

  default CraftingResult processStart(IComponent component) {
    return CraftingResult.pass();
  }
  default CraftingResult processEnd(IComponent component) {
    return CraftingResult.pass();
  }
  default CraftingResult processTick(IComponent component) {
    return CraftingResult.pass();
  }

  boolean componentMatches(IComponent component);

  boolean matches(IComponent component, int recipeTime);
  NamedCodec<? extends IRequirement<R>> getCodec();

  RequirementMode getMode();

  String getId();
}
