package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.block.ChestBlock;
import es.degrassi.forge.core.common.machines.block.FurnaceBlock;
import es.degrassi.forge.core.common.machines.block.MachineCasing;
import es.degrassi.forge.core.common.machines.block.SolarPanelBlock;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.EnergyHatch;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.FluidInputTank;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.FluidOutputTank;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.InputBus;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.MelterFrame;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.OutputBus;
import es.degrassi.forge.core.common.storage.energy.block.EnergyCell;
import es.degrassi.forge.core.common.storage.fluid.block.FluidTank;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.core.tiers.SolarPanel;
import es.degrassi.common.registry.VarReg;
import es.degrassi.forge.core.tiers.Storage;
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
  public static final RegistrySupplier<MelterFrame> MELTER_FRAME = BLOCKS.register("melter_frame", () -> new MelterFrame(commonBlock(500)));

  public static final VarReg<Furnace, Block> FURNACE;

  public static final VarReg<SolarPanel, Block> SP;
  public static final VarReg<Chest, Block> CHEST;

  public static final VarReg<Storage.Energy, Block> ENERGY_CELL;
  public static final VarReg<Storage.Fluid, Block> FLUID_TANK;

  // Multiblock parts
  public static final VarReg<MultiblockPartStorage.Energy, Block> ENERGY_HATCH;
  public static final VarReg<MultiblockPartStorage.Fluid.Input, Block> FLUID_INPUT_TANK;
  public static final VarReg<MultiblockPartStorage.Fluid.Output, Block> FLUID_OUTPUT_TANK;
  public static final VarReg<MultiblockPartStorage.Item.Input, Block> INPUT_BUS;
  public static final VarReg<MultiblockPartStorage.Item.Output, Block> OUTPUT_BUS;

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
    // Chest
    CHEST = new VarReg<>(BLOCKS, "chest", variant -> new ChestBlock(
      commonBlock(600),
      variant
    ), Chest.getNormalVariants());

    // Storages
    ENERGY_CELL = new VarReg<>(BLOCKS, "energy_cell", variant -> new EnergyCell(
      commonBlock(500),
      variant
    ), Storage.Energy.getNormalVariants());

    FLUID_TANK = new VarReg<>(BLOCKS, "fluid_tank", variant -> new FluidTank(
      commonBlock(500),
      variant
    ), Storage.Fluid.getNormalVariants());
  }

  // Multiblock parts
  static {
    ENERGY_HATCH = new VarReg<>(BLOCKS, "energy_hatch", variant -> new EnergyHatch(
      commonBlock(550),
      variant
    ), MultiblockPartStorage.Energy.getNormalVariants());
    FLUID_INPUT_TANK = new VarReg<>(BLOCKS, "fluid_input_tank", variant -> new FluidInputTank(
      commonBlock(550),
      variant
    ), MultiblockPartStorage.Fluid.Input.getNormalVariants());
    FLUID_OUTPUT_TANK = new VarReg<>(BLOCKS, "fluid_output_tank", variant -> new FluidOutputTank(
      commonBlock(550),
      variant
    ), MultiblockPartStorage.Fluid.Output.getNormalVariants());
    INPUT_BUS = new VarReg<>(BLOCKS, "input_bus", variant -> new InputBus(
      commonBlock(550),
      variant
    ), MultiblockPartStorage.Item.Input.getNormalVariants());
    OUTPUT_BUS = new VarReg<>(BLOCKS, "output_bus", variant -> new OutputBus(
      commonBlock(550),
      variant
    ), MultiblockPartStorage.Item.Output.getNormalVariants());
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
