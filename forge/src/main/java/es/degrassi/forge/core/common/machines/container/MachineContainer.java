package es.degrassi.forge.core.common.machines.container;

import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class MachineContainer<T extends MachineEntity> extends AbstractContainerMenu {
  int HOTBAR_SLOT_COUNT = 9;
  int PLAYER_INVENTORY_ROW_COUNT = 3;
  int PLAYER_INVENTORY_COLUMN_COUNT = 9;
  int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
  int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
  int VANILLA_FIRST_SLOT_INDEX = 0;
  int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
  // THIS YOU HAVE TO DEFINE!
  protected static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!

  private final T entity;
  private final Level level;
  private final Inventory playerInv;
  protected MachineContainer(@Nullable MenuType<?> menuType, int containerId, T entity, Inventory inventory) {
    super(menuType, containerId);
    this.entity = entity;
    this.playerInv = inventory;
    this.level = inventory.player.level();
  }

  public T getEntity() {
    return entity;
  }

  public Level getLevel() {
    return level;
  }

  public Inventory getPlayerInv() {
    return playerInv;
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
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
    sourceSlot.onTake(player, sourceStack);
    return copyOfSourceStack;
  }
}
