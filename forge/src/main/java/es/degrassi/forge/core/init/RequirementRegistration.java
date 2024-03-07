package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;

public class RequirementRegistration {
  public static final Registrar<RequirementType<? extends IRequirement<?>>> REQUIREMENT_TYPE_REGISTRY = Registration.REGISTRIES.builder(RequirementType.REGISTRY_KEY.location(), new RequirementType<?>[]{}).build();
  public static final DeferredRegister<RequirementType<? extends IRequirement<?>>> REQUIREMENTS = DeferredRegister.create(Degrassi.MODID, RequirementType.REGISTRY_KEY);
  public static final RegistrySupplier<RequirementType<EnergyRequirement>> ENERGY = REQUIREMENTS.register("energy", () -> new RequirementType<>(EnergyRequirement.CODEC));
}
