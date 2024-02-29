package es.degrassi.forge.api.impl.core.elements;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.element.IGuiElement;
import es.degrassi.forge.api.impl.codec.DefaultCodecs;
import es.degrassi.forge.api.impl.codec.NamedMapCodec;
import es.degrassi.forge.api.impl.util.TextComponentUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

  public static NamedMapCodec<Properties> makePropertiesCodec() {
    return makePropertiesCodec(null, null, Collections.emptyList());
  }

  public static NamedMapCodec<Properties> makePropertiesCodec(@Nullable ResourceLocation defaultTexture) {
    return makePropertiesCodec(defaultTexture, null, Collections.emptyList());
  }

  public static NamedMapCodec<Properties> makePropertiesCodec(@Nullable ResourceLocation defaultTexture, @Nullable ResourceLocation defaultTextureHovered) {
    return makePropertiesCodec(defaultTexture, defaultTextureHovered, Collections.emptyList());
  }

  public static NamedMapCodec<Properties> makePropertiesCodec(@Nullable ResourceLocation defaultTexture, @Nullable ResourceLocation defaultTextureHovered, @NotNull List<Component> defaultTooltips) {
    return NamedCodec.record(propertiesInstance ->
      propertiesInstance.group(
        NamedCodec.intRange(0, Integer.MAX_VALUE).fieldOf("x").forGetter(Properties::x),
        NamedCodec.intRange(0, Integer.MAX_VALUE).fieldOf("y").forGetter(Properties::y),
        NamedCodec.intRange(-1, Integer.MAX_VALUE).optionalFieldOf("width", -1).forGetter(Properties::width),
        NamedCodec.intRange(-1, Integer.MAX_VALUE).optionalFieldOf("height", -1).forGetter(Properties::height),
        NamedCodec.INT.optionalFieldOf("priority", 0).forGetter(Properties::priority),
        DefaultCodecs.RESOURCE_LOCATION.optionalFieldOf("texture").forGetter(properties -> Optional.ofNullable(properties.texture())),
        DefaultCodecs.RESOURCE_LOCATION.optionalFieldOf("texture_hovered").forGetter(properties -> Optional.ofNullable(properties.textureHovered())),
        TextComponentUtils.CODEC.listOf().optionalFieldOf("tooltips", defaultTooltips).forGetter(Properties::tooltips)
      ).apply(propertiesInstance, (x, y, width, height, priority, texture, textureHovered, tooltips) ->
        new Properties(x, y, width, height, priority, texture.orElse(defaultTexture), textureHovered.orElse(defaultTextureHovered), tooltips)
      ), "Gui element properties");
  }
}
