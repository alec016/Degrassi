package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.block.ChestBlock;
import es.degrassi.forge.core.common.machines.block.FurnaceBlock;
import es.degrassi.forge.core.common.machines.block.MachineCasing;
import es.degrassi.forge.core.common.machines.block.SolarPanelBlock;
import es.degrassi.forge.core.common.storage.energy.block.EnergyCell;
import es.degrassi.forge.core.common.storage.fluid.block.FluidTank;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
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

  public static final VarReg<Furnace, Block> FURNACE;

  public static final VarReg<SolarPanel, Block> SP;
  public static final VarReg<Chest, Block> CHEST;

  public static final VarReg<Storage.Energy, Block> ENERGY_CELL;
  public static final VarReg<Storage.Fluid, Block> FLUID_TANK;

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
