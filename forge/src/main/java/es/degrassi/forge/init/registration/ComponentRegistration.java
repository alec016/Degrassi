package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.core.components.EnergyComponent;
import es.degrassi.forge.core.components.ExperienceComponent;

public class ComponentRegistration {
  public static final Registrar<ComponentType<? extends IComponent>> COMPONENT_TYPE_REGISTRY = Register.REGISTRIES.builder(ComponentType.REGISTRY_KEY.location(), new ComponentType<?>[]{}).build();
  public static final DeferredRegister<ComponentType<? extends IComponent>> MACHINE_COMPONENTS = DeferredRegister.create(Degrassi.MODID, ComponentType.REGISTRY_KEY);

  public static final RegistrySupplier<ComponentType<EnergyComponent>> ENERGY = COMPONENT_TYPE_REGISTRY.register(new DegrassiLocation("energy"), () -> ComponentType.create(EnergyComponent.Template.CODEC));
  public static final RegistrySupplier<ComponentType<ExperienceComponent>> EXPERIENCE = COMPONENT_TYPE_REGISTRY.register(new DegrassiLocation("experience"), () -> ComponentType.create(ExperienceComponent.Template.CODEC));
}
