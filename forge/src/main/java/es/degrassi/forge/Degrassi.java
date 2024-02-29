package es.degrassi.forge;

import dev.architectury.registry.registries.Registrar;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.api.core.component.variant.ComponentVariantRegistry;
import es.degrassi.forge.api.core.component.variant.IComponentVariant;
import es.degrassi.forge.api.core.element.ElementType;
import es.degrassi.forge.api.core.network.DataType;
import es.degrassi.forge.init.registration.ComponentRegistration;
import es.degrassi.forge.init.registration.DataRegistration;
import es.degrassi.forge.init.registration.ElementRegistration;
import es.degrassi.forge.init.registration.Register;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Degrassi {
  public static final String MODID = "degrassi";
  public static Logger LOGGER = LogManager.getLogger("Degrassi");

  public static void init() {
  }

  @Contract("_ -> new")
  public static @NotNull DegrassiLocation rl(String path) {
    return new DegrassiLocation(path);
  }

  public static <T> Registrar<T> registrar(ResourceKey<Registry<T>> registryKey) {
    return Register.REGISTRIES.get(registryKey);
  }

  public static Registrar<ComponentType<?>> componentRegistrar() {
    return ComponentRegistration.COMPONENT_TYPE_REGISTRY;
  }

  public static Registrar<ElementType<?>> elementRegistrar() {
    return ElementRegistration.ELEMENT_TYPE_REGISTRY;
  }

  @Nullable
  public static <C extends IComponent> NamedCodec<IComponentVariant> getVariantCodec(ComponentType<C> type, ResourceLocation id) {
    return ComponentVariantRegistry.getVariantCodec(type, id);
  }
  public static Registrar<DataType<?, ?>> dataRegistrar() {
    return DataRegistration.DATA_REGISTRY;
  }
}
