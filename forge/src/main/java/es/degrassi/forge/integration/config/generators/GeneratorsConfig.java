package es.degrassi.forge.integration.config.generators;

import es.degrassi.forge.integration.config.DefaultConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "generators")
public class GeneratorsConfig extends DefaultConfig {
  @ConfigEntry.Category("Jewelry Generator")
  @Comment("energy capacity - Default 1000000")
  public int jewelry_capacity = 1000000;

  @ConfigEntry.Category("Jewelry Generator")
  @Comment("energy transfer - Default 20000")
  public int jewelry_transfer = 20000;

  @ConfigEntry.Category("Combustion Generator")
  @Comment("energy capacity - Default 1000000")
  public int combustion_capacity = 1000000;

  @ConfigEntry.Category("Combustion Generator")
  @Comment("energy transfer - Default 20000")
  public int combustion_transfer = 20000;
}
