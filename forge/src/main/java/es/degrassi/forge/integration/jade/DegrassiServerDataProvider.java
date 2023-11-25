package es.degrassi.forge.integration.jade;

import es.degrassi.forge.integration.jade.server.data.providers.FurnaceDataProvider;
import es.degrassi.forge.integration.jade.server.data.providers.MelterDataProvider;
import es.degrassi.forge.integration.jade.server.data.providers.UpgradeMakerDataProvider;

public class DegrassiServerDataProvider {
  public static final FurnaceDataProvider FURNACE = FurnaceDataProvider.INSTANCE;
  public static final MelterDataProvider MELTER = MelterDataProvider.INSTANCE;
  public static final UpgradeMakerDataProvider UPGRADE_MAKER = UpgradeMakerDataProvider.INSTANCE;
}
