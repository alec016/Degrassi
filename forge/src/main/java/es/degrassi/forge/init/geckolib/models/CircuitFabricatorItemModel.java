package es.degrassi.forge.init.geckolib.models;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.geckolib.item.CircuitFabricatorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CircuitFabricatorItemModel extends AnimatedGeoModel<CircuitFabricatorItem> {
  private static final ResourceLocation MODEL = new DegrassiLocation("geo/circuit_fabricator.geo.json");
  private static final ResourceLocation TEXTURE = new DegrassiLocation("textures/block/circuit_fabricator/circuit_fabricator.png");
  private static final ResourceLocation ANIMATION = new DegrassiLocation("animations/circuit_fabricator.animation.json");
  @Override
  public ResourceLocation getModelResource(CircuitFabricatorItem object) {
    return MODEL;
  }

  @Override
  public ResourceLocation getTextureResource(CircuitFabricatorItem object) {
    return TEXTURE;
  }

  @Override
  public ResourceLocation getAnimationResource(CircuitFabricatorItem animatable) {
    return ANIMATION;
  }
}
