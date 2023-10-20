package es.degrassi.forge.init.registration;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.registries.Registries;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import es.degrassi.Degrassi;
import es.degrassi.forge.client.ClientHandler;

@SuppressWarnings("unused")
public class Register {
  public static final Registries REGISTRIES = Registries.get(Degrassi.MODID);

  public static void register() {
    BlockRegister.register();
    ItemRegister.register();
    EntityRegister.register();
    RecipeRegistry.register();
    ContainerRegistry.register();
    CreativeTabs.register();

    LifecycleEvent.SETUP.register(Degrassi::setup);

    EnvExecutor.runInEnv(Env.CLIENT, () -> ClientHandler::init);
  }
}
