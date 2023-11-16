package es.degrassi.forge.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.init.entity.type.IExperienceEntity;
import es.degrassi.forge.init.gui.container.BaseContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ExperiencePacket extends BaseS2CMessage {
  private final float xp;
  private final BlockPos pos;

  public ExperiencePacket(BlockPos pos) {
    this(0, pos);
  }

  public ExperiencePacket(float xp, BlockPos pos) {
    this.xp = xp;
    this.pos = pos;
  }

  public ExperiencePacket(@NotNull FriendlyByteBuf buf) {
    this.xp = buf.readFloat();
    this.pos = buf.readBlockPos();
  }

  @Override
  public MessageType getType() {
    return PacketManager.EXPERIENCE;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeFloat(xp);
    buf.writeBlockPos(pos);
  }


  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      assert Minecraft.getInstance().level != null;
      if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof IExperienceEntity entity) {
        entity.setXp(xp);
        if (Minecraft.getInstance().player.containerMenu instanceof BaseContainer<?> menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity.setXp(xp);
        }
      }
    });
  }

  @Contract("_ -> new")
  public static @NotNull ExperiencePacket read(@NotNull FriendlyByteBuf buf) {
    return new ExperiencePacket(buf);
  }
}
