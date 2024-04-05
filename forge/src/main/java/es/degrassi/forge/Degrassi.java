package es.degrassi.forge;

import com.tterrag.registrate.Registrate;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandPerformEvent;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.core.common.conduit.common.network.CoreNetwork;
import es.degrassi.forge.core.network.PacketRegistration;
import net.minecraftforge.common.util.Lazy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Degrassi {
  public static final String MODID = "degrassi";
  public static Logger LOGGER = LogManager.getLogger("Degrassi");
  private static final Lazy<Registrate> REGISTRATE = Lazy.of(() -> Registrate.create(MODID));

  public static void init() {
    DegrassiLogger.init();
    CoreNetwork.networkInit();
    CommandPerformEvent.EVENT.register(Degrassi::onReloadStart);
  }

  @Contract("_ -> new")
  public static @NotNull DegrassiLocation rl(String path) {
    return new DegrassiLocation(path);
  }

  public static void setup() {
    PacketRegistration.init();
  }

  private static EventResult onReloadStart(CommandPerformEvent event) {
    if(event.getResults().getReader().getString().equals("reload") && event.getResults().getContext().getSource().hasPermission(2))
      DegrassiLogger.reset();
    return EventResult.pass();
  }


  public static Registrate registrate() {
    return REGISTRATE.get();
  }
}
