package es.degrassi.forge.core.client.renderer.cable;

import es.degrassi.forge.core.client.model.DegrassiLayerDefinition;
import es.degrassi.forge.core.client.model.cable.FluidCableModel;
import es.degrassi.forge.core.common.cables.fluid.FluidCableEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class FluidCableRenderer extends CableRenderer<FluidCableEntity, FluidCableModel, FluidCableRenderer> {

  public FluidCableRenderer(BlockEntityRendererProvider.Context context) {
    super(context, new FluidCableModel(context.bakeLayer(DegrassiLayerDefinition.CABLE)));
  }
}
