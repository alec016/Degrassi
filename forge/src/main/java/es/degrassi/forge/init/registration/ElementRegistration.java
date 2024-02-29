package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.Registrar;
import es.degrassi.forge.api.core.element.ElementType;
import es.degrassi.forge.api.core.element.IGuiElement;

public class ElementRegistration {
  public static final Registrar<ElementType<? extends IGuiElement>> ELEMENT_TYPE_REGISTRY = Register.REGISTRIES.builder(ElementType.REGISTRY_KEY.location(), new ElementType<?>[]{}).build();
}
