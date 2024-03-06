package es.degrassi.forge.core.init;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrarManager;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.screen.FurnaceScreen;
import net.minecraftforge.eventbus.api.IEventBus;

public class Registration {
  public static final RegistrarManager REGISTRIES = RegistrarManager.get(Degrassi.MODID);

  public static void register (IEventBus bus) {
    BlockRegistration.BLOCKS.register();
    ItemRegistration.ITEMS.register();
    EntityRegistration.ENTITIES.register();
    ContainerRegistration.MENUS.register();

    LifecycleEvent.SETUP.register(Degrassi::setup);
  }
}
