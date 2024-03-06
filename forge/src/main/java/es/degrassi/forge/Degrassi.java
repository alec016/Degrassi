package es.degrassi.forge;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.network.PacketRegistration;
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

  public static void setup() {
    PacketRegistration.init();
  }
}
