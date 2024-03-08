package es.degrassi.forge.core.network.component;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.PacketRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ExperiencePacket extends BaseS2CMessage {
  private final float experience;
  private final String id;
  private final BlockPos pos;
  public ExperiencePacket(float experience, String id, BlockPos pos) {
    this.experience = experience;
    this.id = id;
    this.pos = pos;
  }

  @Override
  public MessageType getType() {
    return PacketRegistration.EXPERIENCE;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeFloat(experience);
    buf.writeUtf(id);
    buf.writeBlockPos(pos);
  }

  @Contract("_ -> new")
  public static @NotNull ExperiencePacket read(@NotNull FriendlyByteBuf buf) {
    return new ExperiencePacket(buf.readFloat(), buf.readUtf(), buf.readBlockPos());
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof MachineEntity<?> entity) {
        entity
          .getComponentManager()
          .getComponent(id)
          .map(component -> (ExperienceComponent) component)
          .ifPresent(component -> component.setExperience(experience));
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.containerMenu instanceof MachineContainer<?> menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity
            .getComponentManager()
            .getComponent(id)
            .map(component -> (ExperienceComponent) component)
            .ifPresent(component -> component.setExperience(experience));
        }
      }
    });
  }
}
