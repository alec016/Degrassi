package es.degrassi.forge.api.core.element;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.codec.NamedCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ElementType<T extends IGuiElement> {
  public static final ResourceKey<Registry<ElementType<? extends IGuiElement>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Degrassi.rl("gui_element_type"));

  public static <T extends IGuiElement> ElementType<T> create(NamedCodec<T> codec) {
    return new ElementType<>(codec);
  }
  private final NamedCodec<T> codec;
  private ElementType(NamedCodec<T> codec) {
    this.codec = codec;
  }
  public NamedCodec<T> getCodec() {
    return this.codec;
  }
  public ResourceLocation getId() {
    return Degrassi.elementRegistrar().getId(this);
  }
}
