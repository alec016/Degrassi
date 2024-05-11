package es.degrassi.forge.core.common.capability;

import es.degrassi.forge.api.core.capability.IHeatStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Declaration of custom capabilities
 * @apiNote Do not instantiate this class either extends from it
 */
public final class DegrassiCaps {
  public static final Capability<IHeatStorage> HEAT = CapabilityManager.get(new CapabilityToken<>() {});

  private DegrassiCaps() {}
}
