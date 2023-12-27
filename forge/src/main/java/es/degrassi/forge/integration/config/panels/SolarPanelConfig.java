package es.degrassi.forge.integration.config.panels;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "solar_panels")
public class SolarPanelConfig implements ConfigData {
  // panels
  @ConfigEntry.Category("Solar Panel Tier I")
  @Comment("base generation - default 1")
  public int sp1_generation = 1;

  @ConfigEntry.Category("Solar Panel Tier I")
  @Comment("percentage of energy loose on break - default 5")
  public int sp1_loose = 5;

  @ConfigEntry.Category("Solar Panel Tier I")
  @Comment("Energy Capacity - default 10000")
  public int sp1_capacity = 10000;

  @ConfigEntry.Category("Solar Panel Tier I")
  @Comment("Max FE/t transfer - default 5")
  public int sp1_transfer = 5;

  @ConfigEntry.Category("Solar Panel Tier II")
  @Comment("base generation - default 8")
  public int sp2_generation = 8;

  @ConfigEntry.Category("Solar Panel Tier II")
  @Comment("percentage of energy loose on break - default 5")
  public int sp2_loose = 5;

  @ConfigEntry.Category("Solar Panel Tier II")
  @Comment("Energy Capacity - default 60000")
  public int sp2_capacity = 60000;

  @ConfigEntry.Category("Solar Panel Tier II")
  @Comment("Max FE/t transfer - default 16")
  public int sp2_transfer = 16;

  @ConfigEntry.Category("Solar Panel Tier III")
  @Comment("base generation - default 16")
  public int sp3_generation = 16;

  @ConfigEntry.Category("Solar Panel Tier III")
  @Comment("percentage of energy loose on break - default 5")
  public int sp3_loose = 5;

  @ConfigEntry.Category("Solar Panel Tier III")
  @Comment("Energy Capacity - default 100000")
  public int sp3_capacity = 100000;

  @ConfigEntry.Category("Solar Panel Tier III")
  @Comment("Max FE/t transfer - default 64")
  public int sp3_transfer = 64;

  @ConfigEntry.Category("Solar Panel Tier IV")
  @Comment("base generation - default 48")
  public int sp4_generation = 48;

  @ConfigEntry.Category("Solar Panel Tier IV")
  @Comment("percentage of energy loose on break - default 5")
  public int sp4_loose = 5;

  @ConfigEntry.Category("Solar Panel Tier IV")
  @Comment("Energy Capacity - default 500000")
  public int sp4_capacity = 500000;

  @ConfigEntry.Category("Solar Panel Tier IV")
  @Comment("Max FE/t transfer - default 128")
  public int sp4_transfer = 128;

  @ConfigEntry.Category("Solar Panel Tier V")
  @Comment("base generation - default 128")
  public int sp5_generation = 128;

  @ConfigEntry.Category("Solar Panel Tier V")
  @Comment("percentage of energy loose on break - default 5")
  public int sp5_loose = 5;

  @ConfigEntry.Category("Solar Panel Tier V")
  @Comment("Energy Capacity - default 1000000")
  public int sp5_capacity = 1000000;

  @ConfigEntry.Category("Solar Panel Tier V")
  @Comment("Max FE/t transfer - default 512")
  public int sp5_transfer = 512;

  @ConfigEntry.Category("Solar Panel Tier VI")
  @Comment("base generation - default 256")
  public int sp6_generation = 256;

  @ConfigEntry.Category("Solar Panel Tier VI")
  @Comment("percentage of energy loose on break - default 5")
  public int sp6_loose = 5;

  @ConfigEntry.Category("Solar Panel Tier VI")
  @Comment("Energy Capacity - default 2000000")
  public int sp6_capacity = 2000000;

  @ConfigEntry.Category("Solar Panel Tier VI")
  @Comment("Max FE/t transfer - default 1024")
  public int sp6_transfer = 1024;

  @ConfigEntry.Category("Solar Panel Tier VII")
  @Comment("base generation - default 1024")
  public int sp7_generation = 1024;

  @ConfigEntry.Category("Solar Panel Tier VII")
  @Comment("percentage of energy loose on break - default 5")
  public int sp7_loose = 5;

  @ConfigEntry.Category("Solar Panel Tier VII")
  @Comment("Energy Capacity - default 5000000")
  public int sp7_capacity = 5000000;

  @ConfigEntry.Category("Solar Panel Tier VII")
  @Comment("Max FE/t transfer - default 2048")
  public int sp7_transfer = 2048;

  @ConfigEntry.Category("Solar Panel Tier VIII")
  @Comment("base generation - default 32048")
  public int sp8_generation = 32048;

  @ConfigEntry.Category("Solar Panel Tier VIII")
  @Comment("percentage of energy loose on break - default 5")
  public int sp8_loose = 5;

  @ConfigEntry.Category("Solar Panel Tier VIII")
  @Comment("Energy Capacity - default 10000000")
  public int sp8_capacity = 10000000;

  @ConfigEntry.Category("Solar Panel Tier VIII")
  @Comment("Max FE/t transfer - default 48072")
  public int sp8_transfer = 48072;
}
