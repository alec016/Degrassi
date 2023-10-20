package es.degrassi.network.furnace;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.comon.init.entity.furnace.FurnaceEntity;
import es.degrassi.comon.init.gui.container.furnace.FurnaceContainer;
import es.degrassi.network.PacketManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FurnaceEnergyPacket extends BaseS2CMessage {
  private final int energy;
  private final int capacity;
  private final int transfer;
  private final BlockPos pos;

  public FurnaceEnergyPacket(int energy, int capacity, int transfer, BlockPos pos) {
    this.energy = energy;
    this.capacity = capacity;
    this.transfer = transfer;
    this.pos = pos;
  }

  public FurnaceEnergyPacket(@NotNull FriendlyByteBuf buf) {
    this.energy = buf.readInt();
    this.capacity = buf.readInt();
    this.transfer = buf.readInt();
    this.pos = buf.readBlockPos();
  }


  @Override
  public MessageType getType() {
    return PacketManager.FURNACE_ENERGY;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeInt(energy);
    buf.writeInt(capacity);
    buf.writeInt(transfer);
    buf.writeBlockPos(pos);
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      assert Minecraft.getInstance().level != null;
      if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FurnaceEntity entity) {
        entity.setEnergyLevel(energy);
        entity.setCapacityLevel(capacity);
        entity.setTransferRate(transfer);

        if (Minecraft.getInstance().player.containerMenu instanceof FurnaceContainer menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity.setEnergyLevel(energy);
          entity.setCapacityLevel(capacity);
          entity.setTransferRate(transfer);
        }
      }
    });
  }

  @Contract("_ -> new")
  public static @NotNull FurnaceEnergyPacket read(@NotNull FriendlyByteBuf buf) {
    return new FurnaceEnergyPacket(buf);
  }
}
