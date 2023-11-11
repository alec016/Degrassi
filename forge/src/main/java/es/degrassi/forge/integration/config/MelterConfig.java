package es.degrassi.forge.integration.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "melter")
public class MelterConfig implements ConfigData {
  // melter
  @ConfigEntry.Category("Melter")
  @Comment("energy capacity - Default 100000")
  public int melter_capacity = 100000;

  @ConfigEntry.Category("Melter")
  @Comment("transfer rate - Default 10000")
  public int melter_transfer = 10000;
}
