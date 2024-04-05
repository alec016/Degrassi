package es.degrassi.forge.core.data.loot;

import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.SolarPanel;
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
    for(Furnace tier : Furnace.values()) {
      dropSelf(BlockRegistration.FURNACE.get(tier));
    }
    for (SolarPanel tier : SolarPanel.values()) {
      dropSelf(BlockRegistration.SP.get(tier));
    }
    for (Chest tier : Chest.values()) {
      dropSelf(BlockRegistration.CHEST.get(tier));
    }
  }

  @Override
  public @NotNull Iterable<Block> getKnownBlocks() {
    List<Block> blocks = new ArrayList<>();
    BlockRegistration.BLOCKS.forEach(block -> blocks.add(block.get()));
    return blocks;
  }
}
