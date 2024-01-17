package es.degrassi.forge.init.gui.container.generators;

import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.entity.generators.GeneratorEntity;
import es.degrassi.forge.init.gui.container.BaseContainer;
import es.degrassi.forge.init.gui.container.types.IProgressContainer;
import es.degrassi.forge.init.gui.element.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public abstract class GeneratorContainer<T extends GeneratorEntity<?, ?>> extends BaseContainer<T> implements IProgressContainer<T> {
  private static int TE_INVENTORY_SLOT_COUNT;
  private final ElementManager manager;

  protected GeneratorContainer(@Nullable MenuType<?> menu, int i, int inventorySlotCount, T entity, Inventory inv) {
    super(menu, i);
    checkContainerSize(inv, inventorySlotCount);
    this.entity = entity;
    this.manager = entity.getElementManager();
    this.level = inv.player.level;
    this.playerInv = inv;
    TE_INVENTORY_SLOT_COUNT = inventorySlotCount;
    IClientHandler.addPlayerInventory(this, playerInv, 8, 98);
  }

  public T getEntity() {
    return this.entity;
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
