package es.degrassi.network.furnace;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.utils.Env;
import es.degrassi.comon.init.entity.furnace.FurnaceEntity;
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

public class FurnaceItemPacket extends BaseS2CMessage {
  protected final ItemStackHandler itemStackHandler;
  protected final BlockPos pos;
  public FurnaceItemPacket(ItemStackHandler itemStackHandler, BlockPos pos) {
    this.itemStackHandler = itemStackHandler;
    this.pos = pos;
  }

  @Override
  public MessageType getType() {
    return PacketManager.FURNACE_ITEM;
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
        if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FurnaceEntity entity) {
          entity.setHandler(this.itemStackHandler);
        }
      });
  }

  @Contract("_ -> new")
  public static @NotNull FurnaceItemPacket read(@NotNull FriendlyByteBuf buf) {
    List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
    ItemStackHandler itemStackHandler = new ItemStackHandler(collection.size());

    for (int i = 0; i < collection.size(); i++) {
      itemStackHandler.insertItem(i, collection.get(i), false);
    }
    return new FurnaceItemPacket(itemStackHandler, buf.readBlockPos());
  }
}
