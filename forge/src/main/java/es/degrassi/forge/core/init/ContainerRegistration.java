package es.degrassi.forge.core.init;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.container.ChestContainer;
import es.degrassi.forge.core.common.machines.container.FurnaceContainer;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.container.SolarPanelContainer;
import es.degrassi.forge.core.common.machines.multiblock.controller.client.container.MelterContainer;
import es.degrassi.forge.core.common.machines.multiblock.controller.client.screen.MelterScreen;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.container.EnergyHatchContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.container.FluidInputTankContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.container.FluidOutputTankContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.container.FluidQuadrupleInputTankContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.container.InputBusContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.container.OutputBusContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.screen.EnergyHatchScreen;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.screen.FluidInputTankScreen;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.screen.FluidOutputTankScreen;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.screen.FluidQuadrupleInputTankScreen;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.screen.InputBusScreen;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.screen.OutputBusScreen;
import es.degrassi.forge.core.common.machines.screen.ChestScreen;
import es.degrassi.forge.core.common.machines.screen.FurnaceScreen;
import es.degrassi.forge.core.common.machines.screen.SolarPanelScreen;
import es.degrassi.forge.core.digital.client.container.DigitalControllerContainer;
import es.degrassi.forge.core.digital.client.screen.DigitalControllerScreen;
import es.degrassi.forge.core.digital.client.screen.controller.SelectFrequencyScreen;
import es.degrassi.forge.core.init._temp.DegrassiBook;
import es.degrassi.forge.lib.client.screen.wiki.WikiScreen;
import es.degrassi.forge.lib.client.wiki.Wiki;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.xplat.IXplatAbstractions;
import vazkii.patchouli.xplat.XplatModContainer;

public class ContainerRegistration {
  public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Degrassi.MODID, Registries.MENU);

  public static final RegistrySupplier<MenuType<FurnaceContainer>> FURNACE = registerMenuType("furnace", FurnaceContainer::new);

  public static final RegistrySupplier<MenuType<SolarPanelContainer>> SOLAR_PANEL = registerMenuType("solar_panel", SolarPanelContainer::new);

  public static final RegistrySupplier<MenuType<ChestContainer>> CHEST = registerMenuType("chest", ChestContainer::new);
  public static final RegistrySupplier<MenuType<MelterContainer>> MELTER = registerMenuType("melter", MelterContainer::new);

  public static final RegistrySupplier<MenuType<DigitalControllerContainer>> DIGITAL_CONTROLLER = registerMenuType("controller", DigitalControllerContainer::new);

  public static final RegistrySupplier<MenuType<InputBusContainer>> INPUT_BUS = registerMenuType("input_bus", InputBusContainer::new);

  public static final RegistrySupplier<MenuType<OutputBusContainer>> OUTPUT_BUS = registerMenuType("output_bus", OutputBusContainer::new);

  public static final RegistrySupplier<MenuType<EnergyHatchContainer>> ENERGY_HATCH = registerMenuType("energy_hatch", EnergyHatchContainer::new);

  public static final RegistrySupplier<MenuType<FluidInputTankContainer>> FLUID_INPUT_TANK = registerMenuType("fluid_input_tank", FluidInputTankContainer::new);
  public static final RegistrySupplier<MenuType<FluidOutputTankContainer>> FLUID_OUTPUT_TANK = registerMenuType("fluid_output_tank", FluidOutputTankContainer::new);
  public static final RegistrySupplier<MenuType<FluidQuadrupleInputTankContainer>> FLUID_QUADRUPLE_INPUT_TANK = registerMenuType("fluid_quadruple_tank", FluidQuadrupleInputTankContainer::new);

  public static void registerScreens() {
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.FURNACE.get(),
      FurnaceScreen::new
    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.SOLAR_PANEL.get(),
      SolarPanelScreen::new
    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.CHEST.get(),
      ChestScreen::new
    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.MELTER.get(),
      MelterScreen::new
    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.DIGITAL_CONTROLLER.get(),
      DigitalControllerScreen::new
    );
//    MenuRegistry.registerScreenFactory(
//      ContainerRegistration.DIGITAL_CONTROLLER.get(),
//      SelectFrequencyScreen::new
//    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.INPUT_BUS.get(),
      InputBusScreen::new
    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.OUTPUT_BUS.get(),
      OutputBusScreen::new
    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.ENERGY_HATCH.get(),
      EnergyHatchScreen::new
    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.FLUID_INPUT_TANK.get(),
      FluidInputTankScreen::new
    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.FLUID_OUTPUT_TANK.get(),
      FluidOutputTankScreen::new
    );
    MenuRegistry.registerScreenFactory(
      ContainerRegistration.FLUID_QUADRUPLE_INPUT_TANK.get(),
      FluidQuadrupleInputTankScreen::new
    );
//    DegrassiBook.register();

    XplatModContainer mod = IXplatAbstractions.INSTANCE.getAllMods().stream().filter(m -> m.getId().equals(Degrassi.MODID)).findFirst().orElse(null);
    if (mod != null)
      BookRegistry.INSTANCE.books.put(
        new DegrassiLocation("book"),
        new DegrassiBook(mod, new DegrassiLocation("book"), true)
      );
  }

  private static <T extends MachineContainer<?>> RegistrySupplier<MenuType<T>> registerMenuType(String id, MenuRegistry.ExtendedMenuTypeFactory<T> factory) {
    return MENUS.register(
      id,
      () -> MenuRegistry.ofExtended(factory)
    );
  }

  public static void openManualScreen() {
    WikiScreen.open(Wiki.WIKIS.get(Degrassi.MODID).getCategories().get(0));
  }
}
