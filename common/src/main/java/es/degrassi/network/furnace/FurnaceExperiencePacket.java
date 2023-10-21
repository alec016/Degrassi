package es.degrassi.network.furnace;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.comon.init.entity.furnace.FurnaceEntity;
import es.degrassi.comon.init.gui.container.furnace.FurnaceContainer;
import es.degrassi.network.PacketManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FurnaceExperiencePacket extends BaseS2CMessage {
  private final float xp;
  private final BlockPos pos;

  public FurnaceExperiencePacket(BlockPos pos) {
    this(0, pos);
  }

  public FurnaceExperiencePacket(float xp, BlockPos pos) {
    this.xp = xp;
    this.pos = pos;
  }

  public FurnaceExperiencePacket(@NotNull FriendlyByteBuf buf) {
    this.xp = buf.readFloat();
    this.pos = buf.readBlockPos();
  }

  @Override
  public MessageType getType() {
    return PacketManager.FURNACE_EXPERIENCE;
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
      if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FurnaceEntity entity) {
        entity.setXp(xp);
        if (Minecraft.getInstance().player.containerMenu instanceof FurnaceContainer menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity.setXp(xp);
        }
      }
    });
  }

  @Contract("_ -> new")
  public static @NotNull FurnaceExperiencePacket read(@NotNull FriendlyByteBuf buf) {
    return new FurnaceExperiencePacket(buf);
  }
}
