package es.degrassi.forge.core.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.core.digital.client.container.DigitalControllerContainer;
import es.degrassi.forge.core.digital.network.Network;
import es.degrassi.forge.core.digital.util.Networks;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class NetworkFetchPacket extends BaseS2CMessage {
  private final BlockPos pos;

  public NetworkFetchPacket(BlockPos pos) {
    this.pos = pos;
  }
  @Override
  public MessageType getType() {
    return PacketRegistration.NETWORK_FETCH;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
  }

  @Contract("_ -> new")
  public static @NotNull NetworkFetchPacket read(@NotNull FriendlyByteBuf buf) {
    return new NetworkFetchPacket(buf.readBlockPos());
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      if (context.getPlayer() != null && context.getPlayer().containerMenu instanceof DigitalControllerContainer menu &&
        menu.getEntity().getBlockPos().equals(pos)
      ) {
        List<Network> networks = Networks.get(context.getPlayer().getServer().overworld()).NETWORKS;
        DegrassiLogger.INSTANCE.info("fetching networks: {}", networks);
        menu.setNetworks(networks);
      }
    });
  }
}
