package es.degrassi.forge.integration.config;

import es.degrassi.forge.init.item.upgrade.UpgradeType;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "upgrades")
public class UpgradesConfig implements ConfigData {
  // upgrades
  @ConfigEntry.Category("Efficiency Upgrade")
  @Comment("efficiency augment - default 5")
  public int eff_augment = 5;

  @ConfigEntry.Category("Efficiency Upgrade")
  @Comment("efficiency type - default ADD. Available types: ADD, MULTIPLY, EXPONENTIAL")
  public UpgradeType eff_type = UpgradeType.ADD;

  @ConfigEntry.Category("Transfer Upgrade")
  @Comment("efficiency augment - default 5")
  public int trans_augment = 5;

  @ConfigEntry.Category("Transfer Upgrade")
  @Comment("transfer type - default ADD. Available types: ADD, MULTIPLY, EXPONENTIAL")
  public UpgradeType trans_type = UpgradeType.ADD;

  @ConfigEntry.Category("Generation Upgrade")
  @Comment("generation augment - default 5")
  public int gen_augment = 5;

  @ConfigEntry.Category("Generation Upgrade")
  @Comment("generation type - default ADD. Available types: ADD, MULTIPLY, EXPONENTIAL")
  public UpgradeType gen_type = UpgradeType.ADD;

  @ConfigEntry.Category("Capacity Upgrade")
  @Comment("capacity augment - default 5")
  public int cap_augment = 5;

  @ConfigEntry.Category("Capacity Upgrade")
  @Comment("capacity type - default ADD. Available types: ADD, MULTIPLY, EXPONENTIAL")
  public UpgradeType cap_type = UpgradeType.ADD;

  @ConfigEntry.Category("Speed Upgrade")
  @Comment("speed augment in percentage - default 1")
  public int speed_augment = 1;

  @ConfigEntry.Category("Speed Upgrade")
  @Comment("speed energy augment in percentage - default 5")
  public int speed_energy_augment = 5;


  @ConfigEntry.Category("Energy Upgrade")
  @Comment("energy reduction in percentage - default 1")
  public int energy_augment = 1;
}
