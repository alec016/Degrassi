package es.degrassi.forge.data.loot;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import net.minecraft.data.loot.ChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

public class DegrassiChestLootTables extends ChestLoot {
  private static final ResourceLocation CUSTOM_CHEST_LOOT =
    new DegrassiLocation("chests/custom_chest_loot");

  @Override
  public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
//    consumer.accept(CUSTOM_CHEST_LOOT, LootTable.lootTable()
//      .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 8.0F))
//        .add(LootItem.lootTableItem(Items.APPLE).setWeight(15)
//          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
//        .add(LootItem.lootTableItem(Items.COAL).setWeight(15)
//          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
//        .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10)
//          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
//        .add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(2))
//        .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10))
//        .add(LootItem.lootTableItem(Items.EMERALD))
//        .add(LootItem.lootTableItem(Items.WHEAT).setWeight(10)
//          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))))
//      .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
//        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))));
  }
}
