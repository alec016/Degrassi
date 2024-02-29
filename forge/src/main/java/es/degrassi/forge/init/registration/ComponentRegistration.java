package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.Registrar;
import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;

public class ComponentRegistration {
  public static final Registrar<ComponentType<? extends IComponent>> COMPONENT_TYPE_REGISTRY = Register.REGISTRIES.builder(ComponentType.REGISTRY_KEY.location(), new ComponentType<?>[]{}).build();
}
