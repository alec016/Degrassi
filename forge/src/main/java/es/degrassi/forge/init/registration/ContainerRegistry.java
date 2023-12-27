package es.degrassi.forge.init.registration;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.gui.container.FurnaceContainer;
import es.degrassi.forge.init.gui.container.MelterContainer;
import es.degrassi.forge.init.gui.container.generators.JewelryGeneratorContainer;
import es.degrassi.forge.init.gui.container.panel.SolarPanelContainer;
import es.degrassi.forge.init.gui.container.UpgradeMakerContainer;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class ContainerRegistry {
  public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Degrassi.MODID, Registry.MENU_REGISTRY);
  // panel
  // sp
  public static final RegistrySupplier<MenuType<SolarPanelContainer>> SP_CONTAINER = CONTAINERS.register("sp_container", () -> MenuRegistry.ofExtended(SolarPanelContainer::new));

  // furnace
  public static final RegistrySupplier<MenuType<FurnaceContainer>> FURNACE_CONTAINER = CONTAINERS.register("furnace_container", () -> MenuRegistry.ofExtended(FurnaceContainer::new));

  // melter
  public static final RegistrySupplier<MenuType<MelterContainer>> MELTER_CONTAINER = CONTAINERS.register("melter_container", () -> MenuRegistry.ofExtended(MelterContainer::new));

  // upgrade maker
  public static final RegistrySupplier<MenuType<UpgradeMakerContainer>> UPGRADE_MAKER_CONTAINER = CONTAINERS.register("upgrade_maker_container", () -> MenuRegistry.ofExtended(UpgradeMakerContainer::new));

  // generators
  public static final RegistrySupplier<MenuType<JewelryGeneratorContainer>> JEWELRY_GENERATOR = CONTAINERS.register("jewelry_generator_container", () -> MenuRegistry.ofExtended(JewelryGeneratorContainer::new));

  public static void register() {
    CONTAINERS.register();
  }
}
