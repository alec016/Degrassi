package es.degrassi.forge.network.panel;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.init.entity.panel.PanelEntity;
import es.degrassi.forge.init.gui.container.panel.PanelContainer;
import es.degrassi.forge.network.PacketManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PanelGenerationPacket extends BaseS2CMessage {
  private final int generation;
  private final BlockPos pos;

  public PanelGenerationPacket(int generation, BlockPos pos) {
    this.generation = generation;
    this.pos = pos;
  }

  public PanelGenerationPacket(@NotNull FriendlyByteBuf buf) {
    this.generation = buf.readInt();
    this.pos = buf.readBlockPos();
  }

  @Override
  public MessageType getType() {
    return PacketManager.PANEL_GENERATION;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeInt(generation);
    buf.writeBlockPos(pos);
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    context.queue(() -> {
      assert Minecraft.getInstance().level != null;
      if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof PanelEntity entity) {
        entity.getGenerationStorage().setGeneration(generation);

        if (Minecraft.getInstance().player.containerMenu instanceof PanelContainer menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity.getGenerationStorage().setGeneration(generation);
        }
      }
    });
  }

  @Contract("_ -> new")
  public static @NotNull PanelGenerationPacket read(@NotNull FriendlyByteBuf buf) {
    return new PanelGenerationPacket(buf);
  }
}
