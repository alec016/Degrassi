package es.degrassi.forge.core.data.loot;

import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.CableTier;
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
    dropSelf(BlockRegistration.IRON_FURNACE.get());
    dropSelf(BlockRegistration.GOLD_FURNACE.get());
    dropSelf(BlockRegistration.DIAMOND_FURNACE.get());
    dropSelf(BlockRegistration.EMERALD_FURNACE.get());
    dropSelf(BlockRegistration.NETHERITE_FURNACE.get());
    dropSelf(BlockRegistration.SP.get(SolarPanel.T1));
    dropSelf(BlockRegistration.SP.get(SolarPanel.T2));
    dropSelf(BlockRegistration.SP.get(SolarPanel.T3));
    dropSelf(BlockRegistration.SP.get(SolarPanel.T4));
    dropSelf(BlockRegistration.SP.get(SolarPanel.T5));
    dropSelf(BlockRegistration.SP.get(SolarPanel.T6));
    dropSelf(BlockRegistration.SP.get(SolarPanel.T7));
    dropSelf(BlockRegistration.SP.get(SolarPanel.T8));
    dropSelf(BlockRegistration.ENERGY_CABLE.get(CableTier.BASIC));
    dropSelf(BlockRegistration.ENERGY_CABLE.get(CableTier.ADVANCE));
    dropSelf(BlockRegistration.ENERGY_CABLE.get(CableTier.EXTREME));
    dropSelf(BlockRegistration.FLUID_CABLE.get(CableTier.BASIC));
    dropSelf(BlockRegistration.FLUID_CABLE.get(CableTier.ADVANCE));
    dropSelf(BlockRegistration.FLUID_CABLE.get(CableTier.EXTREME));
  }

  @Override
  public @NotNull Iterable<Block> getKnownBlocks() {
    List<Block> blocks = new ArrayList<>();
    BlockRegistration.BLOCKS.forEach(block -> blocks.add(block.get()));
    return blocks;
  }
}
