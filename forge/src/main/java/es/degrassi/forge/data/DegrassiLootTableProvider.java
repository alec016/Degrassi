package es.degrassi.forge.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import es.degrassi.forge.data.loot.*;
import org.jetbrains.annotations.NotNull;

public class DegrassiLootTableProvider extends LootTableProvider {
  private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>>
    loot_tables = ImmutableList.of(Pair.of(DegrassiBlockLootTables::new, LootContextParamSets.BLOCK),
    Pair.of(DegrassiChestLootTables::new, LootContextParamSets.CHEST));

  public DegrassiLootTableProvider(DataGenerator pGenerator) {
    super(pGenerator);
  }


  @Override
  protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
    return loot_tables;
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationtracker) {
    map.forEach((id, table) -> LootTables.validate(validationtracker, id, table));
  }
}
