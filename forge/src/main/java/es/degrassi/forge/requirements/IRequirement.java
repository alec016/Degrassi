package es.degrassi.forge.requirements;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.codec.impl.RegistrarCodec;
import es.degrassi.forge.init.gui.IComponent;
import es.degrassi.forge.init.recipe.CraftingResult;


@SuppressWarnings("unused")
public interface IRequirement<T extends IComponent> {
  NamedCodec<IRequirement<?>> CODEC = NamedCodec.record(iRequirementInstance ->
    iRequirementInstance.group(
      RegistrarCodec.REQUIREMENT.<IRequirement<?>>dispatch(IRequirement::getType, RequirementType::getCodec, "Requirement").forGetter(requirement -> requirement)
    ).apply(iRequirementInstance, requirement -> requirement), "Requirement"
  );

  RequirementType<? extends IRequirement<?>> getType();

  boolean test(T handler, ICraftingContext context);

  CraftingResult processStart(T handler, ICraftingContext context);
  CraftingResult processEnd(T component, ICraftingContext context);

  enum ModeIO {
    INPUT,
    OUTPUT;

    public static final NamedCodec<ModeIO> CODEC = NamedCodec.enumCodec(ModeIO.class);
  }
}
