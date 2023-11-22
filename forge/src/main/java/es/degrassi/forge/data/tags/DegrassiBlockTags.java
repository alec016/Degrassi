package es.degrassi.forge.data.tags;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.data.DegrassiTagProvider;
import es.degrassi.forge.init.registration.BlockRegister;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
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
    tag(BlockTags.create(new ResourceLocation("forge", "pistons")))
      .replace(false)
      .add(Blocks.PISTON)
      .add(Blocks.STICKY_PISTON);
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
    tag(BlockTags.create(new DegrassiLocation("panels/solar_panels")))
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
    tag(BlockTags.create(new DegrassiLocation("machines")))
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
        BlockRegister.JEWELRY_GENERATOR.get()
      );
  }

  private void addFurnaces() {
    tag(BlockTags.create(new DegrassiLocation("furnaces")))
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
    tag(BlockTags.create(new DegrassiLocation("blocks")))
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
        BlockRegister.JEWELRY_GENERATOR.get()
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
      .add(BlockRegister.SP1_BLOCK.get())
      .add(BlockRegister.SP2_BLOCK.get())
      .add(BlockRegister.SP3_BLOCK.get())
      .add(BlockRegister.SP4_BLOCK.get())
      .add(BlockRegister.SP5_BLOCK.get())
      .add(BlockRegister.SP6_BLOCK.get())
      .add(BlockRegister.SP7_BLOCK.get())
      .add(BlockRegister.SP8_BLOCK.get())
      .add(BlockRegister.GOLD_FURNACE_BLOCK.get())
      .add(BlockRegister.DIAMOND_FURNACE_BLOCK.get())
      .add(BlockRegister.MELTER_BLOCK.get())
      .add(BlockRegister.UPGRADE_MAKER.get())
      .add(BlockRegister.JEWELRY_GENERATOR.get());
  }

  private void addDiamondNeedsTags() {
    tag(BlockTags.NEEDS_DIAMOND_TOOL)
      .replace(false)
      .add(BlockRegister.EMERALD_FURNACE_BLOCK.get())
      .add(BlockRegister.NETHERITE_FURNACE_BLOCK.get());
  }

  private void addStoneNeedsTags() {
    tag(BlockTags.NEEDS_STONE_TOOL)
      .replace(false)
      .add(BlockRegister.MACHINE_CASING.get())
      .add(BlockRegister.IRON_FURNACE_BLOCK.get());
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
      .add(BlockRegister.SP1_BLOCK.get())
      .add(BlockRegister.SP2_BLOCK.get())
      .add(BlockRegister.SP3_BLOCK.get())
      .add(BlockRegister.SP4_BLOCK.get())
      .add(BlockRegister.SP5_BLOCK.get())
      .add(BlockRegister.SP6_BLOCK.get())
      .add(BlockRegister.SP7_BLOCK.get())
      .add(BlockRegister.SP8_BLOCK.get())
      .add(BlockRegister.IRON_FURNACE_BLOCK.get())
      .add(BlockRegister.GOLD_FURNACE_BLOCK.get())
      .add(BlockRegister.DIAMOND_FURNACE_BLOCK.get())
      .add(BlockRegister.EMERALD_FURNACE_BLOCK.get())
      .add(BlockRegister.NETHERITE_FURNACE_BLOCK.get())
      .add(BlockRegister.MELTER_BLOCK.get())
      .add(BlockRegister.UPGRADE_MAKER.get())
      .add(BlockRegister.MACHINE_CASING.get())
      .add(BlockRegister.JEWELRY_GENERATOR.get());
  }
}
