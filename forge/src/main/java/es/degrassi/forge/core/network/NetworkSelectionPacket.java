package es.degrassi.forge.core.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.utils.Env;
import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.digital.block.entity.DigitalControllerEntity;
import es.degrassi.forge.core.digital.client.container.DigitalControllerContainer;
import es.degrassi.forge.core.digital.network.Network;
import es.degrassi.forge.core.digital.util.Networks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class NetworkSelectionPacket extends BaseC2SMessage {
  private final Network frequency;
  private final BlockPos pos;

  public NetworkSelectionPacket(String frequency, BlockPos pos) {
    this.frequency = DigitalControllerContainer.getNetworks().stream().filter(freq -> freq.getFrequency().equals(frequency)).findFirst().orElse(null);
    this.frequency.setController(pos);
    this.pos = pos;

    DegrassiLogger.INSTANCE.info("Found network for frequency {}: {}", frequency, this.frequency);
  }
  @Override
  public MessageType getType() {
    return PacketRegistration.NETWORK_SELECTED;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeUtf(frequency.getFrequency());
    buf.writeBlockPos(pos);
  }

  @Contract("_ -> new")
  public static @NotNull NetworkSelectionPacket read(@NotNull FriendlyByteBuf buf) {
    return new NetworkSelectionPacket(buf.readUtf(), buf.readBlockPos());
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    if (context.getEnvironment() == Env.SERVER)
      context.queue(() -> {
        Player player = context.getPlayer();
        if (player != null && player.containerMenu instanceof MachineContainer<?> container)
          if (container.getEntity() instanceof DigitalControllerEntity entity) {
            ServerLevel level = (ServerLevel) player.level;
            Networks.get(level).getOrCreateNetwork(frequency.getFrequency(), level).setController(pos);
//            entity.onNetworkSelected(frequency, pos);
          }
      });
  }
}
