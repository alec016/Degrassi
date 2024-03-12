package es.degrassi.forge.core.network.component;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.PacketRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FluidPacket extends BaseS2CMessage {
  private final FluidStack fluid;
  private final int capacity;
  private final String id;
  private final BlockPos pos;
  public FluidPacket(FluidStack fluid, int capacity, String id, BlockPos pos) {
    this.fluid = fluid;
    this.capacity = capacity;
    this.id = id;
    this.pos = pos;
  }
  @Override
  public MessageType getType() {
    return PacketRegistration.FLUID;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeFluidStack(fluid);
    buf.writeInt(capacity);
    buf.writeUtf(id);
    buf.writeBlockPos(pos);
  }

  @Contract("_ -> new")
  public static @NotNull FluidPacket read(@NotNull FriendlyByteBuf buf) {
    return new FluidPacket(buf.readFluidStack(), buf.readInt(), buf.readUtf(), buf.readBlockPos());
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof MachineEntity<?> entity) {
        entity
          .getComponentManager()
          .getComponent(id)
          .map(component -> (FluidComponent) component)
          .ifPresent(component -> {
            component.setFluid(fluid);
            component.setCapacity(capacity);
          });
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.containerMenu instanceof MachineContainer<?> menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity
            .getComponentManager()
            .getComponent(id)
            .map(component -> (FluidComponent) component)
            .ifPresent(component -> {
              component.setFluid(fluid);
              component.setCapacity(capacity);
            });
        }
      }
    });
  }
}
