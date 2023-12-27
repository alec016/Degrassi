package es.degrassi.forge.init.registration;

import com.mojang.math.Vector3f;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.fluid.types.MoltenRedMatterType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidTypeRegister {
  public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Degrassi.MODID);

  public static final RegistryObject<FluidType> MOLTEN_RED_MATTER = FLUID_TYPES.register(
    "molten_red_matter",
    () -> new MoltenRedMatterType(
      FluidRegister.MOLTEN_RED_MATTER_RL,
      FluidRegister.MOLTEN_RED_MATTER_FLOWING_RL,
      FluidRegister.MOLTEN_RED_MATTER_OVERLAY_RL,
      new Vector3f(
        224f / 255f,
        56f / 255f,
        208f / 255f
      ),
      FluidType.Properties.create()
        .lightLevel(13)
        .density(5000)
        .viscosity(5000)
        .canDrown(true)
        .supportsBoating(false)
        .temperature(875)
        .canSwim(true)
        .canPushEntity(true)
        .canHydrate(false)
        .canConvertToSource(false)
    )
  );

  public static void register(IEventBus bus) {
    FLUID_TYPES.register(bus);
  }
}
