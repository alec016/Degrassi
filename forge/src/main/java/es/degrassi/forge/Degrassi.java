package es.degrassi.forge;

import dev.architectury.registry.registries.Registrar;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.registration.Register;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
}
