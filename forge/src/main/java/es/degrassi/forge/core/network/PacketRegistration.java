package es.degrassi.forge.core.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.network.component.BarPacket;
import es.degrassi.forge.core.network.component.EnergyPacket;
import es.degrassi.forge.core.network.component.ExperiencePacket;
import es.degrassi.forge.core.network.component.FluidPacket;
import es.degrassi.forge.core.network.component.HeatPacket;
import es.degrassi.forge.core.network.component.ItemPacket;
import es.degrassi.forge.core.network.component.ProgressPacket;

public class PacketRegistration {
  public static final SimpleNetworkManager MANAGER = SimpleNetworkManager.create(Degrassi.MODID);

  // Server to Client
  public static final MessageType ENERGY = MANAGER.registerS2C("energy", EnergyPacket::read);
  public static final MessageType BAR = MANAGER.registerS2C("bar", BarPacket::read);
  public static final MessageType ITEM = MANAGER.registerS2C("item", ItemPacket::read);
  public static final MessageType EXPERIENCE = MANAGER.registerS2C("experience", ExperiencePacket::read);
  public static final MessageType PROGRESS = MANAGER.registerS2C("progress", ProgressPacket::read);
  public static final MessageType FLUID = MANAGER.registerS2C("fluid", FluidPacket::read);
  public static final MessageType HEAT = MANAGER.registerS2C("heat", HeatPacket::read);
  public static final MessageType SIDE_CONFIG = MANAGER.registerS2C("side_config", SideConfigPacket::read);
  public static final MessageType NETWORK_FETCH = MANAGER.registerS2C("network_fetch", NetworkFetchPacket::read);

  // Client to Server
  public static final MessageType NETWORK_SELECTED = MANAGER.registerC2S("network_selected", NetworkSelectionPacket::read);

  public static void init() {}
}
