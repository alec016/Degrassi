package es.degrassi.forge.integration.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "upgrade_maker")
public class UpgradeMakerConfig implements ConfigData {
  // upgrade maker
  @ConfigEntry.Category("Upgrade Maker")
  @Comment("energy capacity - Default 1000000")
  public int upgrade_maker_capacity = 1000000;

  @ConfigEntry.Category("Upgrade Maker")
  @Comment("transfer rate - Default 50000")
  public int upgrade_maker_transfer = 50000;
}
