package es.degrassi.forge.integration.config;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.item.upgrade.UpgradeType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Degrassi.MODID)
public class DegrassiConfig implements ConfigData {

  // panels
  @Category("Solar Panel Tier I")
  @Comment("base generation - default 1")
  public int sp1_generation = 1;

  @Category("Solar Panel Tier I")
  @Comment("percentage of energy loose on break - default 5")
  public int sp1_loose = 5;

  @Category("Solar Panel Tier I")
  @Comment("Energy Capacity - default 10000")
  public int sp1_capacity = 10000;

  @Category("Solar Panel Tier I")
  @Comment("Max FE/t transfer - default 5")
  public int sp1_transfer = 5;

  @Category("Solar Panel Tier II")
  @Comment("base generation - default 8")
  public int sp2_generation = 8;

  @Category("Solar Panel Tier II")
  @Comment("percentage of energy loose on break - default 5")
  public int sp2_loose = 5;

  @Category("Solar Panel Tier II")
  @Comment("Energy Capacity - default 60000")
  public int sp2_capacity = 60000;

  @Category("Solar Panel Tier II")
  @Comment("Max FE/t transfer - default 16")
  public int sp2_transfer = 16;

  @Category("Solar Panel Tier III")
  @Comment("base generation - default 16")
  public int sp3_generation = 16;

  @Category("Solar Panel Tier III")
  @Comment("percentage of energy loose on break - default 5")
  public int sp3_loose = 5;

  @Category("Solar Panel Tier III")
  @Comment("Energy Capacity - default 100000")
  public int sp3_capacity = 100000;

  @Category("Solar Panel Tier III")
  @Comment("Max FE/t transfer - default 64")
  public int sp3_transfer = 64;

  @Category("Solar Panel Tier IV")
  @Comment("base generation - default 48")
  public int sp4_generation = 48;

  @Category("Solar Panel Tier IV")
  @Comment("percentage of energy loose on break - default 5")
  public int sp4_loose = 5;

  @Category("Solar Panel Tier IV")
  @Comment("Energy Capacity - default 500000")
  public int sp4_capacity = 500000;

  @Category("Solar Panel Tier IV")
  @Comment("Max FE/t transfer - default 128")
  public int sp4_transfer = 128;

  @Category("Solar Panel Tier V")
  @Comment("base generation - default 128")
  public int sp5_generation = 128;

  @Category("Solar Panel Tier V")
  @Comment("percentage of energy loose on break - default 5")
  public int sp5_loose = 5;

  @Category("Solar Panel Tier V")
  @Comment("Energy Capacity - default 1000000")
  public int sp5_capacity = 1000000;

  @Category("Solar Panel Tier V")
  @Comment("Max FE/t transfer - default 512")
  public int sp5_transfer = 512;

  @Category("Solar Panel Tier VI")
  @Comment("base generation - default 256")
  public int sp6_generation = 256;

  @Category("Solar Panel Tier VI")
  @Comment("percentage of energy loose on break - default 5")
  public int sp6_loose = 5;

  @Category("Solar Panel Tier VI")
  @Comment("Energy Capacity - default 2000000")
  public int sp6_capacity = 2000000;

  @Category("Solar Panel Tier VI")
  @Comment("Max FE/t transfer - default 1024")
  public int sp6_transfer = 1024;

  @Category("Solar Panel Tier VII")
  @Comment("base generation - default 1024")
  public int sp7_generation = 1024;

  @Category("Solar Panel Tier VII")
  @Comment("percentage of energy loose on break - default 5")
  public int sp7_loose = 5;

  @Category("Solar Panel Tier VII")
  @Comment("Energy Capacity - default 5000000")
  public int sp7_capacity = 5000000;

  @Category("Solar Panel Tier VII")
  @Comment("Max FE/t transfer - default 2048")
  public int sp7_transfer = 2048;

  @Category("Solar Panel Tier VIII")
  @Comment("base generation - default 32048")
  public int sp8_generation = 32048;

  @Category("Solar Panel Tier VIII")
  @Comment("percentage of energy loose on break - default 5")
  public int sp8_loose = 5;

  @Category("Solar Panel Tier VIII")
  @Comment("Energy Capacity - default 10000000")
  public int sp8_capacity = 10000000;

  @Category("Solar Panel Tier VIII")
  @Comment("Max FE/t transfer - default 48072")
  public int sp8_transfer = 48072;

  // furnaces
  @Category("Iron Furnace")
  @Comment("energy capacity - Default 10000")
  public int iron_furnace_capacity = 10000;

  @Category("Iron Furnace")
  @Comment("energy transfer - Default 20")
  public int iron_furnace_transfer = 20;

