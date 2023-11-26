package es.degrassi.forge.init.geckolib.renderer;

import es.degrassi.forge.init.geckolib.item.CircuitFabricatorItem;
import es.degrassi.forge.init.geckolib.models.CircuitFabricatorItemModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class CircuitFabricatorItemRenderer extends GeoItemRenderer<CircuitFabricatorItem> {
  public CircuitFabricatorItemRenderer() {
    super(new CircuitFabricatorItemModel());
  }
}
