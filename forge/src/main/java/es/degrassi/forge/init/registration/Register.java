package es.degrassi.forge.init.registration;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.registries.Registries;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.client.ClientHandler;
import net.minecraftforge.eventbus.api.IEventBus;

@SuppressWarnings("unused")
public class Register {

  public static final Registries REGISTRIES = Registries.get(Degrassi.MODID);
  public static void register() {
    TagRegistry.register();

    BlockRegister.register();
    ItemRegister.register();
    FluidRegister.register();
    EntityRegister.register();
    CreativeTabs.register();

    FluidRegister.FLUIDS.register();
    BlockRegister.BLOCKS.register();
    ItemRegister.ITEMS.register();
    EntityRegister.BLOCK_ENTITIES.register();
    ContainerRegistry.CONTAINERS.register();
    RecipeRegistry.SERIALIZERS.register();
    RecipeRegistry.RECIPE_TYPES.register();
    ElementRegistry.register();
    ComponentRegistry.register();
    RequirementRegistry.register();

//    FluidRegister.register();
//    BlockRegister.register();
//    ItemRegister.register();
//    EntityRegister.register();
//    ContainerRegistry.register();
//    RecipeRegistry.register();
//    ElementRegistry.register();
//    ComponentRegistry.register();
//    RequirementRegistry.register();
//    CreativeTabs.register();

    LifecycleEvent.SETUP.register(Degrassi::setup);

    EnvExecutor.runInEnv(Env.CLIENT, () -> ClientHandler::init);
  }

  public static void register(IEventBus bus) {
    FluidTypeRegister.register(bus);
    register();
  }
}
