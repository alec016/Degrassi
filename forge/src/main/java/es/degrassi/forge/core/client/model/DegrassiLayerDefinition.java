package es.degrassi.forge.core.client.model;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import es.degrassi.forge.Degrassi;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class DegrassiLayerDefinition {
  public static final ModelLayerLocation CHEST = new ModelLayerLocation(Degrassi.rl("models"), "chest");

  public static void register() {
    EntityModelLayerRegistry.register(CHEST, ChestModel::createDefinition);
  }
}
