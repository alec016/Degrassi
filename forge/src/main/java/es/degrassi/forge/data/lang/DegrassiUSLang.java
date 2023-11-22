package es.degrassi.forge.data.lang;

import es.degrassi.forge.data.DegrassiLangProvider;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import net.minecraft.data.DataGenerator;

public class DegrassiUSLang extends DegrassiLangProvider {
  public DegrassiUSLang(DataGenerator gen) {
    super(gen, "en_us");
  }

  protected void addItems() {
    add(ItemRegister.GOLD_COIN.get(), "Gold Coin");

    add(ItemRegister.EFFICIENCY_UPGRADE.get(), "Efficiency Upgrade");
    add(ItemRegister.TRANSFER_UPGRADE.get(), "Transfer Upgrade");
    add(ItemRegister.CAPACITY_UPGRADE.get(), "Capacity Upgrade");
    add(ItemRegister.GENERATION_UPGRADE.get(), "Generation Upgrade");
    add(ItemRegister.SPEED_UPGRADE.get(), "Speed Upgrade");
    add(ItemRegister.ENERGY_UPGRADE.get(), "Energy Upgrade");

    add(ItemRegister.UPGRADE_BASE.get(), "Upgrade Base");
    add(ItemRegister.MODIFIER_BASE.get(), "Modifier Base");
    add(ItemRegister.BLACK_PEARL.get(), "Black Pearl");
    add(ItemRegister.RED_MATTER.get(), "Red Matter");
    add(ItemRegister.CIRCUIT.get(), "Circuit");

    add(ItemRegister.PHOTOVOLTAIC_CELL_I.get(), "Photovoltaic Cell I");
    add(ItemRegister.PHOTOVOLTAIC_CELL_II.get(), "Photovoltaic Cell II");
    add(ItemRegister.PHOTOVOLTAIC_CELL_III.get(), "Photovoltaic Cell III");
    add(ItemRegister.PHOTOVOLTAIC_CELL_IV.get(), "Photovoltaic Cell IV");
    add(ItemRegister.PHOTOVOLTAIC_CELL_V.get(), "Photovoltaic Cell V");
    add(ItemRegister.PHOTOVOLTAIC_CELL_VI.get(), "Photovoltaic Cell VI");
    add(ItemRegister.PHOTOVOLTAIC_CELL_VII.get(), "Photovoltaic Cell VII");
    add(ItemRegister.PHOTOVOLTAIC_CELL_VIII.get(), "Photovoltaic Cell VIII");
  }

  protected void addBlocks() {
    add(BlockRegister.MACHINE_CASING.get(), "Machine Casing");

    add(BlockRegister.SP1_BLOCK.get(), "Solar Panel Tier I");
    add(BlockRegister.SP2_BLOCK.get(), "Solar Panel Tier II");
    add(BlockRegister.SP3_BLOCK.get(), "Solar Panel Tier III");
    add(BlockRegister.SP4_BLOCK.get(), "Solar Panel Tier VI");
    add(BlockRegister.SP5_BLOCK.get(), "Solar Panel Tier V");
    add(BlockRegister.SP6_BLOCK.get(), "Solar Panel Tier VI");
    add(BlockRegister.SP7_BLOCK.get(), "Solar Panel Tier VII");
    add(BlockRegister.SP8_BLOCK.get(), "Solar Panel Tier VIII");

    add(BlockRegister.MELTER_BLOCK.get(), "Melter");
    add(BlockRegister.IRON_FURNACE_BLOCK.get(), "Iron Furnace");
    add(BlockRegister.GOLD_FURNACE_BLOCK.get(), "Gold Furnace");
    add(BlockRegister.DIAMOND_FURNACE_BLOCK.get(), "Diamond Furnace");
    add(BlockRegister.EMERALD_FURNACE_BLOCK.get(), "Emerald Furnace");
    add(BlockRegister.NETHERITE_FURNACE_BLOCK.get(), "Netherite Furnace");
    add(BlockRegister.UPGRADE_MAKER.get(), "Upgrade Maker");
    add(BlockRegister.JEWELRY_GENERATOR.get(), "Jewelry Generator");
  }

  protected void addEntities() {}

  protected void addItemGroups() {
    addItemGroup("common", "Degrassi");
    addItemGroup("machines", "Degrassi Machines");
  }

  @Override
  protected void addGuiElements() {
    addGuiElement("energy.tooltip", "Energy: %s%s/%s%s");
    addGuiElement("energy.stored","Stored: %s%s/%s%s");
    addGuiElement("energy.generation","Generating: %s%s/t");
    addGuiElement("energy.input","Requires");
    addGuiElement("energy.output","Produces");
    addGuiElement("energy.jei", "%s%s%s");
    addGuiElement("energy.jei.total", "%s%s");
    addGuiElement("energy.jei.perTick", "%s%s%s");
    addGuiElement("efficiency", "Efficiency: %s");
    addGuiElement("fluid.tooltip", "%smB/%smB");
    addGuiElement("fluid.tooltip.amount", "%smB");
    addGuiElement("fluid.empty", "Empty: %smB/%smB");
    addGuiElement("transfer", "Transfer rate: %s%s/t");
    addGuiElement("progress", "Show Recipes");
  }

  @Override
  protected void addUpgradeTooltips() {
    addUpgradeTooltip("capacity.tooltips", "Augment capacity by x%s");
    addUpgradeTooltip("efficiency.tooltips", "Augment efficiency by x%s");
    addUpgradeTooltip("generation.tooltips", "Augment generation by x%s");
    addUpgradeTooltip("transfer.tooltips", "Augment transfer rate by x%s");
    addUpgradeTooltip("speed.tooltip", "Augment machine speed by x%s");
    addUpgradeTooltip("speed.energy.tooltip", "Augment machine energy by x%s");
    addUpgradeTooltip("energy.tooltip", "Reduces machine energy requirement by x%s");
    addUpgradeTooltip("type", "Current type: %s");

    addUpgradeShiftTooltip("tooltip", "%s %s %s");
    addUpgradeShiftTooltip("press.tooltip", "Press");
    addUpgradeShiftTooltip("more.tooltip", "for more info");
  }

  @Override
  protected void addJeiGuiElements() {
    addJeiGuiElement("sp", "This Solar Panel %s%s");
    addJeiGuiElement("energy.generation", "generates up to %s%s/t");
    addJeiGuiElement("sp.solar_position", " based in solar position.");

    addJeiGuiElement("upgrades.transfer", "Modify the transfer rate of the machine by x%s");
    addJeiGuiElement("upgrades.generation", "Modify the generation rate of the machine by x%s");
    addJeiGuiElement("upgrades.efficiency", "Modify the efficiency rate of the machine by x%s");
    addJeiGuiElement("upgrades.capacity", "Modify the energy capacity of the machine by x%s");
  }

  @Override
  protected void addJeiRecipes() {
    addJeiRecipe("time", "Duration: %s ticks");
    addJeiRecipe("instant", "Instant craft");
    addJeiRecipe("id", "Recipe ID");
  }

  @Override
  protected void addMachineTooltips() {
    addMachineTooltip("sp", "Generates %s%s/t");
  }
}
