package es.degrassi.forge.init.registration;

import es.degrassi.forge.Degrassi;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraftforge.eventbus.api.IEventBus;

public class Register {
  public static final RegistrarManager REGISTRIES = RegistrarManager.get(Degrassi.MODID);

  public static void register() {

//    BlockRegistration.BLOCKS.register();
//    ItemRegistration.ITEMS.register();
//    EntityRegistration.TILE_ENTITIES.register();
//    ContainerRegistration.CONTAINERS.register();
//    RecipeRegistration.RECIPE_SERIALIZERS.register();
//    RecipeRegistration.RECIPE_TYPES.register();
    ElementRegistration.GUI_ELEMENTS.register();
    ComponentRegistration.MACHINE_COMPONENTS.register();
//    RequirementRegistration.REQUIREMENTS.register();
    DataRegistration.DATAS.register();
//    ProcessorsRegistration.PROCESSORS.register();
  }

  public static void register(IEventBus bus) {
//    FluidTypeRegister.register(bus);
    register();
  }
}
