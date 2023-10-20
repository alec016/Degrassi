package es.degrassi.forge.init.registration;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.Degrassi;
import es.degrassi.forge.init.gui.container.furnace.IronFurnaceContainer;
import es.degrassi.forge.init.gui.container.sp.*;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class ContainerRegistry {
  public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Degrassi.MODID, Registry.MENU_REGISTRY);
  public static final RegistrySupplier<MenuType<SP1Container>> SP1_CONTAINER = CONTAINERS.register("sp1_container", () -> MenuRegistry.ofExtended(SP1Container::new));
  public static final RegistrySupplier<MenuType<SP2Container>> SP2_CONTAINER = CONTAINERS.register("sp2_container", () -> MenuRegistry.ofExtended(SP2Container::new));
  public static final RegistrySupplier<MenuType<SP3Container>> SP3_CONTAINER = CONTAINERS.register("sp3_container", () -> MenuRegistry.ofExtended(SP3Container::new));
  public static final RegistrySupplier<MenuType<SP4Container>> SP4_CONTAINER = CONTAINERS.register("sp4_container", () -> MenuRegistry.ofExtended(SP4Container::new));
  public static final RegistrySupplier<MenuType<SP5Container>> SP5_CONTAINER = CONTAINERS.register("sp5_container", () -> MenuRegistry.ofExtended(SP5Container::new));
  public static final RegistrySupplier<MenuType<SP6Container>> SP6_CONTAINER = CONTAINERS.register("sp6_container", () -> MenuRegistry.ofExtended(SP6Container::new));
  public static final RegistrySupplier<MenuType<SP7Container>> SP7_CONTAINER = CONTAINERS.register("sp7_container", () -> MenuRegistry.ofExtended(SP7Container::new));
  public static final RegistrySupplier<MenuType<SP8Container>> SP8_CONTAINER = CONTAINERS.register("sp8_container", () -> MenuRegistry.ofExtended(SP8Container::new));
  public static final RegistrySupplier<MenuType<IronFurnaceContainer>> IRON_FURNACE_CONTAINER = CONTAINERS.register("iron_furnace_container", () -> MenuRegistry.ofExtended(IronFurnaceContainer::new));

  public static void register() {
    CONTAINERS.register();
  }
}
