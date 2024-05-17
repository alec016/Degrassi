package es.degrassi.forge.api.core.common;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.impl.codec.RegistrarCodec;
import net.minecraft.network.chat.Component;

public interface IRequirement<C extends IComponent> extends IType {
  NamedCodec<IRequirement<?>> CODEC = NamedCodec.record(
    requirementInstance -> requirementInstance.group(
      RegistrarCodec.REQUIREMENT.<IRequirement<?>>dispatch(IRequirement::getType, RequirementType::getCodec, "Requirement").forGetter(requirement -> requirement)
    ).apply(requirementInstance, (requirement) -> requirement),
    "requirement"
  );
  RequirementType<? extends IRequirement<?>> getType();

  default CraftingResult processStart() {
    if (getMode().isOutput())
      return CraftingResult.pass();
    if (getComponent() == null)
      return CraftingResult.error(Component.literal("Requirement Component can not be null"));
    return CraftingResult.pass();
  }
  default CraftingResult processEnd() {
    if (getMode().isInput())
      return CraftingResult.pass();
    if (getComponent() == null)
      return CraftingResult.error(Component.literal("Requirement Component can not be null"));
    return CraftingResult.pass();
  }
  default CraftingResult processTick() {
    if (!getMode().isPerTick())
      return CraftingResult.pass();
    if (getComponent() == null)
      return CraftingResult.error(Component.literal("Requirement Component can not be null"));
    return CraftingResult.pass();
  }

  boolean componentMatches(C component);

  boolean matches(C component, int recipeTime);
  NamedCodec<? extends IRequirement<C>> getCodec();

  RequirementMode getMode();

  IRequirement<?> copy();

  String getId();

  JsonObject toJson(JsonObject json);

  String getTypeString();

  void setComponent(C component);
  C getComponent();
}
