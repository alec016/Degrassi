package es.degrassi.forge.core.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.storage.StorageEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;

public class SideConfigPacket extends BaseS2CMessage {
  private final Direction side;
  private final ComponentIOMode mode;
  private final BlockPos pos;

  public SideConfigPacket(Direction side, ComponentIOMode mode , BlockPos pos) {
    this.side = side;
    this.mode = mode;
    this.pos = pos;
  }

  @Override
  public MessageType getType() {
    return PacketRegistration.SIDE_CONFIG;
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeUtf(side.getSerializedName());
    buf.writeUtf(mode.serialize());
    buf.writeBlockPos(pos);
  }

  public static SideConfigPacket read(FriendlyByteBuf buf) {
    return new SideConfigPacket(Direction.byName(buf.readUtf()), ComponentIOMode.deserialize(buf.readUtf()), buf.readBlockPos());
  }

  @Override
  public void handle(NetworkManager.PacketContext context) {
    context.queue(() -> {
      if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof StorageEntity<?> entity)
        entity.getConfig().setMode(side, mode);
    });
  }
}
