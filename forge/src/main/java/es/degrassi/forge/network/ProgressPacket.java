package es.degrassi.forge.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.init.entity.type.IProgressEntity;
import es.degrassi.forge.init.gui.container.BaseContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ProgressPacket extends BaseS2CMessage {
  protected final int progress;
  protected final int maxProgress;
  private final BlockPos pos;

  public ProgressPacket(int progress, int maxProgress, BlockPos pos) {
    this.progress = progress;
    this.maxProgress = maxProgress;
    this.pos = pos;
  }

  public ProgressPacket(@NotNull FriendlyByteBuf buf) {
    this.progress = buf.readInt();
    this.maxProgress = buf.readInt();
    this.pos = buf.readBlockPos();
  }

  @Override
  public MessageType getType() {
    return PacketManager.PROGRESS;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeInt(progress);
    buf.writeInt(maxProgress);
    buf.writeBlockPos(pos);
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      assert Minecraft.getInstance().level != null;
      if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof IProgressEntity entity) {
        entity.setProgress(progress);
        entity.setMaxProgress(maxProgress);
        if (Minecraft.getInstance().player.containerMenu instanceof BaseContainer<?> menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity.setProgress(progress);
          entity.setMaxProgress(maxProgress);
        }
      }
    });
  }

  @Contract("_ -> new")
  public static @NotNull ProgressPacket read(@NotNull FriendlyByteBuf buf) {
    return new ProgressPacket(buf);
  }
}
