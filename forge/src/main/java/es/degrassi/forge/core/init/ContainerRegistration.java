package es.degrassi.forge.core.init;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.client.book.DegrassiBook;
import es.degrassi.forge.core.common.machines.container.ChestContainer;
import es.degrassi.forge.core.common.machines.container.FurnaceContainer;
import es.degrassi.forge.core.common.machines.container.SolarPanelContainer;
import es.degrassi.forge.core.common.machines.screen.ChestScreen;
import es.degrassi.forge.core.common.machines.screen.FurnaceScreen;
import es.degrassi.forge.core.common.machines.screen.SolarPanelScreen;
import es.degrassi.forge.lib.client.screen.wiki.WikiScreen;
import es.degrassi.forge.lib.client.wiki.Wiki;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class ContainerRegistration {
  public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Degrassi.MODID, Registries.MENU);

  public static final RegistrySupplier<MenuType<FurnaceContainer>> FURNACE = MENUS.register(
    "furnace",
    () -> MenuRegistry.ofExtended(FurnaceContainer::new)
  );
  public static final RegistrySupplier<MenuType<SolarPanelContainer>> SOLAR_PANEL = MENUS.register(
    "solar_panel",
    () -> MenuRegistry.ofExtended(SolarPanelContainer::new)
  );
  public static final RegistrySupplier<MenuType<ChestContainer>> CHEST = MENUS.register(
    "chest",
    () -> MenuRegistry.ofExtended(ChestContainer::new)
  );

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
    DegrassiBook.register();
  }

  public static void openManualScreen() {
    WikiScreen.open(Wiki.WIKIS.get(Degrassi.MODID).getCategories().get(0));
  }
}
