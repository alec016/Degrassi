package es.degrassi.forge.init.registration;

import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.ForgeFlowingFluid;

@SuppressWarnings("unused")
public class FluidRegister {
  public static final ResourceLocation MOLTEN_RED_MATTER_RL = new DegrassiLocation("fluid/still/molten_red_matter");
  public static final ResourceLocation MOLTEN_RED_MATTER_FLOWING_RL = new DegrassiLocation("fluid/flowing/molten_red_matter");
  public static final ResourceLocation MOLTEN_RED_MATTER_OVERLAY_RL = new DegrassiLocation("fluid/overlay/molten_red_matter");
  public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Degrassi.MODID, Registry.FLUID_REGISTRY);

  public static final RegistrySupplier<FlowingFluid> MOLTEN_RED_MATTER = FLUIDS.register(
    "molten_red_matter",
    () -> new ForgeFlowingFluid.Source(FluidRegister.MOLTEN_RED_MATTER_ATTRIBUTES)
  );

  public static final RegistrySupplier<FlowingFluid> MOLTEN_RED_MATTER_FLOWING = FLUIDS.register(
    "molten_red_matter_flowing",
    () -> new ForgeFlowingFluid.Flowing(FluidRegister.MOLTEN_RED_MATTER_ATTRIBUTES)
  );

  public static final ForgeFlowingFluid.Properties MOLTEN_RED_MATTER_ATTRIBUTES = new ForgeFlowingFluid.Properties(
    FluidTypeRegister.MOLTEN_RED_MATTER,
    FluidRegister.MOLTEN_RED_MATTER,
    FluidRegister.MOLTEN_RED_MATTER_FLOWING
  ).block(BlockRegister.MOLTEN_RED_MATTER_BLOCK)
    .bucket(ItemRegister.MOLTEN_RED_MATTER_BUCKET)
    .slopeFindDistance(2)
    .levelDecreasePerBlock(2);

//  public static final ArchitecturyFluidAttributes MOLTEN_RED_MATTER_ATTRIBUTES = SimpleArchitecturyFluidAttributes.of(
//      FluidRegister.MOLTEN_RED_MATTER_FLOWING,
//      FluidRegister.MOLTEN_RED_MATTER
//    )
//    .block(BlockRegister.MOLTEN_RED_MATTER_BLOCK)
//    .flowingTexture(FluidRegister.MOLTEN_RED_MATTER_FLOWING_RL)
//    .sourceTexture(FluidRegister.MOLTEN_RED_MATTER_RL)
//    .overlayTexture(FluidRegister.MOLTEN_RED_MATTER_OVERLAY_RL)
//    .convertToSource(true)
//    .lighterThanAir(false)
//    .luminosity(13)
//    .density(5000)
//    .viscosity(5000)
//    .temperature(875)
//    .rarity(Rarity.COMMON)
//    .bucketItem(ItemRegister.MOLTEN_RED_MATTER_BUCKET);

  public static void register() {
//    FLUIDS.register();
  }

  public static Fluid get(String name) {
    if (name == null) return null;
    return switch(name) {
      case "water" -> Fluids.WATER;
      case "lava" -> Fluids.LAVA;
      case "red_matter", "molten_red_matter" -> FluidRegister.MOLTEN_RED_MATTER.get();
      default -> null;
    };
  }
}
