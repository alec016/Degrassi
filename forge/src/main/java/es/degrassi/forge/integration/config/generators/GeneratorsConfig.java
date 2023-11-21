package es.degrassi.forge.integration.config.generators;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "generators")
public class GeneratorsConfig implements ConfigData {
  @ConfigEntry.Category("Jewelry Generator")
  @Comment("energy capacity - Default 10000")
  public int jewelry_capacity = 10000;

  @ConfigEntry.Category("Jewelry Generator")
  @Comment("energy transfer - Default 20")
  public int jewelry_transfer = 20;
}
