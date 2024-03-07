package es.degrassi.forge.core.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.network.component.EnergyPacket;
import es.degrassi.forge.core.network.component.ExperiencePacket;
import es.degrassi.forge.core.network.component.ItemPacket;

public class PacketRegistration {
  public static final SimpleNetworkManager MANAGER = SimpleNetworkManager.create(Degrassi.MODID);
  public static final MessageType ENERGY = MANAGER.registerS2C("energy", EnergyPacket::read);
  public static final MessageType ITEM = MANAGER.registerS2C("item", ItemPacket::read);
  public static final MessageType EXPERIENCE = MANAGER.registerS2C("experience", ExperiencePacket::read);

  public static void init() {}
}
