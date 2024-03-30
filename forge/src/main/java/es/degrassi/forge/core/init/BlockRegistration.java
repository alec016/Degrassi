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

  public static final RegistrySupplier<FurnaceBlock> IRON_FURNACE;
  public static final RegistrySupplier<FurnaceBlock> GOLD_FURNACE;
  public static final RegistrySupplier<FurnaceBlock> DIAMOND_FURNACE;
  public static final RegistrySupplier<FurnaceBlock> EMERALD_FURNACE;
  public static final RegistrySupplier<FurnaceBlock> NETHERITE_FURNACE;

//  public static final RegistrySupplier<SolarPanelBlock> SP1;
//  public static final RegistrySupplier<SolarPanelBlock> SP2;
//  public static final RegistrySupplier<SolarPanelBlock> SP3;
//  public static final RegistrySupplier<SolarPanelBlock> SP4;
//  public static final RegistrySupplier<SolarPanelBlock> SP5;
//  public static final RegistrySupplier<SolarPanelBlock> SP6;
//  public static final RegistrySupplier<SolarPanelBlock> SP7;
//  public static final RegistrySupplier<SolarPanelBlock> SP8;
//
  public static final VarReg<SolarPanel, Block> SP;

  public static final VarReg<CableTier, Block> ENERGY_CABLE;
  public static final VarReg<CableTier, Block> FLUID_CABLE;

  // Furnaces
  static {
    IRON_FURNACE = BLOCKS.register("iron_furnace", () -> new FurnaceBlock(
      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
      Furnace.IRON
    ));
    GOLD_FURNACE = BLOCKS.register("gold_furnace", () -> new FurnaceBlock(
      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
      Furnace.GOLD
    ));
    DIAMOND_FURNACE = BLOCKS.register("diamond_furnace", () -> new FurnaceBlock(
      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
      Furnace.DIAMOND
    ));
    EMERALD_FURNACE = BLOCKS.register("emerald_furnace", () -> new FurnaceBlock(
      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
      Furnace.EMERALD
    ));
    NETHERITE_FURNACE = BLOCKS.register("netherite_furnace", () -> new FurnaceBlock(
      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
      Furnace.NETHERITE
    ));
  }

  // Solar Panels
//  static {
//    SP1 = BLOCKS.register("sp1", () -> new SolarPanelBlock(
//      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
//      SolarPanel.T1
//    ));
//    SP2 = BLOCKS.register("sp2", () -> new SolarPanelBlock(
//      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
//      SolarPanel.T2
//    ));
//    SP3 = BLOCKS.register("sp3", () -> new SolarPanelBlock(
//      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
//      SolarPanel.T3
//    ));
//    SP4 = BLOCKS.register("sp4", () -> new SolarPanelBlock(
//      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
//      SolarPanel.T4
//    ));
//    SP5 = BLOCKS.register("sp5", () -> new SolarPanelBlock(
//      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
//      SolarPanel.T5
//    ));
//    SP6 = BLOCKS.register("sp6", () -> new SolarPanelBlock(
//      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
//      SolarPanel.T6
//    ));
//    SP7 = BLOCKS.register("sp7", () -> new SolarPanelBlock(
//      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
//      SolarPanel.T7
//    ));
//    SP8 = BLOCKS.register("sp8", () -> new SolarPanelBlock(
//      BlockBehaviour.Properties.of().destroyTime(500).requiresCorrectToolForDrops().dynamicShape().noOcclusion(),
//      SolarPanel.T8
//    ));
//  }

  // VarReg
  static {
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
//  public static @Unmodifiable List<RegistrySupplier<SolarPanelBlock>> listPanels() {
//    return List.of(
//      SP1,
//      SP2,
//      SP3,
//      SP4,
//      SP5,
//      SP6,
//      SP7,
//      SP8
//    );
//  }

  public static @Unmodifiable List<SolarPanelBlock> listPanels() {
    return SP.getAll().stream().map(b -> (SolarPanelBlock) b).toList();
  }
}
