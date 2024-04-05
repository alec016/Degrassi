package es.degrassi.forge.core.common.conduit.common.integrations;

import es.degrassi.common.integration.IntegrationManager;
import es.degrassi.common.integration.IntegrationWrapper;

public class Integrations {
  public static final IntegrationWrapper<ConduitSelfIntegration> SELF_INTEGRATION = IntegrationManager.wrapper("degrassi", ConduitSelfIntegration::new);

  public static void register() {
  }
}
