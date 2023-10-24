package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.requirements.*;

public class RequirementRegistry {
  public static final Registrar<RequirementType<? extends IRequirement<?>>> REQUIREMENT_TYPE_REGISTRY = Register.REGISTRIES.builder(RequirementType.REGISTRY_KEY.location(), new RequirementType<?>[]{}).build();
  public static final DeferredRegister<RequirementType<? extends IRequirement<?>>> REQUIREMENTS = DeferredRegister.create(Degrassi.MODID, RequirementType.REGISTRY_KEY);
  public static final RegistrySupplier<RequirementType<ItemRequirement>> ITEM_REQUIREMENT = REQUIREMENTS.register("item", () -> RequirementType.inventory(ItemRequirement.CODEC));
  public static final RegistrySupplier<RequirementType<EnergyRequirement>> ENERGY_REQUIREMENT = REQUIREMENTS.register("energy", () -> RequirementType.inventory(EnergyRequirement.CODEC));
  public static final RegistrySupplier<RequirementType<ExperienceRequirement>> EXPERIENCE_REQUIREMENT = REQUIREMENTS.register("experience", () -> RequirementType.inventory(ExperienceRequirement.CODEC));

  public static void register() {
    REQUIREMENTS.register();
  }

}
