package es.degrassi.forge.data.tags;

import es.degrassi.forge.data.DegrassiTagProvider;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.init.registration.TagRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public final class DegrassiItemTags extends DegrassiTagProvider<Item> {
  public DegrassiItemTags(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, Registry.ITEM, existingFileHelper);
  }

  @Override
  protected void addTags() {
    addForgeTags();
    addCustomTags();
  }

  private void addForgeTags() {
    tag(TagRegistry.AllItemTags.PISTONS.tag)
      .replace(false)
      .add(
        Blocks.PISTON.asItem(),
        Blocks.STICKY_PISTON.asItem()
      );
  }

  private void addCustomTags() {
    addItems();
    addMachines();
    addUpgrades();
    addFurnaces();
    addGenerators();
    addPanels();
  }

  private void addItems(){
    tag(TagRegistry.AllItemTags.ITEMS.tag)
      .replace(false)
      .add(
        ItemRegister.GOLD_COIN.get(),
        ItemRegister.EFFICIENCY_UPGRADE.get(),
        ItemRegister.TRANSFER_UPGRADE.get(),
        ItemRegister.GENERATION_UPGRADE.get(),
        ItemRegister.CAPACITY_UPGRADE.get(),
        ItemRegister.RED_MATTER.get(),
        ItemRegister.BLACK_PEARL.get(),
        ItemRegister.MODIFIER_BASE.get(),
        ItemRegister.UPGRADE_BASE.get(),
        ItemRegister.SPEED_UPGRADE.get(),
        ItemRegister.ENERGY_UPGRADE.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_I.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_II.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_III.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_VI.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_V.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_VI.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_VII.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_VIII.get(),
        ItemRegister.CIRCUIT.get(),
        ItemRegister.MOLTEN_RED_MATTER_BUCKET.get(),

        BlockRegister.MACHINE_CASING.get().asItem(),
        BlockRegister.SP1_BLOCK.get().asItem(),
        BlockRegister.SP2_BLOCK.get().asItem(),
        BlockRegister.SP3_BLOCK.get().asItem(),
        BlockRegister.SP4_BLOCK.get().asItem(),
        BlockRegister.SP5_BLOCK.get().asItem(),
        BlockRegister.SP6_BLOCK.get().asItem(),
        BlockRegister.SP7_BLOCK.get().asItem(),
        BlockRegister.SP8_BLOCK.get().asItem(),
        BlockRegister.IRON_FURNACE_BLOCK.get().asItem(),
        BlockRegister.GOLD_FURNACE_BLOCK.get().asItem(),
        BlockRegister.DIAMOND_FURNACE_BLOCK.get().asItem(),
        BlockRegister.EMERALD_FURNACE_BLOCK.get().asItem(),
        BlockRegister.NETHERITE_FURNACE_BLOCK.get().asItem(),
        BlockRegister.MELTER_BLOCK.get().asItem(),
        BlockRegister.UPGRADE_MAKER.get().asItem(),
        BlockRegister.JEWELRY_GENERATOR.get().asItem(),
        BlockRegister.COMBUSTION_GENERATOR.get().asItem(),
        BlockRegister.CIRCUIT_FABRICATOR.get().asItem()
      );
  }

  private void addGenerators() {
    tag(TagRegistry.AllItemTags.GENERATORS.tag)
      .replace(false)
      .add(
        BlockRegister.JEWELRY_GENERATOR.get().asItem(),
        BlockRegister.CIRCUIT_FABRICATOR.get().asItem()
      );
  }

  private void addMachines() {
    tag(TagRegistry.AllItemTags.MACHINES.tag)
      .replace(false)
      .add(
        BlockRegister.SP1_BLOCK.get().asItem(),
        BlockRegister.SP2_BLOCK.get().asItem(),
        BlockRegister.SP3_BLOCK.get().asItem(),
        BlockRegister.SP4_BLOCK.get().asItem(),
        BlockRegister.SP5_BLOCK.get().asItem(),
        BlockRegister.SP6_BLOCK.get().asItem(),
        BlockRegister.SP7_BLOCK.get().asItem(),
        BlockRegister.SP8_BLOCK.get().asItem(),
        BlockRegister.IRON_FURNACE_BLOCK.get().asItem(),
        BlockRegister.GOLD_FURNACE_BLOCK.get().asItem(),
        BlockRegister.DIAMOND_FURNACE_BLOCK.get().asItem(),
        BlockRegister.EMERALD_FURNACE_BLOCK.get().asItem(),
        BlockRegister.NETHERITE_FURNACE_BLOCK.get().asItem(),
        BlockRegister.MELTER_BLOCK.get().asItem(),
        BlockRegister.UPGRADE_MAKER.get().asItem(),
        BlockRegister.JEWELRY_GENERATOR.get().asItem(),
        BlockRegister.CIRCUIT_FABRICATOR.get().asItem()
      );
  }

  private void addUpgrades() {
    tag(TagRegistry.AllItemTags.UPGRADES.tag)
      .replace(false)
      .add(
        ItemRegister.UPGRADE_BASE.get(),
        ItemRegister.EFFICIENCY_UPGRADE.get(),
        ItemRegister.TRANSFER_UPGRADE.get(),
        ItemRegister.GENERATION_UPGRADE.get(),
        ItemRegister.CAPACITY_UPGRADE.get(),
        ItemRegister.SPEED_UPGRADE.get(),
        ItemRegister.ENERGY_UPGRADE.get()
      );
  }

  private void addFurnaces() {
    tag(TagRegistry.AllItemTags.FURNACES.tag)
      .replace(false)
      .add(
        BlockRegister.IRON_FURNACE_BLOCK.get().asItem(),
        BlockRegister.GOLD_FURNACE_BLOCK.get().asItem(),
        BlockRegister.DIAMOND_FURNACE_BLOCK.get().asItem(),
        BlockRegister.EMERALD_FURNACE_BLOCK.get().asItem(),
        BlockRegister.NETHERITE_FURNACE_BLOCK.get().asItem()
      );
  }

  private void addPanels() {
    addPhotovoltaicCells();
    addSolarPanels();
  }

  private void addPhotovoltaicCells() {
    tag(TagRegistry.AllItemTags.PHOTOVOLTAIC_CELLS.tag)
      .replace(false)
      .add(
        ItemRegister.PHOTOVOLTAIC_CELL_I.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_II.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_III.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_VI.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_V.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_VI.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_VII.get(),
        ItemRegister.PHOTOVOLTAIC_CELL_VIII.get()
      );
  }

  private void addSolarPanels() {
    tag(TagRegistry.AllItemTags.SOLAR_PANELS.tag)
      .replace(false)
      .add(
        BlockRegister.SP1_BLOCK.get().asItem(),
        BlockRegister.SP2_BLOCK.get().asItem(),
        BlockRegister.SP3_BLOCK.get().asItem(),
        BlockRegister.SP4_BLOCK.get().asItem(),
        BlockRegister.SP5_BLOCK.get().asItem(),
        BlockRegister.SP6_BLOCK.get().asItem(),
        BlockRegister.SP7_BLOCK.get().asItem(),
        BlockRegister.SP8_BLOCK.get().asItem()
      );
  }
}
