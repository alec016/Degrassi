package es.degrassi.forge.init.gui.container;

import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.entity.MelterEntity;
import es.degrassi.forge.init.gui.container.BaseContainer;
import es.degrassi.forge.init.gui.container.types.IProgressContainer;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ContainerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class MelterContainer extends BaseContainer<MelterEntity> implements IProgressContainer<MelterEntity> {

  // THIS YOU HAVE TO DEFINE!
  private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!
  public FluidStack fluid;
  public MelterContainer(int i, Inventory inv, MelterEntity entity, ContainerData data) {
    super(ContainerRegistry.MELTER_CONTAINER.get(), i);
    checkContainerSize(inv, TE_INVENTORY_SLOT_COUNT);
    this.entity = entity;
    this.level = inv.player.level;
    this.playerInv = inv;
    this.data = data;
    this.fluid = entity.getFluidStorage().getFluid();

    IClientHandler.addPlayerInventory(this, playerInv, 8, 98);

    this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
      this.addSlot(new SlotItemHandler(handler, 0, 35, 31)); // upgrade 1
      this.addSlot(new SlotItemHandler(handler, 1, 35, 67)); // upgrade 2
      this.addSlot(new SlotItemHandler(handler, 2, 62, 49)); // input slot
    });

    addDataSlots(data);
  }

  public MelterContainer(int id, Inventory inv, MelterEntity entity) {
    this(id, inv, entity, new SimpleContainerData(TE_INVENTORY_SLOT_COUNT));
  }

  public MelterContainer(int id, Inventory playerInv, @NotNull FriendlyByteBuf extraData) {
    this(
      id,
      playerInv,
      ClientHandler.getClientSideMelterEntity(extraData.readBlockPos())
    );
  }

  public MelterEntity getEntity() {
    return this.entity;
  }


  @Override
  public boolean isCrafting() {
    return entity.getProgressStorage().getProgress() > 0;
  }

  @Override
  public int getScaledProgress(int renderSize) {
    int progress = entity.getProgressStorage().getProgress();
    int maxProgress = entity.getProgressStorage().getMaxProgress();  // Max Progress
    int progressArrowSize = 26; // This is the height in pixels of your arrow

    return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
  }

  @Override
  public Component getId() {
    return Component.literal("Melter Container");
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

  @Override
  public boolean stillValid(@NotNull Player player) {
    return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
      player, BlockRegister.MELTER_BLOCK.get());
  }


  public void setFluid(FluidStack fluidStack) {
    this.fluid = fluidStack;
  }

  public FluidStack getFluid() {
    return fluid;
  }
}
