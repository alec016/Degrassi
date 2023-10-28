package es.degrassi.forge.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.network.furnace.FurnaceExperiencePacket;
import es.degrassi.forge.network.panel.PanelEfficiencyPacket;
import es.degrassi.forge.network.panel.PanelGenerationPacket;

public class PacketManager {

    public static final SimpleNetworkManager MANAGER = SimpleNetworkManager.create(Degrassi.MODID);

    // Server to Client
    public static final MessageType OPEN_FILE = MANAGER.registerS2C("open_file", SOpenFilePacket::read);
    public static final MessageType ENERGY = MANAGER.registerS2C("energy", EnergyPacket::read);
    public static final MessageType ITEM = MANAGER.registerS2C("item", ItemPacket::read);
    public static final MessageType FLUID = MANAGER.registerS2C("fluid", FluidPacket::read);
    public static final MessageType PROGRESS = MANAGER.registerS2C("progress", ProgressPacket::read);

    //panel
    public static final MessageType PANEL_EFFICIENCY = MANAGER.registerS2C("panel_efficiency", PanelEfficiencyPacket::read);
    public static final MessageType PANEL_GENERATION = MANAGER.registerS2C("panel_generation", PanelGenerationPacket::read);

    // furnace
    public static final MessageType FURNACE_EXPERIENCE = MANAGER.registerS2C("furnace_experience", FurnaceExperiencePacket::read);


    // Client to Server

    public static void init() {
    }
}
