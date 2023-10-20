package es.degrassi.forge.init.gui.container.sp;

import es.degrassi.comon.init.entity.panel.PanelEntity;
import es.degrassi.comon.init.gui.container.panel.PanelContainer;
import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ContainerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SP6Container extends PanelContainer {
  public SP6Container(int i, Inventory inv, PanelEntity entity, ContainerData data) {
    super(ContainerRegistry.SP6_CONTAINER.get(), i, entity, inv);
    this.data = data;

    this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
      this.addSlot(new SlotItemHandler(handler, 0, 8, 21));
      this.addSlot(new SlotItemHandler(handler, 1, 8, 39));
      this.addSlot(new SlotItemHandler(handler, 2, 8, 57));
      this.addSlot(new SlotItemHandler(handler, 3, 8, 75));
    });

    addDataSlots(data);
  }

  public SP6Container(int id, Inventory inv, PanelEntity entity) {
    this(id, inv, entity, new SimpleContainerData(4));
  }

  public SP6Container(int id, Inventory playerInv, @NotNull FriendlyByteBuf extraData) {
    this(
      id,
      playerInv,
      ClientHandler.getClientSidePanelEntity(extraData.readBlockPos())
    );
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
      player, BlockRegister.SP6_BLOCK.get());
  }
}
