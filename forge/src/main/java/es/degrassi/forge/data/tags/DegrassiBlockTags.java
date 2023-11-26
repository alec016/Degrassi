package es.degrassi.forge.data.tags;

import es.degrassi.forge.data.DegrassiTagProvider;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.TagRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public final class DegrassiBlockTags extends DegrassiTagProvider<Block> {
  public DegrassiBlockTags(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, Registry.BLOCK, existingFileHelper);
  }

  @Override
  protected void addTags() {
    addForgeTags();
    addMineableTags();
    addNeedsTags();
    addCustomTags();
  }

  private void addForgeTags() {
    tag(TagRegistry.AllBlockTags.PISTONS.tag)
      .replace(false)
      .add(
        Blocks.PISTON,
        Blocks.STICKY_PISTON
      );
  }

  private void addCustomTags() {
    addBlocks();
    addFurnaces();
    addMachines();
    addPanels();
  }

  private void addPanels() {
    addSolarPanels();
  }

  private void addSolarPanels() {
    tag(TagRegistry.AllBlockTags.SOLAR_PANELS.tag)
      .replace(false)
      .add(
        BlockRegister.SP1_BLOCK.get(),
        BlockRegister.SP2_BLOCK.get(),
        BlockRegister.SP3_BLOCK.get(),
        BlockRegister.SP4_BLOCK.get(),
        BlockRegister.SP5_BLOCK.get(),
        BlockRegister.SP6_BLOCK.get(),
        BlockRegister.SP7_BLOCK.get(),
        BlockRegister.SP8_BLOCK.get()
      );
  }

  private void addMachines() {
    tag(TagRegistry.AllBlockTags.MACHINES.tag)
      .replace(false)
      .add(
        BlockRegister.SP1_BLOCK.get(),
        BlockRegister.SP2_BLOCK.get(),
        BlockRegister.SP3_BLOCK.get(),
        BlockRegister.SP4_BLOCK.get(),
        BlockRegister.SP5_BLOCK.get(),
        BlockRegister.SP6_BLOCK.get(),
        BlockRegister.SP7_BLOCK.get(),
        BlockRegister.SP8_BLOCK.get(),
        BlockRegister.IRON_FURNACE_BLOCK.get(),
        BlockRegister.GOLD_FURNACE_BLOCK.get(),
        BlockRegister.DIAMOND_FURNACE_BLOCK.get(),
        BlockRegister.EMERALD_FURNACE_BLOCK.get(),
        BlockRegister.NETHERITE_FURNACE_BLOCK.get(),
        BlockRegister.MELTER_BLOCK.get(),
        BlockRegister.UPGRADE_MAKER.get(),
        BlockRegister.JEWELRY_GENERATOR.get(),
        BlockRegister.CIRCUIT_FABRICATOR.get()
      );
  }

  private void addFurnaces() {
    tag(TagRegistry.AllBlockTags.FURNACES.tag)
      .replace(false)
      .add(
        BlockRegister.IRON_FURNACE_BLOCK.get(),
        BlockRegister.GOLD_FURNACE_BLOCK.get(),
        BlockRegister.DIAMOND_FURNACE_BLOCK.get(),
        BlockRegister.EMERALD_FURNACE_BLOCK.get(),
        BlockRegister.NETHERITE_FURNACE_BLOCK.get()
      );
  }

  private void addBlocks() {
    tag(TagRegistry.AllBlockTags.BLOCKS.tag)
      .replace(false)
      .add(
        BlockRegister.MACHINE_CASING.get(),
        BlockRegister.SP1_BLOCK.get(),
        BlockRegister.SP2_BLOCK.get(),
        BlockRegister.SP3_BLOCK.get(),
        BlockRegister.SP4_BLOCK.get(),
        BlockRegister.SP5_BLOCK.get(),
        BlockRegister.SP6_BLOCK.get(),
        BlockRegister.SP7_BLOCK.get(),
        BlockRegister.SP8_BLOCK.get(),
        BlockRegister.IRON_FURNACE_BLOCK.get(),
        BlockRegister.GOLD_FURNACE_BLOCK.get(),
        BlockRegister.DIAMOND_FURNACE_BLOCK.get(),
        BlockRegister.EMERALD_FURNACE_BLOCK.get(),
        BlockRegister.NETHERITE_FURNACE_BLOCK.get(),
        BlockRegister.MELTER_BLOCK.get(),
        BlockRegister.UPGRADE_MAKER.get(),
        BlockRegister.JEWELRY_GENERATOR.get(),
        BlockRegister.CIRCUIT_FABRICATOR.get()
      );
  }

  private void addMineableTags() {
    addAxeMineableTags();
    addHoeMineableTags();
    addShovelMineableTags();
    addPickaxeMineableTags();
  }

  private void addNeedsTags() {
    addIronNeedsTags();
    addDiamondNeedsTags();
    addStoneNeedsTags();
  }

  private void addIronNeedsTags() {
    tag(BlockTags.NEEDS_IRON_TOOL)
      .replace(false)
      .add(
        BlockRegister.SP1_BLOCK.get(),
        BlockRegister.SP2_BLOCK.get(),
        BlockRegister.SP3_BLOCK.get(),
        BlockRegister.SP4_BLOCK.get(),
        BlockRegister.SP5_BLOCK.get(),
        BlockRegister.SP6_BLOCK.get(),
        BlockRegister.SP7_BLOCK.get(),
        BlockRegister.SP8_BLOCK.get(),
        BlockRegister.GOLD_FURNACE_BLOCK.get(),
        BlockRegister.DIAMOND_FURNACE_BLOCK.get(),
        BlockRegister.MELTER_BLOCK.get(),
        BlockRegister.UPGRADE_MAKER.get(),
        BlockRegister.JEWELRY_GENERATOR.get()
      );
  }

  private void addDiamondNeedsTags() {
    tag(BlockTags.NEEDS_DIAMOND_TOOL)
      .replace(false)
      .add(
        BlockRegister.EMERALD_FURNACE_BLOCK.get(),
        BlockRegister.NETHERITE_FURNACE_BLOCK.get(),
        BlockRegister.CIRCUIT_FABRICATOR.get()
      );
  }

  private void addStoneNeedsTags() {
    tag(BlockTags.NEEDS_STONE_TOOL)
      .replace(false)
      .add(
        BlockRegister.MACHINE_CASING.get(),
        BlockRegister.IRON_FURNACE_BLOCK.get()
      );
  }

  private void addAxeMineableTags() {
    tag(BlockTags.MINEABLE_WITH_AXE)
      .replace(false)
      .add(BlockRegister.JEWELRY_GENERATOR.get());
  }

  private void addHoeMineableTags() {
    tag(BlockTags.MINEABLE_WITH_HOE)
      .replace(false);
  }

  private void addShovelMineableTags() {
    tag(BlockTags.MINEABLE_WITH_SHOVEL)
      .replace(false);
  }

  private void addPickaxeMineableTags() {
    tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .replace(false)
      .add(
        BlockRegister.SP1_BLOCK.get(),
        BlockRegister.SP2_BLOCK.get(),
        BlockRegister.SP3_BLOCK.get(),
        BlockRegister.SP4_BLOCK.get(),
        BlockRegister.SP5_BLOCK.get(),
        BlockRegister.SP6_BLOCK.get(),
        BlockRegister.SP7_BLOCK.get(),
        BlockRegister.SP8_BLOCK.get(),
        BlockRegister.IRON_FURNACE_BLOCK.get(),
        BlockRegister.GOLD_FURNACE_BLOCK.get(),
        BlockRegister.DIAMOND_FURNACE_BLOCK.get(),
        BlockRegister.EMERALD_FURNACE_BLOCK.get(),
        BlockRegister.NETHERITE_FURNACE_BLOCK.get(),
        BlockRegister.MELTER_BLOCK.get(),
        BlockRegister.UPGRADE_MAKER.get(),
        BlockRegister.MACHINE_CASING.get(),
        BlockRegister.JEWELRY_GENERATOR.get(),
        BlockRegister.CIRCUIT_FABRICATOR.get()
      );
  }
}
