package es.degrassi.network.panel;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.utils.Env;
import es.degrassi.comon.init.entity.panel.SolarPanelEntity;
import es.degrassi.network.PacketManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PanelItemPacket extends BaseS2CMessage {
  protected final ItemStackHandler itemStackHandler;
  protected final BlockPos pos;

  public PanelItemPacket(ItemStackHandler itemStackHandler, BlockPos pos) {
    this.itemStackHandler = itemStackHandler;
    this.pos = pos;
  }

  public PanelItemPacket(FriendlyByteBuf buf) {
    this(getItemStackHandlerFromBuf(buf), buf.readBlockPos());
  }

  public MessageType getType() {
    return PacketManager.PANEL_ITEMS;
  }

  @Contract("_ -> new")
  public static @NotNull PanelItemPacket read(@NotNull FriendlyByteBuf buf) {
    return new PanelItemPacket(buf);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    Collection<ItemStack> list = new ArrayList<>();

    for (int i = 0; i < itemStackHandler.getSlots(); i++) {
      list.add(itemStackHandler.getStackInSlot(i));
    }

    buf.writeCollection(list, FriendlyByteBuf::writeItem);
    buf.writeBlockPos(pos);
  }

  @Override
  public void handle(NetworkManager.@NotNull PacketContext context) {
    if (context.getEnvironment() == Env.CLIENT)
      context.queue(() -> {
        assert Minecraft.getInstance().level != null;
        if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof SolarPanelEntity entity) {
          entity.setHandler(this.itemStackHandler);
        }
      });
  }

  private static @NotNull ItemStackHandler getItemStackHandlerFromBuf(@NotNull FriendlyByteBuf buf) {
    List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
    ItemStackHandler itemStackHandler = new ItemStackHandler(collection.size());

    for (int i = 0; i < collection.size(); i++) {
      itemStackHandler.insertItem(i, collection.get(i), false);
    }

    return itemStackHandler;
  }
}
