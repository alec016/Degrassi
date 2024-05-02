package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.common.machines.entity.ChestEntity;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.TestMultiblockEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.EnergyHatchEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.FluidInputTankEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.FluidOutputTankEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.InputBusEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.OutputBusEntity;
import es.degrassi.forge.core.common.storage.energy.entity.EnergyCellEntity;
import es.degrassi.forge.core.common.storage.fluid.entity.FluidTankEntity;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.core.tiers.SolarPanel;
import es.degrassi.forge.core.tiers.Storage;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EntityRegistration {
  public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(Degrassi.MODID, Registries.BLOCK_ENTITY_TYPE);

  public static final Supplier<BlockEntityType<FurnaceEntity>> FURNACE;

  public static final Supplier<BlockEntityType<SolarPanelEntity>> SP;
  public static final Supplier<BlockEntityType<ChestEntity>> CHEST;
  public static final Supplier<BlockEntityType<EnergyCellEntity>> ENERGY_CELL;
  public static final Supplier<BlockEntityType<FluidTankEntity>> FLUID_TANK;
  public static final Supplier<BlockEntityType<EnergyHatchEntity>> ENERGY_HATCH;
  public static final Supplier<BlockEntityType<FluidInputTankEntity>> FLUID_INPUT_TANK;
  public static final Supplier<BlockEntityType<FluidOutputTankEntity>> FLUID_OUTPUT_TANK;
  public static final Supplier<BlockEntityType<InputBusEntity>> INPUT_BUS;
  public static final Supplier<BlockEntityType<OutputBusEntity>> OUTPUT_BUS;

  static {
    FURNACE = register("furnace", (pos, state) -> EnvHandler.INSTANCE.createFurnace(pos, state, Furnace.IRON), BlockRegistration.FURNACE::getAll);
    SP = register("sp", (pos, state) -> EnvHandler.INSTANCE.createSP(pos, state, SolarPanel.T1), BlockRegistration.SP::getAll);
    CHEST = register("chest", (pos, state) -> EnvHandler.INSTANCE.createChest(pos, state, Chest.IRON), BlockRegistration.CHEST::getAll);
    ENERGY_CELL = register("energy_cell", (pos, state) -> EnvHandler.INSTANCE.createEnergyCell(pos, state, Storage.Energy.BASIC), BlockRegistration.ENERGY_CELL::getAll);
    FLUID_TANK = register("fluid_tank", (pos, state) -> EnvHandler.INSTANCE.createFluidTank(pos, state, Storage.Fluid.BASIC), BlockRegistration.FLUID_TANK::getAll);
    // multiblock parts
    ENERGY_HATCH = register("energy_hatch", (pos, state) -> EnvHandler.INSTANCE.createEnergyHatch(pos, state, MultiblockPartStorage.Energy.BASIC), BlockRegistration.ENERGY_HATCH::getAll);
    FLUID_INPUT_TANK = register("fluid_input_tank", (pos, state) -> EnvHandler.INSTANCE.createFluidInputTank(pos, state, MultiblockPartStorage.Fluid.Input.BASIC), BlockRegistration.FLUID_INPUT_TANK::getAll);
    FLUID_OUTPUT_TANK = register("fluid_output_tank", (pos, state) -> EnvHandler.INSTANCE.createFluidOutputTank(pos, state, MultiblockPartStorage.Fluid.Output.BASIC), BlockRegistration.FLUID_OUTPUT_TANK::getAll);
    INPUT_BUS = register("input_bus", (pos, state) -> EnvHandler.INSTANCE.createInputBus(pos, state, MultiblockPartStorage.Item.Input.BASIC), BlockRegistration.INPUT_BUS::getAll);
    OUTPUT_BUS = register("output_bus", (pos, state) -> EnvHandler.INSTANCE.createOutputBus(pos, state, MultiblockPartStorage.Item.Output.BASIC), BlockRegistration.OUTPUT_BUS::getAll);
  }

  private static <BE extends BlockEntity> Supplier<BlockEntityType<BE>> register(String path, BlockEntityType.BlockEntitySupplier<BE> supplier, Supplier<List<Block>> blocks) {
    return ENTITIES.register(path, () -> BlockEntityType.Builder.of(supplier, blocks.get().toArray(Block[]::new)).build(null));
  }
}
