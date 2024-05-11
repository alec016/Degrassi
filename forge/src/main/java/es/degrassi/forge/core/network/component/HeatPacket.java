package es.degrassi.forge.core.network.component;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.core.common.component.HeatComponent;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.PacketRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class HeatPacket extends BaseS2CMessage {
  private final double heat, heatCapacity;
  private final String id;
  private final BlockPos pos;

  public HeatPacket(double heat, double heatCapacity, String id, BlockPos pos) {
    this.heat = heat;
    this.heatCapacity = heatCapacity;
    this.id = id;
    this.pos = pos;
  }
  @Override
  public MessageType getType() {
    return PacketRegistration.HEAT;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeDouble(heat);
    buf.writeDouble(heatCapacity);
    buf.writeUtf(id);
    buf.writeBlockPos(pos);
  }

  @Contract("_ -> new")
  public static @NotNull HeatPacket read(@NotNull FriendlyByteBuf buf) {
    return new HeatPacket(buf.readDouble(), buf.readDouble(), buf.readUtf(), buf.readBlockPos());
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof MachineEntity<?> entity) {
        entity
          .getComponentManager()
          .getComponent(id)
          .map(component -> (HeatComponent) component)
          .ifPresent(component -> {
            component.setHeat(heat);
            component.setHeatCapacity(heatCapacity);
          });
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.containerMenu instanceof MachineContainer<?> menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity
            .getComponentManager()
            .getComponent(id)
            .map(component -> (HeatComponent) component)
            .ifPresent(component -> {
              component.setHeat(heat);
              component.setHeatCapacity(heatCapacity);
            });
        }
      }
    });
  }
}