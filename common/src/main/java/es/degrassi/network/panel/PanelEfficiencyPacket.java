package es.degrassi.network.panel;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.comon.init.entity.panel.SolarPanelEntity;
import es.degrassi.comon.init.gui.container.panel.PanelContainer;
import es.degrassi.network.PacketManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PanelEfficiencyPacket extends BaseS2CMessage {
  private final double efficiency;
  private final BlockPos pos;

  public PanelEfficiencyPacket(double efficiency, BlockPos pos) {
    this.efficiency = efficiency;
    this.pos = pos;
  }

  public PanelEfficiencyPacket(@NotNull FriendlyByteBuf buf) {
    this.efficiency = buf.readDouble();
    this.pos = buf.readBlockPos();
  }

  @Override
  public MessageType getType() {
    return PacketManager.PANEL_EFFICIENCY;
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
      if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof SolarPanelEntity entity) {
        entity.getCurrentEfficiency().setEfficiency(efficiency / 100);

        if (Minecraft.getInstance().player.containerMenu instanceof PanelContainer menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity.getCurrentEfficiency().setEfficiency(efficiency / 100);
        }
      }
    });
  }

  @Contract("_ -> new")
  public static @NotNull PanelEfficiencyPacket read(@NotNull FriendlyByteBuf buf) {
    return new PanelEfficiencyPacket(buf);
  }
}
