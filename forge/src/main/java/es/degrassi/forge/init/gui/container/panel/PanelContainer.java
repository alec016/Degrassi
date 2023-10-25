package es.degrassi.forge.init.gui.container.panel;

import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.entity.panel.PanelEntity;
import es.degrassi.forge.init.gui.container.IContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PanelContainer extends AbstractContainerMenu implements IContainer<PanelEntity> {

  // THIS YOU HAVE TO DEFINE!
  private static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!

  protected final PanelEntity entity;
  protected final Level level;
  protected final Inventory playerInv;
  protected ContainerData data;

  protected PanelContainer(@Nullable MenuType<?> menuType, int id, PanelEntity entity, Inventory inv) {
    super(menuType, id);
    checkContainerSize(inv, 4);
    this.entity = entity;
    this.level = inv.player.level;
    this.playerInv = inv;

    IClientHandler.addPlayerInventory(this, playerInv, 8, 98);
  }

  public PanelEntity getEntity() {
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
