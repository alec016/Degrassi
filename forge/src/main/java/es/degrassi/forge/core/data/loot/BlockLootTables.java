package es.degrassi.forge.core.data.loot;

import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.core.tiers.SolarPanel;
import es.degrassi.forge.core.tiers.Storage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockLootTables extends BlockLootSubProvider {
  public BlockLootTables() {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags());
  }

  @Override
  public void generate() {
    dropSelf(BlockRegistration.MACHINE_CASING.get());
    dropSelf(BlockRegistration.MELTER_FRAME.get());
    for(Furnace tier : Furnace.values()) {
      dropSelf(BlockRegistration.FURNACE.get(tier));
    }
    for (SolarPanel tier : SolarPanel.values()) {
      dropSelf(BlockRegistration.SP.get(tier));
    }
    for (Chest tier : Chest.values()) {
      dropSelf(BlockRegistration.CHEST.get(tier));
    }
    for (Storage.Energy tier : Storage.Energy.values()) {
      dropSelf(BlockRegistration.ENERGY_CELL.get(tier));
    }
    for (Storage.Fluid tier : Storage.Fluid.values()) {
      dropSelf(BlockRegistration.FLUID_TANK.get(tier));
    }
    for (MultiblockPartStorage.Energy tier : MultiblockPartStorage.Energy.values()) {
      dropSelf(BlockRegistration.ENERGY_HATCH.get(tier));
    }
    for (MultiblockPartStorage.Fluid.Input tier : MultiblockPartStorage.Fluid.Input.values()) {
      dropSelf(BlockRegistration.FLUID_INPUT_TANK.get(tier));
    }
    for (MultiblockPartStorage.Fluid.Output tier : MultiblockPartStorage.Fluid.Output.values()) {
      dropSelf(BlockRegistration.FLUID_OUTPUT_TANK.get(tier));
    }
    for (MultiblockPartStorage.Item.Input tier : MultiblockPartStorage.Item.Input.values()) {
      dropSelf(BlockRegistration.INPUT_BUS.get(tier));
    }
    for (MultiblockPartStorage.Item.Output tier : MultiblockPartStorage.Item.Output.values()) {
      dropSelf(BlockRegistration.OUTPUT_BUS.get(tier));
    }
  }

  @Override
  public @NotNull Iterable<Block> getKnownBlocks() {
    List<Block> blocks = new ArrayList<>();
    BlockRegistration.BLOCKS.forEach(block -> blocks.add(block.get()));
    return blocks;
  }
}
