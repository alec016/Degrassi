package es.degrassi.forge;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.registries.Registrar;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.command.DegrassiCommand;
import es.degrassi.forge.init.gui.renderer.GuiElementType;
import es.degrassi.forge.init.registration.ElementRegistry;
import es.degrassi.forge.init.registration.Register;
import es.degrassi.forge.init.registration.RequirementRegistry;
import es.degrassi.forge.requirements.RequirementType;
import es.degrassi.forge.util.DegrassiLogger;
import es.degrassi.forge.network.PacketManager;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
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

  public static Registrar<GuiElementType<?>> guiElementRegistrar() {
    return ElementRegistry.GUI_ELEMENT_TYPE_REGISTRY;
  }


  public static Registrar<RequirementType<?>> requirementRegistrar() {
    return RequirementRegistry.REQUIREMENT_TYPE_REGISTRY;
  }

  public static <T> Registrar<T> registrar(ResourceKey<Registry<T>> registryKey) {
    return Register.REGISTRIES.get(registryKey);
  }

  @Contract("_ -> new")
  public static @NotNull ResourceLocation rl(String s) {
    return new DegrassiLocation(s);
  }

}
