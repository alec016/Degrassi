package es.degrassi;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import es.degrassi.command.DegrassiCommand;
import es.degrassi.comon.init.recipe.helpers.furnace.FurnaceRecipeHelper;
import es.degrassi.comon.util.DegrassiLogger;
import es.degrassi.network.PacketManager;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Degrassi {
  public static final String MODID = "degrassi";
  public static Logger LOGGER = LogManager.getLogger("Degrassi");

  public static void init() {
    DegrassiLogger.init();
    CommandRegistrationEvent.EVENT.register(Degrassi::registerCommands);
  }

  private static void registerCommands(
    final @NotNull CommandDispatcher<CommandSourceStack> dispatcher,
    final CommandBuildContext registry,
    final Commands.CommandSelection selection
  ) {
    dispatcher.register(DegrassiCommand.register("degrassi"));
  }

  public static void setup() {
    PacketManager.init();
  }
}
