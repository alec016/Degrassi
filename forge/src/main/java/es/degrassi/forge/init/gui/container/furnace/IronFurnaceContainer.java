package es.degrassi.forge.init.gui.container.furnace;

import es.degrassi.forge.init.entity.furnace.FurnaceEntity;
import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ContainerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class IronFurnaceContainer extends FurnaceContainer {
  protected IronFurnaceContainer(int i, Inventory inv, FurnaceEntity entity, ContainerData data) {
    super(ContainerRegistry.IRON_FURNACE_CONTAINER.get(), i, entity, inv);
    this.data = data;

    this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
      this.addSlot(new SlotItemHandler(handler, 0, 8, 25)); // speed upgrade
      this.addSlot(new SlotItemHandler(handler, 1, 8, 43)); // energy upgrade
      this.addSlot(new SlotItemHandler(handler, 2, 44, 34)); // input
      this.addSlot(new SlotItemHandler(handler, 3, 134, 34)); // output
    });

    addDataSlots(data);
  }

  public IronFurnaceContainer(int id, Inventory inv, FurnaceEntity entity) {
    this(id, inv, entity, new SimpleContainerData(4));
  }

  public IronFurnaceContainer(int id, Inventory playerInv, @NotNull FriendlyByteBuf extraData) {
    this(
      id,
      playerInv,
      ClientHandler.getClientSideFurnaceEntity(extraData.readBlockPos())
    );
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
      player, BlockRegister.IRON_FURNACE_BLOCK.get());
  }

  @Override
  public Component getId() {
    return Component.literal("Iron Furnace Container");
  }
}
