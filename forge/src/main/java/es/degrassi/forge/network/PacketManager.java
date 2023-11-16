package es.degrassi.forge.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import es.degrassi.forge.Degrassi;

public class PacketManager {

    public static final SimpleNetworkManager MANAGER = SimpleNetworkManager.create(Degrassi.MODID);

    // Server to Client
    public static final MessageType OPEN_FILE = MANAGER.registerS2C("open_file", SOpenFilePacket::read);
    public static final MessageType ENERGY = MANAGER.registerS2C("energy", EnergyPacket::read);
    public static final MessageType ITEM = MANAGER.registerS2C("item", ItemPacket::read);
    public static final MessageType FLUID = MANAGER.registerS2C("fluid", FluidPacket::read);
    public static final MessageType PROGRESS = MANAGER.registerS2C("progress", ProgressPacket::read);
    public static final MessageType EFFICIENCY = MANAGER.registerS2C("efficiency", EfficiencyPacket::read);
    public static final MessageType GENERATION = MANAGER.registerS2C("generation", GenerationPacket::read);
    public static final MessageType EXPERIENCE = MANAGER.registerS2C("experience", ExperiencePacket::read);


    // Client to Server

    public static void init() {
    }
}
