package es.degrassi.forge.integration.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "furnaces")
public class FurnaceConfig implements ConfigData {
  // furnaces
  @ConfigEntry.Category("Iron Furnace")
  @Comment("energy capacity - Default 10000")
  public int iron_furnace_capacity = 10000;

  @ConfigEntry.Category("Iron Furnace")
  @Comment("energy transfer - Default 20")
  public int iron_furnace_transfer = 20;

  @ConfigEntry.Category("Iron Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int iron_furnace_xp = 1;

  @ConfigEntry.Category("Iron Furnace")
  @Comment("speed recipe reduction in percentage - Default 5")
  public int iron_furnace_speed = 5;

  @ConfigEntry.Category("Iron Furnace")
  @Comment("energy recipe augment in percentage - Default 0")
  public int iron_furnace_energy = 0;

  @ConfigEntry.Category("Gold Furnace")
  @Comment("energy capacity - Default 50000")
  public int gold_furnace_capacity = 50000;

  @ConfigEntry.Category("Gold Furnace")
  @Comment("energy transfer - Default 100")
  public int gold_furnace_transfer = 100;

  @ConfigEntry.Category("Gold Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int gold_furnace_xp = 1;

  @ConfigEntry.Category("Gold Furnace")
  @Comment("speed recipe reduction in percentage - Default 20")
  public int gold_furnace_speed = 20;

  @ConfigEntry.Category("Gold Furnace")
  @Comment("energy recipe augment in percentage - Default 20")
  public int gold_furnace_energy = 20;

  @ConfigEntry.Category("Diamond Furnace")
  @Comment("energy capacity - Default 100000")
  public int diamond_furnace_capacity = 100000;

  @ConfigEntry.Category("Diamond Furnace")
  @Comment("energy transfer - Default 200")
  public int diamond_furnace_transfer = 200;

  @ConfigEntry.Category("Diamond Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int diamond_furnace_xp = 1;

  @ConfigEntry.Category("Diamond Furnace")
  @Comment("speed recipe reduction in percentage - Default 40")
  public int diamond_furnace_speed = 40;

  @ConfigEntry.Category("Diamond Furnace")
  @Comment("energy recipe augment in percentage - Default 40")
  public int diamond_furnace_energy = 40;

  @ConfigEntry.Category("Emerald Furnace")
  @Comment("energy capacity - Default 500000")
  public int emerald_furnace_capacity = 500000;

  @ConfigEntry.Category("Emerald Furnace")
  @Comment("energy transfer - Default 1000")
  public int emerald_furnace_transfer = 1000;

  @ConfigEntry.Category("Emerald Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int emerald_furnace_xp = 1;

  @ConfigEntry.Category("Emerald Furnace")
  @Comment("speed recipe reduction in percentage - Default 60")
  public int emerald_furnace_speed = 60;

  @ConfigEntry.Category("Emerald Furnace")
  @Comment("energy recipe augment in percentage - Default 60")
  public int emerald_furnace_energy = 60;

  @ConfigEntry.Category("Netherite Furnace")
  @Comment("energy capacity - Default 1000000")
  public int netherite_furnace_capacity = 1000000;

  @ConfigEntry.Category("Netherite Furnace")
  @Comment("energy transfer - Default 10000")
  public int netherite_furnace_transfer = 10000;

  @ConfigEntry.Category("Netherite Furnace")
  @Comment("xp augment production on recipe in percentage - Default 1")
  public int netherite_furnace_xp = 1;

  @ConfigEntry.Category("Netherite Furnace")
  @Comment("speed recipe reduction in percentage - Default 90")
  public int netherite_furnace_speed = 90;

  @ConfigEntry.Category("Netherite Furnace")
  @Comment("energy recipe augment in percentage - Default 90")
  public int netherite_furnace_energy = 90;
}
