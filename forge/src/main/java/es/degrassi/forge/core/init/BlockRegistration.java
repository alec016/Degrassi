package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.cables.energy.EnergyCableBlock;
import es.degrassi.forge.core.common.cables.fluid.FluidCableBlock;
import es.degrassi.forge.core.common.machines.block.FurnaceBlock;
import es.degrassi.forge.core.common.machines.block.MachineCasing;
import es.degrassi.forge.core.common.machines.block.SolarPanelBlock;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.SolarPanel;
import es.degrassi.common.registry.VarReg;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

public class BlockRegistration {
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Degrassi.MODID, Registries.BLOCK);


  public static final RegistrySupplier<Block> MACHINE_CASING = BLOCKS.register("machine_casing", MachineCasing::new);

  public static final VarReg<Furnace, Block> FURNACE;

  public static final VarReg<SolarPanel, Block> SP;

  public static final VarReg<CableTier, Block> ENERGY_CABLE;
  public static final VarReg<CableTier, Block> FLUID_CABLE;

  // VarReg
  static {
    // Furnace
    FURNACE = new VarReg<>(BLOCKS, "furnace", variant -> new FurnaceBlock(
      commonBlock(550),
      variant
    ), Furnace.getNormalVariants());
    // SP
    SP = new VarReg<>(BLOCKS, "sp", variant -> new SolarPanelBlock(
      commonBlock(500),
      variant
    ), SolarPanel.getNormalVariants());
    // ENERGY
    ENERGY_CABLE = new VarReg<>(BLOCKS, "energy_cable", variant -> new EnergyCableBlock(metalNoSolid(2.0f, 20.0f), variant), CableTier.getNormalVariants());
    FLUID_CABLE = new VarReg<>(BLOCKS, "fluid_cable", variant -> new FluidCableBlock(metalNoSolid(2.0f, 20.0f), variant), CableTier.getNormalVariants());
  }

  private static BlockBehaviour.Properties metalNoSolid(float hardness, float resistance) {
    return BlockBehaviour.Properties.of()
      .mapColor(MapColor.METAL)
      .sound(SoundType.METAL)
      .strength(hardness, resistance)
      .requiresCorrectToolForDrops()
      .noCollission()
      .noOcclusion();
  }

  private static BlockBehaviour.Properties commonBlock(float destroyTime) {
    return BlockBehaviour.Properties.of().destroyTime(destroyTime).requiresCorrectToolForDrops().dynamicShape().noOcclusion();
  }

  @Contract(value = " -> new", pure = true)
  public static @Unmodifiable List<SolarPanelBlock> listPanels() {
    return SP.getAll().stream().map(b -> (SolarPanelBlock) b).toList();
  }
}
