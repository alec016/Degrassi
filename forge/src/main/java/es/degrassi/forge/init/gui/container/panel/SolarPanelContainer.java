package es.degrassi.forge.init.gui.container.panel;

import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.init.entity.panel.SolarPanelEntity;
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

public class SolarPanelContainer extends PanelContainer<SolarPanelEntity> {
  public SolarPanelContainer(int i, Inventory inv, SolarPanelEntity entity, ContainerData data) {
    super(ContainerRegistry.SP_CONTAINER.get(), i, entity, inv);
    this.data = data;

    this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
      this.addSlot(new SlotItemHandler(handler, 0, 8, 21));
      this.addSlot(new SlotItemHandler(handler, 1, 8, 39));
      this.addSlot(new SlotItemHandler(handler, 2, 8, 57));
      this.addSlot(new SlotItemHandler(handler, 3, 8, 75));
    });

    addDataSlots(data);
  }

  public SolarPanelContainer(int id, Inventory inv, SolarPanelEntity entity) {
    this(id, inv, entity, new SimpleContainerData(4));
  }

  public SolarPanelContainer(int id, Inventory playerInv, @NotNull FriendlyByteBuf extraData) {
    this(
      id,
      playerInv,
      ClientHandler.getClientSideSolarPanelEntity(extraData.readBlockPos())
    );
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return switch(entity.tier()) {
      case I -> stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
        player, BlockRegister.SP1_BLOCK.get());
      case II -> stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
        player, BlockRegister.SP2_BLOCK.get());
      case III -> stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
        player, BlockRegister.SP3_BLOCK.get());
      case IV -> stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
        player, BlockRegister.SP4_BLOCK.get());
      case V -> stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
        player, BlockRegister.SP5_BLOCK.get());
      case VI -> stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
        player, BlockRegister.SP6_BLOCK.get());
      case VII -> stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
        player, BlockRegister.SP7_BLOCK.get());
      case VIII -> stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
        player, BlockRegister.SP8_BLOCK.get());
    };
  }
}
