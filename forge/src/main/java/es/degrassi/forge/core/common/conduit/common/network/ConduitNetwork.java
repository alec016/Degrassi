package es.degrassi.forge.core.common.conduit.common.network;

public class ConduitNetwork {
    public static void register() {
        CoreNetwork.registerPacket(new C2SSetConduitConnectionState.Handler(), C2SSetConduitConnectionState.class);
        CoreNetwork.registerPacket(new C2SSetConduitExtendedData.Handler(), C2SSetConduitExtendedData.class);
    }

}
