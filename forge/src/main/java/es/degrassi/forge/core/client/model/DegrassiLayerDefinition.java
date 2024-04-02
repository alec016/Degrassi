package es.degrassi.forge.core.client.model;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.client.model.cable.CableModel;
import es.degrassi.forge.core.client.renderer.ChestEntityRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import org.zeith.hammerlib.client.model.LoadUnbakedGeometry;

public class DegrassiLayerDefinition {
  public static final ModelLayerLocation CABLE = new ModelLayerLocation(Degrassi.rl("models"), "cable");
  public static final ModelLayerLocation CHEST = new ModelLayerLocation(Degrassi.rl("models"), "chest");

  public static void register() {
    EntityModelLayerRegistry.register(CABLE, CableModel::createDefinition);
    EntityModelLayerRegistry.register(CHEST, ChestModel::createDefinition);
  }
}
