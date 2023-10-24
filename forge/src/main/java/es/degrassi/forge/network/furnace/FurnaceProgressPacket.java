package es.degrassi.forge.network.furnace;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.init.entity.furnace.FurnaceEntity;
import es.degrassi.forge.init.gui.container.furnace.FurnaceContainer;
import es.degrassi.forge.network.PacketManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FurnaceProgressPacket extends BaseS2CMessage {
  protected final int progress;
  protected final int maxProgress;
  private final BlockPos pos;

  public FurnaceProgressPacket(int progress, int maxProgress, BlockPos pos) {
    this.progress = progress;
    this.maxProgress = maxProgress;
    this.pos = pos;
  }

  public FurnaceProgressPacket(@NotNull FriendlyByteBuf buf) {
    this.progress = buf.readInt();
    this.maxProgress = buf.readInt();
    this.pos = buf.readBlockPos();
  }

  @Override
  public MessageType getType() {
    return PacketManager.FURNACE_PROGRESS;
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
      if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FurnaceEntity entity) {
        entity.setProgress(progress);
        entity.setMaxProgress(maxProgress);
        if (Minecraft.getInstance().player.containerMenu instanceof FurnaceContainer menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity.setProgress(progress);
          entity.setMaxProgress(maxProgress);
        }
      }
    });
  }

  @Contract("_ -> new")
  public static @NotNull FurnaceProgressPacket read(@NotNull FriendlyByteBuf buf) {
    return new FurnaceProgressPacket(buf);
  }
}
