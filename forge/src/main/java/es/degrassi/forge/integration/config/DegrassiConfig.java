package es.degrassi.forge.integration.config;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.integration.config.generators.GeneratorsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = Degrassi.MODID)
public class DegrassiConfig extends PartitioningSerializer.GlobalData {

  @Category("solar_panels")
  public SolarPanelConfig solarPanelConfig = new SolarPanelConfig();

  @Category("furnaces")
  public FurnaceConfig furnaceConfig = new FurnaceConfig();

  @Category("melter")
  public MelterConfig melterConfig = new MelterConfig();

  @Category("upgrade_maker")
  public UpgradeMakerConfig upgradeMakerConfig = new UpgradeMakerConfig();

  @Category("upgrades")
  public UpgradesConfig upgradeConfig = new UpgradesConfig();

  @Category("generators")
  public GeneratorsConfig generatorsConfig = new GeneratorsConfig();

  public static DegrassiConfig get() {
    return AutoConfig.getConfigHolder(DegrassiConfig.class).getConfig();
  }
}
