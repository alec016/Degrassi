package es.degrassi.forge.init.gui.container.furnace;

import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.entity.furnace.FurnaceEntity;
import es.degrassi.forge.init.gui.container.BaseContainer;
import es.degrassi.forge.init.gui.container.types.IProgressContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FurnaceContainer extends BaseContainer<FurnaceEntity> implements IProgressContainer<FurnaceEntity> {

  // THIS YOU HAVE TO DEFINE!
  protected static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!

  protected FurnaceContainer(@Nullable MenuType<?> menuType, int i, FurnaceEntity entity, Inventory inv) {
    super(menuType, i);
    checkContainerSize(inv, TE_INVENTORY_SLOT_COUNT);
    this.entity = entity;
    this.level = inv.player.level;
    this.playerInv = inv;

    IClientHandler.addPlayerInventory(this, playerInv, 8, 98);
  }

  @Override
  public boolean isCrafting() {
    return entity.progressStorage.getProgress() > 0;
  }

  @Override
  public int getScaledProgress(int renderSize) {
    int progress = this.entity.progressStorage.getProgress();
    int maxProgress = this.entity.progressStorage.getMaxProgress();  // Max Progress

    return maxProgress != 0 && progress != 0 ? (int) (progress / (float) maxProgress) * renderSize : 0;
  }

  public FurnaceEntity getEntity() {
    return this.entity;
  }

  @Override
  public void clicked(int slotId, int dragType, @NotNull ClickType clickTypeIn, @NotNull Player player) {
    this.entity.setChanged();
    if (slotId == 3 + VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT && slots.get(slotId).mayPickup(player)) this.entity.dropExperience();
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
        if (sourceSlot.index == 3 + VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) this.entity.dropExperience();
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
