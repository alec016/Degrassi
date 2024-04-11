package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.common.machines.entity.ChestEntity;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import es.degrassi.forge.core.common.storage.energy.entity.EnergyCellEntity;
import es.degrassi.forge.core.common.storage.fluid.entity.FluidTankEntity;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
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

  static {
    FURNACE = register("furnace", (pos, state) -> EnvHandler.INSTANCE.createFurnace(pos, state, Furnace.IRON), BlockRegistration.FURNACE::getAll);
    SP = register("sp", (pos, state) -> EnvHandler.INSTANCE.createSP(pos, state, SolarPanel.T1), BlockRegistration.SP::getAll);
    CHEST = register("chest", (pos, state) -> EnvHandler.INSTANCE.createChest(pos, state, Chest.IRON), BlockRegistration.CHEST::getAll);
    ENERGY_CELL = register("energy_cell", (pos, state) -> EnvHandler.INSTANCE.createEnergyCell(pos, state, Storage.Energy.BASIC), BlockRegistration.ENERGY_CELL::getAll);
    FLUID_TANK = register("fluid_tank", (pos, state) -> EnvHandler.INSTANCE.createFluidTank(pos, state, Storage.Fluid.BASIC), BlockRegistration.FLUID_TANK::getAll);
  }

  private static <BE extends BlockEntity> Supplier<BlockEntityType<BE>> register(String path, BlockEntityType.BlockEntitySupplier<BE> supplier, Supplier<List<Block>> blocks) {
    return ENTITIES.register(path, () -> BlockEntityType.Builder.of(supplier, blocks.get().toArray(Block[]::new)).build(null));
  }
}
