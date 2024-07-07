package es.degrassi.forge;

import es.degrassi.common.registry.IBlock;
import es.degrassi.forge.core.common.machines.entity.ChestEntity;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.MelterController;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.MelterControllerEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.EnergyHatchEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.FluidInputTankEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.FluidOutputTankEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.InputBusEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.OutputBusEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.QuadrupleFluidInputTankEntity;
import es.degrassi.forge.core.common.storage.energy.entity.EnergyCellEntity;
import es.degrassi.forge.core.common.storage.fluid.entity.FluidTankEntity;
import es.degrassi.forge.core.digital.block.entity.DigitalControllerEntity;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.core.tiers.SolarPanel;
import es.degrassi.forge.core.tiers.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class EnvHandler {
  public static final EnvHandler INSTANCE = new EnvHandler();
  private final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

  private EnvHandler() {}

  public boolean hasEnergy(Level level, BlockPos pos, Direction side) {
    var be = level.getBlockEntity(pos);
    return be != null && be.getCapability(ForgeCapabilities.ENERGY, side).isPresent();
  }

  public boolean hasFluid(Level level, BlockPos pos, Direction side) {
    var be = level.getBlockEntity(pos);
    return be != null && be.getCapability(ForgeCapabilities.FLUID_HANDLER, side).isPresent();
  }

  public SolarPanelEntity createSP(BlockPos pos, BlockState state, SolarPanel solarPanel) {
    return new SolarPanelEntity(pos, state, solarPanel);
  }

  public FurnaceEntity createFurnace(BlockPos pos, BlockState state, Furnace furnace) {
    return new FurnaceEntity(pos, state, furnace);
  }

  public ChestEntity createChest(BlockPos pos, BlockState state, Chest tier) {
    return new ChestEntity(pos, state, tier);
  }

  public EnergyCellEntity createEnergyCell(BlockPos pos, BlockState state, Storage.Energy tier) {
    return new EnergyCellEntity(pos, state, tier);
  }

  public FluidTankEntity createFluidTank(BlockPos pos, BlockState state, Storage.Fluid tier) {
    return new FluidTankEntity(pos, state, tier);
  }

  public EnergyHatchEntity createEnergyHatch(BlockPos pos, BlockState state, MultiblockPartStorage.Energy variant) {
    return new EnergyHatchEntity(pos, state, variant);
  }

  public FluidInputTankEntity createFluidInputTank(BlockPos pos, BlockState state, MultiblockPartStorage.Fluid.Input variant) {
    return new FluidInputTankEntity(pos, state, variant);
  }

  public QuadrupleFluidInputTankEntity createQuadrupleFluidInputTank(BlockPos pos, BlockState state, MultiblockPartStorage.Fluid.QuadrupleInput variant) {
    return new QuadrupleFluidInputTankEntity(pos, state, variant);
  }

  public FluidOutputTankEntity createFluidOutputTank(BlockPos pos, BlockState state, MultiblockPartStorage.Fluid.Output variant) {
    return new FluidOutputTankEntity(pos, state, variant);
  }

  public InputBusEntity createInputBus(BlockPos pos, BlockState state, MultiblockPartStorage.Item.Input variant) {
    return new InputBusEntity(pos, state, variant);
  }

  public OutputBusEntity createOutputBus(BlockPos pos, BlockState state, MultiblockPartStorage.Item.Output variant) {
    return new OutputBusEntity(pos, state, variant);
  }

  // multiblock controllers

  public MelterControllerEntity createMelterControllerEntity(BlockPos pos, BlockState state, MelterController block) {
    return new MelterControllerEntity(pos, state, block);
  }

  public void setupBlockItems() {
    modEventBus.addListener((RegisterEvent event) -> {
      if (event.getRegistryKey() == Registries.ITEM) {
        var registry = event.getForgeRegistry();
        for (var block : ForgeRegistries.BLOCKS.getValues()) {
          if (block instanceof IBlock<?, ?> iBlock) {
            var blockItem = iBlock.getBlockItem(new Item.Properties());
            var name = BuiltInRegistries.BLOCK.getKey(block);
            if (registry == null) continue;
            registry.register(name, blockItem);
          }
        }
      }
    });
  }

  public DigitalControllerEntity createDigitalController(BlockPos pos, BlockState state) {
    return new DigitalControllerEntity(pos, state);
  }
}
