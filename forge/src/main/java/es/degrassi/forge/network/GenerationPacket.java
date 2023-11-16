package es.degrassi.forge.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import es.degrassi.forge.init.entity.type.IEnergyEntity.IGenerationEntity;
import es.degrassi.forge.init.gui.container.BaseContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GenerationPacket extends BaseS2CMessage {
  private final int generation;
  private final BlockPos pos;

  public GenerationPacket(int generation, BlockPos pos) {
    this.generation = generation;
    this.pos = pos;
  }

  public GenerationPacket(@NotNull FriendlyByteBuf buf) {
    this.generation = buf.readInt();
    this.pos = buf.readBlockPos();
  }

  @Override
  public MessageType getType() {
    return PacketManager.GENERATION;
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
      if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof IGenerationEntity entity) {
        entity.getGenerationStorage().setGeneration(generation);

        if (Minecraft.getInstance().player.containerMenu instanceof BaseContainer<?> menu &&
          menu.getEntity().getBlockPos().equals(pos)
        ) {
          entity.getGenerationStorage().setGeneration(generation);
        }
      }
    });
  }

  @Contract("_ -> new")
  public static @NotNull GenerationPacket read(@NotNull FriendlyByteBuf buf) {
    return new GenerationPacket(buf);
  }
}