  @Category("Iron Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int iron_furnace_xp = 1;

  @Category("Iron Furnace")
  @Comment("speed recipe reduction in percentage - Default 5")
  public int iron_furnace_speed = 5;

  @Category("Iron Furnace")
  @Comment("energy recipe augment in percentage - Default 0")
  public int iron_furnace_energy = 0;

  @Category("Gold Furnace")
  @Comment("energy capacity - Default 50000")
  public int gold_furnace_capacity = 50000;

  @Category("Gold Furnace")
  @Comment("energy transfer - Default 100")
  public int gold_furnace_transfer = 100;

  @Category("Gold Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int gold_furnace_xp = 1;

  @Category("Gold Furnace")
  @Comment("speed recipe reduction in percentage - Default 20")
  public int gold_furnace_speed = 20;

  @Category("Gold Furnace")
  @Comment("energy recipe augment in percentage - Default 20")
  public int gold_furnace_energy = 20;

  @Category("Diamond Furnace")
  @Comment("energy capacity - Default 100000")
  public int diamond_furnace_capacity = 100000;

  @Category("Diamond Furnace")
  @Comment("energy transfer - Default 200")
  public int diamond_furnace_transfer = 200;

  @Category("Diamond Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int diamond_furnace_xp = 1;

  @Category("Diamond Furnace")
  @Comment("speed recipe reduction in percentage - Default 40")
  public int diamond_furnace_speed = 40;

  @Category("Diamond Furnace")
  @Comment("energy recipe augment in percentage - Default 40")
  public int diamond_furnace_energy = 40;

  @Category("Emerald Furnace")
  @Comment("energy capacity - Default 500000")
  public int emerald_furnace_capacity = 500000;

  @Category("Emerald Furnace")
  @Comment("energy transfer - Default 1000")
  public int emerald_furnace_transfer = 1000;

  @Category("Emerald Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int emerald_furnace_xp = 1;

  @Category("Emerald Furnace")
  @Comment("speed recipe reduction in percentage - Default 60")
  public int emerald_furnace_speed = 60;

  @Category("Emerald Furnace")
  @Comment("energy recipe augment in percentage - Default 60")
  public int emerald_furnace_energy = 60;

  @Category("Netherite Furnace")
  @Comment("energy capacity - Default 1000000")
  public int netherite_furnace_capacity = 1000000;

  @Category("Netherite Furnace")
  @Comment("energy transfer - Default 10000")
  public int netherite_furnace_transfer = 10000;

  @Category("Netherite Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int netherite_furnace_xp = 1;

  @Category("Netherite Furnace")
  @Comment("speed recipe reduction in percentage - Default 90")
  public int netherite_furnace_speed = 90;

  @Category("Netherite Furnace")
  @Comment("energy recipe augment in percentage - Default 90")
  public int netherite_furnace_energy = 90;

  // melter
  @Category("Melter")
  @Comment("energy capacity - Default 100000")
  public int melter_capacity = 100000;

  @Category("Melter")
  @Comment("transfer rate - Default 10000")
  public int melter_transfer = 10000;

  // upgrades
  @Category("Efficiency Upgrade")
  @Comment("efficiency augment - default 5")
  public int eff_augment = 5;

  @Category("Efficiency Upgrade")
  @Comment("efficiency type - default ADD. Available types: ADD, MULTIPLY, EXPONENTIAL")
  public UpgradeType eff_type = UpgradeType.ADD;

  @Category("Transfer Upgrade")
  @Comment("efficiency augment - default 5")
  public int trans_augment = 5;

  @Category("Transfer Upgrade")
  @Comment("transfer type - default ADD. Available types: ADD, MULTIPLY, EXPONENTIAL")
  public UpgradeType trans_type = UpgradeType.ADD;

  @Category("Generation Upgrade")
  @Comment("generation augment - default 5")
  public int gen_augment = 5;

  @Category("Generation Upgrade")
  @Comment("generation type - default ADD. Available types: ADD, MULTIPLY, EXPONENTIAL")
  public UpgradeType gen_type = UpgradeType.ADD;

  @Category("Capacity Upgrade")
  @Comment("capacity augment - default 5")
  public int cap_augment = 5;

  @Category("Capacity Upgrade")
  @Comment("capacity type - default ADD. Available types: ADD, MULTIPLY, EXPONENTIAL")
  public UpgradeType cap_type = UpgradeType.ADD;

  @Category("Speed Upgrade")
  @Comment("speed augment in percentage - default 1")
  public int speed_augment = 1;

  @Category("Speed Upgrade")
  @Comment("speed energy augment in percentage - default 5")
  public int speed_energy_augment = 5;


  @Category("Energy Upgrade")
  @Comment("energy reduction in percentage - default 1")
  public int energy_augment = 1;

  public static DegrassiConfig get() {
    return AutoConfig.getConfigHolder(DegrassiConfig.class).getConfig();
  }
}
