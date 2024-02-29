package es.degrassi.forge.api.impl.core.elements;

import es.degrassi.forge.api.core.element.IGuiElement;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractGuiElement implements IGuiElement {
  public record Properties(
    int x,
    int y,
    int width,
    int height,
    int priority,
    ResourceLocation texture,
    ResourceLocation textureHovered,
    List<Component> tooltips
  ) {}

  private final Properties properties;
  public AbstractGuiElement(Properties properties) {
    this.properties = properties;
  }

  public Properties getProperties() {
    return properties;
  }

  @Override
  public int getX() {
    return properties.x();
  }

  @Override
  public int getY() {
    return properties.y();
  }

  @Override
  public int getWidth() {
    return properties.width();
  }

  @Override
  public int getHeight() {
    return properties.height();
  }

  @Override
  public int getPriority() {
    return properties.priority();
  }

  @Override
  public List<Component> getTooltips() {
    return properties.tooltips();
  }
}
