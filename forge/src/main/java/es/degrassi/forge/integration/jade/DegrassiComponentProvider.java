package es.degrassi.forge.integration.jade;

import es.degrassi.forge.integration.jade.client.component.providers.FurnaceComponentProvider;
import es.degrassi.forge.integration.jade.client.component.providers.MelterComponentProvider;
import es.degrassi.forge.integration.jade.client.component.providers.UpgradeMakerComponentProvider;
import es.degrassi.forge.integration.jade.client.component.providers.GeneratorComponentProvider;

public class DegrassiComponentProvider {
  public static final FurnaceComponentProvider FURNACE = FurnaceComponentProvider.INSTANCE;
  public static final MelterComponentProvider MELTER = MelterComponentProvider.INSTANCE;
  public static final UpgradeMakerComponentProvider UPGRADE_MAKER = UpgradeMakerComponentProvider.INSTANCE;
  public static final GeneratorComponentProvider GENERATOR = GeneratorComponentProvider.INSTANCE;
}
