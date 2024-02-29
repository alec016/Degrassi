package es.degrassi.forge.api.core.component;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.component.builder.IComponentBuilder;
import es.degrassi.forge.api.core.component.handler.IComponentHandler;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ComponentType<T extends IComponent> {
  public static final ResourceKey<Registry<ComponentType<? extends IComponent>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Degrassi.rl("component_type"));

  public static <T extends IComponent> ComponentType<T> create(NamedCodec<? extends IComponentTemplate<T>> codec) {
    return new ComponentType<T>(codec);
  }
  public static <T extends IComponent> ComponentType<T> create(Function<IComponentManager, T> defaultComponentBuilder) {
    return new ComponentType<T>(defaultComponentBuilder);
  }
  public static <T extends IComponent> ComponentType<T> create(NamedCodec<? extends IComponentTemplate<T>> codec, Function<IComponentManager, T> defaultComponentBuilder) {
    return new ComponentType<T>(codec, defaultComponentBuilder);
  }

  private NamedCodec<? extends IComponentTemplate<T>> codec;
  private boolean isSingle = true;
  private BiFunction<IComponentManager, List<T>, IComponentHandler<T>> handlerBuilder;
  private boolean defaultComponent = false;
  private Function<IComponentManager, T> defaultComponentBuilder;
  private Supplier<IComponentBuilder<T>> GUIComponentBuilder;
  private ComponentType(NamedCodec<? extends IComponentTemplate<T>> codec) {
    this.codec = codec;
  }
  private ComponentType(Function<IComponentManager, T> defaultComponentBuilder) {
    this.defaultComponent = true;
    this.defaultComponentBuilder = defaultComponentBuilder;
  }
  private ComponentType(NamedCodec<? extends IComponentTemplate<T>> codec, Function<IComponentManager, T> defaultComponentBuilder) {
    this.codec = codec;
    this.defaultComponent = true;
    this.defaultComponentBuilder = defaultComponentBuilder;
  }
  public ComponentType<T> setNotSingle(BiFunction<IComponentManager, List<T>, IComponentHandler<T>> handlerBuilder) {
    this.isSingle = false;
    this.handlerBuilder = handlerBuilder;
    return this;
  }
  public NamedCodec<? extends IComponentTemplate<T>> getCodec() {
    if(this.codec != null)
      return this.codec;
    else throw new RuntimeException("Error while trying to serialize or deserialize Machine Component template: " + getId() + ", Codec not present !");
  }
  public boolean isSingle() {
    return this.isSingle;
  }
  public IComponentHandler<T> getHandler(IComponentManager manager, List<T> components) {
    if(this.isSingle || this.handlerBuilder == null)
      return null;
    return this.handlerBuilder.apply(manager, components);
  }
  public boolean isDefaultComponent() {
    return this.defaultComponent;
  }
  public Function<IComponentManager, T> getDefaultComponentBuilder() {
    return this.defaultComponentBuilder;
  }
  public ComponentType<T> setGUIBuilder(Supplier<IComponentBuilder<T>> builder) {
    this.GUIComponentBuilder = builder;
    return this;
  }
  public boolean haveGUIBuilder() {
    return this.GUIComponentBuilder != null;
  }
  public Supplier<IComponentBuilder<T>> getGUIBuilder() {
    if(this.GUIComponentBuilder != null)
      return this.GUIComponentBuilder;
    else throw new IllegalStateException("Error while trying to get a builder for Machine Component: " + getId() + " builder not present !");
  }
  public ResourceLocation getId() {
    return Degrassi.componentRegistrar().getId(this);
  }
  public Component getTranslatedName() {
    if(getId() == null)
      throw new IllegalStateException("Trying to get the registry name of an unregistered ComponentType");
    return Component.translatable(getId().getNamespace() + ".machine.component." + getId().getPath());
  }
}
