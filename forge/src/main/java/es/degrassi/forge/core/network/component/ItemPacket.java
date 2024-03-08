package es.degrassi.forge.core.network.component;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.core.common.component.ItemComponent;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.PacketRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ItemPacket extends BaseS2CMessage {

  private final ItemStack item;
  private final String id;
  private final BlockPos pos;
  public ItemPacket(ItemStack item, String id, BlockPos pos) {
    this.item = item;
    this.id = id;
    this.pos = pos;
  }
  @Override
  public MessageType getType() {
    return PacketRegistration.ITEM;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeItem(item);
    buf.writeUtf(id);
    buf.writeBlockPos(pos);
  }

  @Contract("_ -> new")
  public static @NotNull ItemPacket read(@NotNull FriendlyByteBuf buf) {
    return new ItemPacket(buf.readItem(), buf.readUtf(), buf.readBlockPos());
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof MachineEntity<?> entity) {
        entity
          .getComponentManager()
          .getComponent(id)
          .map(component -> (ItemComponent) component)
          .ifPresent(component -> component.setItem(item));
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.containerMenu instanceof MachineContainer<?> menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity
            .getComponentManager()
            .getComponent(id)
            .map(component -> (ItemComponent) component)
            .ifPresent(component -> component.setItem(item));
        }
      }
    });
  }
}
