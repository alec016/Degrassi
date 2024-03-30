package es.degrassi.forge.core.client.model;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import es.degrassi.forge.Degrassi;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class DegrassiLayerDefinition {
  public static final ModelLayerLocation CABLE = new ModelLayerLocation(Degrassi.rl("models"), "cable");

  public static void register() {
    EntityModelLayerRegistry.register(CABLE, CableModel::createDefinition);
  }
}
