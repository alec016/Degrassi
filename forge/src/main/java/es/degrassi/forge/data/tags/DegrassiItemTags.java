package es.degrassi.forge.data.tags;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.data.DegrassiTagProvider;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public final class DegrassiItemTags extends DegrassiTagProvider<Item> {
  public DegrassiItemTags(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, Registry.ITEM, existingFileHelper);
  }

  @Override
  protected void addTags() {
    addCustomTags();
  }

  private void addCustomTags() {
    addItems();
    addMachines();
    addUpgrades();
    addFurnaces();
    addPanels();
  }

  private void addItems(){
    tag(ItemTags.create(new ResourceLocation(Degrassi.MODID, "items")))
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
        BlockRegister.JEWELRY_GENERATOR.get().asItem()
      );
  }

  private void addMachines() {
    tag(ItemTags.create(new DegrassiLocation("machines")))
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
        BlockRegister.JEWELRY_GENERATOR.get().asItem()
      );
  }

  private void addUpgrades() {
    tag(ItemTags.create(new DegrassiLocation("upgrades")))
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
    tag(ItemTags.create(new DegrassiLocation("furnaces")))
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
    tag(ItemTags.create(new DegrassiLocation("panels/photovoltaic_cells")))
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
    tag(ItemTags.create(new DegrassiLocation("panels/solar_panels")))
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
