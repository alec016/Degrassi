package es.degrassi.forge.core.client.renderer.cable;

import es.degrassi.forge.core.client.model.cable.EnergyCableModel;
import es.degrassi.forge.core.client.model.DegrassiLayerDefinition;
import es.degrassi.forge.core.common.cables.energy.EnergyCableEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class EnergyCableRenderer extends CableRenderer<EnergyCableEntity, EnergyCableModel, EnergyCableRenderer> {

  public EnergyCableRenderer(BlockEntityRendererProvider.Context context) {
    super(context, new EnergyCableModel(context.bakeLayer(DegrassiLayerDefinition.CABLE)));
  }
}
