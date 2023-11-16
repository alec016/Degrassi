package es.degrassi.forge.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.init.entity.type.IEfficiencyEntity;
import es.degrassi.forge.init.gui.container.BaseContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class EfficiencyPacket extends BaseS2CMessage {
  private final double efficiency;
  private final BlockPos pos;

  public EfficiencyPacket(double efficiency, BlockPos pos) {
    this.efficiency = efficiency;
    this.pos = pos;
  }

  public EfficiencyPacket(@NotNull FriendlyByteBuf buf) {
    this.efficiency = buf.readDouble();
    this.pos = buf.readBlockPos();
  }

  @Override
  public MessageType getType() {
    return PacketManager.EFFICIENCY;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeDouble(efficiency);
    buf.writeBlockPos(pos);
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      assert Minecraft.getInstance().level != null;
      if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof IEfficiencyEntity entity) {
        entity.getCurrentEfficiency().setEfficiency(efficiency / 100);

        if (Minecraft.getInstance().player.containerMenu instanceof BaseContainer<?> menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity.getCurrentEfficiency().setEfficiency(efficiency / 100);
        }
      }
    });
  }

  @Contract("_ -> new")
  public static @NotNull EfficiencyPacket read(@NotNull FriendlyByteBuf buf) {
    return new EfficiencyPacket(buf);
  }
}
