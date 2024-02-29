package es.degrassi.forge.api.core.element;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.codec.NamedCodec;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ElementType<T extends IGuiElement> {
  public static final ResourceKey<Registry<ElementType<? extends IGuiElement>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Degrassi.rl("gui_element_type"));

  public static <T extends IGuiElement> ElementType<T> create(NamedCodec<T> codec) {
    return new ElementType<>(codec);
  }
  public static <T extends IGuiElement> ElementType<T> create(Function<IElementManager, T> defaultElementBuilder) {
    return new ElementType<>(defaultElementBuilder);
  }

  private NamedCodec<T> codec;
  private boolean defaultElement = false;
  private boolean isSingle = true;
  private Function<IElementManager, T> defaultElementBuilder;
  private BiFunction<IElementManager, List<T>, IElementHandler<T>> handlerBuilder;
  private ElementType(NamedCodec<T> codec) {
    this.codec = codec;
  }
  private ElementType(Function<IElementManager, T> defaultElementBuilder) {
    this.defaultElement = true;
    this.defaultElementBuilder = defaultElementBuilder;
  }

  public ElementType<T> setNotSingle(BiFunction<IElementManager, List<T>, IElementHandler<T>> handlerBuilder) {
    this.isSingle = false;
    this.handlerBuilder = handlerBuilder;
    return this;
  }

  public IElementHandler<T> getHandler(IElementManager manager, List<T> elements) {
    if (this.isSingle || this.handlerBuilder == null) {
      return null;
    }
    return this.handlerBuilder.apply(manager, elements);
  }

  public boolean isDefaultElement() {
    return defaultElement;
  }

  public Function<IElementManager, T> getDefaultElementBuilder() {
    return defaultElementBuilder;
  }

  public boolean isSingle() {
    return this.isSingle;
  }

  public NamedCodec<T> getCodec() {
    return this.codec;
  }
  public ResourceLocation getId() {
    return Degrassi.elementRegistrar().getId(this);
  }

}
