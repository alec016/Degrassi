package es.degrassi.forge.init.registration;

import es.degrassi.forge.Degrassi;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraftforge.eventbus.api.IEventBus;

public class Register {
  public static final RegistrarManager REGISTRIES = RegistrarManager.get(Degrassi.MODID);

  public static void register() {

  }

  public static void register(IEventBus bus) {
//    FluidTypeRegister.register(bus);
    register();
  }
}
