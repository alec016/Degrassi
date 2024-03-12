package es.degrassi.forge.core.data;

import es.degrassi.forge.core.data.loot.BlockLootTables;
import java.util.List;
import java.util.Set;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class DegrassiLootTableProvider {
  public static LootTableProvider create(PackOutput output) {
    return new LootTableProvider(output, Set.of(), List.of(
      new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)
    ));
  }
}
