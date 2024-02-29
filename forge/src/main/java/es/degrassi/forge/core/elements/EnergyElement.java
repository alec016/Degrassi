package es.degrassi.forge.core.elements;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.element.ElementType;
import es.degrassi.forge.api.core.element.IGuiElement;
import es.degrassi.forge.api.impl.codec.DefaultCodecs;
import es.degrassi.forge.api.impl.core.elements.AbstractGuiElement;
import es.degrassi.forge.api.impl.core.elements.AbstractTexturedGuiElement;
import es.degrassi.forge.init.registration.ElementRegistration;
import net.minecraft.resources.ResourceLocation;

public class EnergyElement extends AbstractTexturedGuiElement {
  private static final ResourceLocation BASE_ENERGY_STORAGE_EMPTY = new DegrassiLocation("textures/gui/base_energy_storage_empty.png");
  private static final ResourceLocation BASE_ENERGY_STORAGE_FILLED = new  DegrassiLocation("textures/gui/base_energy_storage_filled.png");

  public static final NamedCodec<EnergyElement> CODEC = NamedCodec.record(builder -> builder.group(
    makePropertiesCodec().forGetter(AbstractGuiElement::getProperties),
    DefaultCodecs.RESOURCE_LOCATION.optionalFieldOf("textureEmpty", BASE_ENERGY_STORAGE_EMPTY).forGetter(EnergyElement::getEmptyTexture),
    DefaultCodecs.RESOURCE_LOCATION.optionalFieldOf("filledTexture", BASE_ENERGY_STORAGE_FILLED).forGetter(EnergyElement::getFilledTexture)
  ).apply(builder, EnergyElement::new), "Energy Gui Element");

  private final ResourceLocation emptyTexture;
  private final ResourceLocation filledTexture;

  public EnergyElement(Properties properties, ResourceLocation emptyTexture, ResourceLocation filledTexture) {
    super(properties);
    this.filledTexture = filledTexture;
    this.emptyTexture = emptyTexture;
  }

  @Override
  public ElementType<? extends IGuiElement> getType() {
    return ElementRegistration.ENERGY.get();
  }

  public ResourceLocation getEmptyTexture() {
    return this.emptyTexture;
  }

  public ResourceLocation getFilledTexture() {
    return this.filledTexture;
  }
}
