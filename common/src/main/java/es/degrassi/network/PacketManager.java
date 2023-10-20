package es.degrassi.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import es.degrassi.Degrassi;
import es.degrassi.network.furnace.FurnaceEnergyPacket;
import es.degrassi.network.furnace.FurnaceItemPacket;
import es.degrassi.network.furnace.FurnaceProgressPacket;
import es.degrassi.network.panel.PanelEfficiencyPacket;
import es.degrassi.network.panel.PanelEnergyPacket;
import es.degrassi.network.panel.PanelGenerationPacket;
import es.degrassi.network.panel.PanelItemPacket;

public class PacketManager {

    public static final SimpleNetworkManager MANAGER = SimpleNetworkManager.create(Degrassi.MODID);

    // Server to Client
    public static final MessageType OPEN_FILE = MANAGER.registerS2C("open_file", SOpenFilePacket::read);

    //panel
    public static final MessageType PANEL_ITEMS = MANAGER.registerS2C("panel_items", PanelItemPacket::read);
    public static final MessageType PANEL_ENERGY = MANAGER.registerS2C("panel_energy", PanelEnergyPacket::read);
    public static final MessageType PANEL_EFFICIENCY = MANAGER.registerS2C("panel_efficiency", PanelEfficiencyPacket::read);
    public static final MessageType PANEL_GENERATION = MANAGER.registerS2C("panel_generation", PanelGenerationPacket::read);

    // furnace
    public static final MessageType FURNACE_ITEM = MANAGER.registerS2C("furnace_items", FurnaceItemPacket::read);
    public static final MessageType FURNACE_ENERGY = MANAGER.registerS2C("furnace_energy", FurnaceEnergyPacket::read);
    public static final MessageType FURNACE_PROGRESS = MANAGER.registerS2C("furnace_progress", FurnaceProgressPacket::read);


    // Client to Server

    public static void init() {
    }
}
