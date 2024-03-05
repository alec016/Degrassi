package es.degrassi.forge.core.init;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.container.FurnaceContainer;
import es.degrassi.forge.core.common.machines.screen.FurnaceScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class ContainerRegistration {
  public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Degrassi.MODID, Registries.MENU);

  public static final RegistrySupplier<MenuType<FurnaceContainer>> FURNACE = MENUS.register(
    "furnace",
    () -> MenuRegistry.ofExtended(FurnaceContainer::new)
  );

  public static void registerScreens() {
    MenuRegistry.registerScreenFactory(ContainerRegistration.FURNACE.get(), FurnaceScreen::new);
  }
}
