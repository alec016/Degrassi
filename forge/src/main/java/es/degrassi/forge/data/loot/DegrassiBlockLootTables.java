package es.degrassi.forge.data.loot;

import es.degrassi.forge.init.registration.BlockRegister;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DegrassiBlockLootTables extends BlockLoot {

//  private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[] { 0.05F, 0.0625F, 0.083333336F, 0.1F };

  @Override
  protected void addTables() {
    dropSelf(BlockRegister.SP1_BLOCK.get());
    dropSelf(BlockRegister.SP2_BLOCK.get());
    dropSelf(BlockRegister.SP3_BLOCK.get());
    dropSelf(BlockRegister.SP4_BLOCK.get());
    dropSelf(BlockRegister.SP5_BLOCK.get());
    dropSelf(BlockRegister.SP6_BLOCK.get());
    dropSelf(BlockRegister.SP7_BLOCK.get());
    dropSelf(BlockRegister.SP8_BLOCK.get());

    dropSelf(BlockRegister.IRON_FURNACE_BLOCK.get());
    dropSelf(BlockRegister.GOLD_FURNACE_BLOCK.get());
    dropSelf(BlockRegister.DIAMOND_FURNACE_BLOCK.get());
    dropSelf(BlockRegister.EMERALD_FURNACE_BLOCK.get());
    dropSelf(BlockRegister.NETHERITE_FURNACE_BLOCK.get());

    dropSelf(BlockRegister.MELTER_BLOCK.get());
    dropSelf(BlockRegister.UPGRADE_MAKER.get());
    dropSelf(BlockRegister.MACHINE_CASING.get());

    dropSelf(BlockRegister.JEWELRY_GENERATOR.get());
    dropSelf(BlockRegister.COMBUSTION_GENERATOR.get());
    dropSelf(BlockRegister.CIRCUIT_FABRICATOR.get());

//    this.dropSelf(ModBlocks.CITRINE_BLOCK.get());
//    this.dropSelf(ModBlocks.RAW_CITRINE_BLOCK.get());
//
//    this.add(ModBlocks.CITRINE_ORE.get(),
//      (block) -> createOreDrop(ModBlocks.CITRINE_ORE.get(), ModItems.RAW_CITRINE.get()));
//    this.add(ModBlocks.DEEPSLATE_CITRINE_ORE.get(),
//      (block) -> createOreDrop(ModBlocks.DEEPSLATE_CITRINE_ORE.get(), ModItems.RAW_CITRINE.get()));
//    this.add(ModBlocks.NETHERRACK_CITRINE_ORE.get(),
//      (block) -> createOreDrop(ModBlocks.NETHERRACK_CITRINE_ORE.get(), ModItems.RAW_CITRINE.get()));
//    this.add(ModBlocks.ENDSTONE_CITRINE_ORE.get(),
//      (block) -> createOreDrop(ModBlocks.ENDSTONE_CITRINE_ORE.get(), ModItems.RAW_CITRINE.get()));
//
//
//    this.dropSelf(ModBlocks.SPEEDY_BLOCK.get());
//    this.dropSelf(ModBlocks.CITRINE_STAIRS.get());
//
//    this.add(ModBlocks.CITRINE_SLAB.get(), BlockLoot::createSlabItemTable);
//    this.dropSelf(ModBlocks.CITRINE_FENCE.get());
//    this.dropSelf(ModBlocks.CITRINE_FENCE_GATE.get());
//    this.dropSelf(ModBlocks.CITRINE_WALL.get());
//
//    this.add(ModBlocks.EBONY_DOOR.get(), BlockLoot::createDoorTable);
//    this.add(ModBlocks.POTTED_PINK_ROSE.get(), BlockLoot::createPotFlowerItemTable);
//
//    this.dropSelf(ModBlocks.CITRINE_BUTTON.get());
//    this.dropSelf(ModBlocks.CITRINE_PRESSURE_PLATE.get());
//    this.dropSelf(ModBlocks.EBONY_TRAPDOOR.get());
//    this.dropSelf(ModBlocks.PINK_ROSE.get());
//
//    this.dropWhenSilkTouch(ModBlocks.WINTER_WINDOW.get());
//
//    this.dropSelf(ModBlocks.CITRINE_LAMP.get());
//
//    this.dropSelf(ModBlocks.EBONY_LOG.get());
//    this.dropSelf(ModBlocks.EBONY_WOOD.get());
//    this.dropSelf(ModBlocks.EBONY_PLANKS.get());
//    this.dropSelf(ModBlocks.STRIPPED_EBONY_WOOD.get());
//    this.dropSelf(ModBlocks.STRIPPED_EBONY_LOG.get());
//
//    this.add(ModBlocks.EBONY_LEAVES.get(), (block) ->
//      createLeavesDrops(block, ModBlocks.EBONY_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
//
//    this.dropSelf(ModBlocks.EBONY_SAPLING.get());
//    this.dropSelf(ModBlocks.GEM_CUTTING_STATION.get());
//
//    this.dropOther(ModBlocks.EBONY_WALL_SIGN.get(), ModItems.EBONY_SIGN.get());
//    this.dropOther(ModBlocks.EBONY_SIGN.get(), ModItems.EBONY_SIGN.get());
//
//    LootItemCondition.Builder lootitemcondition$builder = LootItemBlockStatePropertyCondition
//      .hasBlockStateProperties(ModBlocks.CUCUMBER_PLANT.get())
//      .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CucumberPlantBlock.AGE, 5));
//
//    this.add(ModBlocks.CUCUMBER_PLANT.get(), createCropDrops(ModBlocks.CUCUMBER_PLANT.get(), ModItems.CUCUMBER.get(),
//      ModItems.CUCUMBER_SEEDS.get(), lootitemcondition$builder));
  }

  @Override
  protected @NotNull Iterable<Block> getKnownBlocks() {
    List<Block> blocks = new ArrayList<>();
    BlockRegister.BLOCKS.forEach(block -> blocks.add(block.get()));
    return blocks;
  }
}
