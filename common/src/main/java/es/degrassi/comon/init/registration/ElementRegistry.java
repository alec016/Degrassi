package es.degrassi.comon.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.Degrassi;
import es.degrassi.comon.init.gui.screen.renderer.GuiElementType;
import es.degrassi.comon.init.gui.screen.renderer.IGuiElement;
import es.degrassi.comon.init.gui.screen.renderer.ProgressComponent;

public class ElementRegistry {
  public static final Registrar<GuiElementType<? extends IGuiElement>> GUI_ELEMENT_TYPE_REGISTRY = Registries.REGISTRIES.builder(GuiElementType.REGISTRY_KEY.location(), new GuiElementType<?>[]{}).build();
  public static final DeferredRegister<GuiElementType<? extends IGuiElement>> GUI_ELEMENTS = DeferredRegister.create(Degrassi.MODID, GuiElementType.REGISTRY_KEY);


  public static final RegistrySupplier<GuiElementType<ProgressComponent>> PROGRESS_GUI_ELEMENT = GUI_ELEMENTS.register("progress", GuiElementType::create);

  public static void register() {
    GUI_ELEMENTS.register();
  }
}
