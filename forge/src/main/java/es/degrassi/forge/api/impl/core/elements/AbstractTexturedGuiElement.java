package es.degrassi.forge.api.impl.core.elements;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import es.degrassi.forge.api.impl.util.TextureSizeHelper;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractTexturedGuiElement extends AbstractGuiElement {

  private final ResourceLocation texture;
  public AbstractTexturedGuiElement(AbstractGuiElement.Properties properties) {
    super(properties);
    if(properties.texture() == null)
      throw new IllegalArgumentException("Can't make a TexturedGuiElement without texture");
    this.texture = properties.texture();
  }

  public AbstractTexturedGuiElement(AbstractGuiElement.Properties properties, ResourceLocation defaultTexture) {
    super(properties);
    this.texture = defaultTexture;
  }
  public ResourceLocation getTexture() {
    return this.texture;
  }

  public ResourceLocation getTextureHovered() {
    return this.getProperties().textureHovered();
  }

  @Override
  public int getWidth() {
    if(super.getWidth() >= 0)
      return super.getWidth();
    else if(Platform.getEnvironment() == Env.CLIENT)
      return TextureSizeHelper.getTextureWidth(this.getTexture());
    else
      return -1;
  }

  @Override
  public int getHeight() {
    if(super.getHeight() >= 0)
      return super.getHeight();
    else if(Platform.getEnvironment() == Env.CLIENT)
      return TextureSizeHelper.getTextureHeight(this.getTexture());
    else
      return -1;
  }
}
