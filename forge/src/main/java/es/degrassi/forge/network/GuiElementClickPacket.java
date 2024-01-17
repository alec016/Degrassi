package es.degrassi.forge.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.utils.Env;
import es.degrassi.forge.init.gui.container.types.IProgressContainer;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GuiElementClickPacket extends BaseC2SMessage {

  private final ProgressGuiElement element;
  private final byte type;

  public GuiElementClickPacket(ProgressGuiElement element, byte type) {
    this.element = element;
    this.type = type;
  }

  @Override
  public MessageType getType() {
    return PacketManager.ELEMENT_CLICKED;
  }

  @Override
  public void write(@NotNull FriendlyByteBuf buf) {
    buf.writeNbt((CompoundTag) this.element.serializeNBT());
    buf.writeByte(this.type);
  }
  @Contract("_ -> new")
  public static @NotNull GuiElementClickPacket read(@NotNull FriendlyByteBuf buf) {
    return new GuiElementClickPacket(new ProgressGuiElement(buf.readNbt()), buf.readByte());
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    if(context.getEnvironment() == Env.SERVER)
      context.queue(() -> {
        Player player = context.getPlayer();
        if(player != null && player.containerMenu instanceof IProgressContainer<?>)
          ((IProgressContainer<?>) player.containerMenu).elementClicked(this.element, this.type);
      });
  }
}
