package es.degrassi.forge.core.data.loot;

import es.degrassi.forge.core.init.BlockRegistration;
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
    dropSelf(BlockRegistration.IRON_FURNACE.get());
    dropSelf(BlockRegistration.GOLD_FURNACE.get());
    dropSelf(BlockRegistration.DIAMOND_FURNACE.get());
    dropSelf(BlockRegistration.EMERALD_FURNACE.get());
    dropSelf(BlockRegistration.NETHERITE_FURNACE.get());
    dropSelf(BlockRegistration.SP1.get());
    dropSelf(BlockRegistration.SP2.get());
    dropSelf(BlockRegistration.SP3.get());
    dropSelf(BlockRegistration.SP4.get());
    dropSelf(BlockRegistration.SP5.get());
    dropSelf(BlockRegistration.SP6.get());
    dropSelf(BlockRegistration.SP7.get());
    dropSelf(BlockRegistration.SP8.get());
  }

  @Override
  public @NotNull Iterable<Block> getKnownBlocks() {
    List<Block> blocks = new ArrayList<>();
    BlockRegistration.BLOCKS.forEach(block -> blocks.add(block.get()));
    return blocks;
  }
}
