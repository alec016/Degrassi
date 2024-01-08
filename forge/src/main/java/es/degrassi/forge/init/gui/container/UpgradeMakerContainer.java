package es.degrassi.forge.init.gui.container;

import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.entity.UpgradeMakerEntity;
import es.degrassi.forge.init.gui.container.types.IProgressContainer;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ContainerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class UpgradeMakerContainer extends BaseContainer<UpgradeMakerEntity> implements IProgressContainer<UpgradeMakerEntity> {
  // THIS YOU HAVE TO DEFINE!
  private static final int TE_INVENTORY_SLOT_COUNT = 5;  // must be the number of slots you have!
  public FluidStack fluid;
  public UpgradeMakerContainer(int i, Inventory inv, UpgradeMakerEntity entity, ContainerData data) {
    super(ContainerRegistry.UPGRADE_MAKER_CONTAINER.get(), i);
    this.entity = entity;
    this.level = inv.player.level;
    this.playerInv = inv;
    this.data = data;
    this.fluid = entity.getFluidStorage().getFluid();

    IClientHandler.addPlayerInventory(this, playerInv, 8, 98);

    this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
      this.addSlot(new SlotItemHandler(handler, 0, 91, 22)); // upgrade 1
      this.addSlot(new SlotItemHandler(handler, 1, 110, 22)); // upgrade 2
      this.addSlot(new SlotItemHandler(handler, 2, 49, 32)); // input slot 1
      this.addSlot(new SlotItemHandler(handler, 3, 49, 68)); // input slot 2
      this.addSlot(new SlotItemHandler(handler, 4, 152, 50)); // output slot
    });

    addDataSlots(data);
  }

  public UpgradeMakerContainer(int id, Inventory inv, UpgradeMakerEntity entity) {
    this(id, inv, entity, new SimpleContainerData(TE_INVENTORY_SLOT_COUNT));
  }

  public UpgradeMakerContainer(int id, Inventory playerInv, @NotNull FriendlyByteBuf extraData) {
    this(
      id,
      playerInv,
      ClientHandler.getClientSideUpgradeMakerEntity(extraData.readBlockPos())
    );
  }

  @Override
  public UpgradeMakerEntity getEntity() {
    return this.entity;
  }

  @Override
  public Component getId() {
    return Component.literal("Upgrade Maker Container");
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
      player, BlockRegister.UPGRADE_MAKER.get());
  }

  public void setFluid(FluidStack fluidStack) {
    this.fluid = fluidStack;
  }

  public FluidStack getFluid() {
    return fluid;
  }

  @Override
  public void clicked(int slotId, int dragType, @NotNull ClickType clickTypeIn, @NotNull Player player) {
    this.entity.setChanged();
    super.clicked(slotId, dragType, clickTypeIn, player);
  }

  @Override
  public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
    Slot sourceSlot = slots.get(index);
    if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
    ItemStack sourceStack = sourceSlot.getItem();
    ItemStack copyOfSourceStack = sourceStack.copy();

    // Check if the slot clicked is one of the vanilla container slots
    if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
      // This is a vanilla container slot so merge the stack into the tile inventory
      if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
        + TE_INVENTORY_SLOT_COUNT, false)) {
        return ItemStack.EMPTY;  // EMPTY_ITEM
      }
    } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
      // This is a TE slot so merge the stack into the players inventory
      if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
        return ItemStack.EMPTY;
      }
    } else {
      System.out.println("Invalid slotIndex:" + index);
      return ItemStack.EMPTY;
    }
    // If stack size == 0 (the entire stack was moved) set slot contents to null
    if (sourceStack.getCount() == 0) {
      sourceSlot.set(ItemStack.EMPTY);
    } else {
      sourceSlot.setChanged();
    }
    sourceSlot.onTake(playerIn, sourceStack);
    return copyOfSourceStack;
  }
}
