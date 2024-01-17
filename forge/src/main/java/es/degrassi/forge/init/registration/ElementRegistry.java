package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.GuiElementType;
import es.degrassi.forge.init.gui.element.IGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;

public class ElementRegistry {
  public static final Registrar<GuiElementType<? extends IGuiElement>> GUI_ELEMENT_TYPE_REGISTRY = Register.REGISTRIES.builder(GuiElementType.REGISTRY_KEY.location(), new GuiElementType<?>[]{}).build();
  public static final DeferredRegister<GuiElementType<? extends IGuiElement>> GUI_ELEMENTS = DeferredRegister.create(Degrassi.MODID, GuiElementType.REGISTRY_KEY);


  public static final RegistrySupplier<GuiElementType<ProgressGuiElement>> PROGRESS_GUI_ELEMENT = GUI_ELEMENTS.register("progress", GuiElementType::create);
  public static final RegistrySupplier<GuiElementType<EnergyGuiElement>> ENERGY_GUI_ELEMENT = GUI_ELEMENTS.register("energy", GuiElementType::create);

  public static void register() {
    GUI_ELEMENTS.register();
  }
}
