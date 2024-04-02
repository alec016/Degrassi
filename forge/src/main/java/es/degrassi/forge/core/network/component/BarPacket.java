package es.degrassi.forge.core.network.component;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.core.common.component.BarComponent;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.PacketRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BarPacket extends BaseS2CMessage {
  private final double amount, capacity;
  private final String id;
  private final BlockPos pos;
  public BarPacket(double amount, double capacity, String id, BlockPos pos) {
    this.amount = amount;
    this.capacity = capacity;
    this.id = id;
    this.pos = pos;
  }
  @Override
  public MessageType getType() {
    return PacketRegistration.BAR;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeDouble(amount);
    buf.writeDouble(capacity);
    buf.writeUtf(id);
    buf.writeBlockPos(pos);
  }

  @Contract("_ -> new")
  public static @NotNull BarPacket read(@NotNull FriendlyByteBuf buf) {
    return new BarPacket(buf.readDouble(), buf.readDouble(), buf.readUtf(), buf.readBlockPos());
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof MachineEntity<?> entity) {
        entity
          .getComponentManager()
          .getComponent(id)
          .map(component -> (BarComponent) component)
          .ifPresent(component -> {
            component.setAmount(amount);
            component.setCapacity(capacity);
          });
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.containerMenu instanceof MachineContainer<?> menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity
            .getComponentManager()
            .getComponent(id)
            .map(component -> (BarComponent) component)
            .ifPresent(component -> {
              component.setAmount(amount);
              component.setCapacity(capacity);
            });
        }
      }
    });
  }
}
