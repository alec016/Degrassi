package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.core.element.ElementType;
import es.degrassi.forge.api.core.element.IGuiElement;
import es.degrassi.forge.core.elements.EnergyElement;

public class ElementRegistration {
  public static final Registrar<ElementType<? extends IGuiElement>> ELEMENT_TYPE_REGISTRY = Register.REGISTRIES.builder(ElementType.REGISTRY_KEY.location(), new ElementType<?>[]{}).build();
  public static final DeferredRegister<ElementType<? extends IGuiElement>> GUI_ELEMENTS = DeferredRegister.create(Degrassi.MODID, ElementType.REGISTRY_KEY);
  public static final RegistrySupplier<ElementType<EnergyElement>> ENERGY = GUI_ELEMENTS.register(new DegrassiLocation("energy"), () -> ElementType.create(EnergyElement.CODEC));
}
