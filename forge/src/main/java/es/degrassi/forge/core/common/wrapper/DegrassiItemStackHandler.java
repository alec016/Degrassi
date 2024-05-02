package es.degrassi.forge.core.common.wrapper;

import es.degrassi.forge.core.common.component.ItemComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class DegrassiItemStackHandler extends ItemStackHandler {
  private final List<ItemComponent> components;
  public DegrassiItemStackHandler(MachineEntity<?> entity) {
    this.components = entity.getComponentManager().getComponentsByType("item").stream().map(comp -> (ItemComponent) comp).toList();
  }

  @Override
  public int getSlots() {
    return components.size();
  }

  @Override
  public void setStackInSlot(int slot, @NotNull ItemStack stack) {
    validateSlotIndex(slot);
    this.components.get(slot).setStackInSlot(0, stack);
    onContentsChanged(slot);
  }

  public ItemStack getStackInSlot(int slot) {
    validateSlotIndex(slot);
    return this.components.get(slot).getStackInSlot(0);
  }

  @Override
  public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;

    if (!isItemValid(slot, stack))
      return stack;

    validateSlotIndex(slot);

    if (components.get(slot).getMode().output())
      return stack;

    ItemStack existing = this.components.get(slot).getStackInSlot(0);

    int limit = getStackLimit(slot, stack);

    if (!existing.isEmpty()) {
      if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
        return stack;

      limit -= existing.getCount();
    }

    if (limit <= 0)
      return stack;

    boolean reachedLimit = stack.getCount() > limit;

    if (!simulate) {
      if (existing.isEmpty()) {
        this.components.get(slot).setStackInSlot(0, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
      }
      else {
        existing.grow(reachedLimit ? limit : stack.getCount());
      }
      onContentsChanged(slot);
    }

    return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
  }

  @Override
  public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (components.get(slot).getMode().input())
      return ItemStack.EMPTY;

    if (amount == 0)
      return ItemStack.EMPTY;

    validateSlotIndex(slot);

    ItemStack existing = this.components.get(slot).getStackInSlot(0);

    if (existing.isEmpty())
      return ItemStack.EMPTY;

    int toExtract = Math.min(amount, existing.getMaxStackSize());

    if (existing.getCount() <= toExtract) {
      if (!simulate) {
        this.components.get(slot).setStackInSlot(0, ItemStack.EMPTY);
        onContentsChanged(slot);
        return existing;
      }
      else {
        return existing.copy();
      }
    }
    else {
      if (!simulate) {
        this.components.get(slot).setStackInSlot(0, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
        onContentsChanged(slot);
      }

      return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
    }
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    return components.get(slot).isItemValid(0, stack);
  }

  @Override
  protected void validateSlotIndex(int slot) {
    if (slot < 0 ||slot >= getSlots())
      throw new RuntimeException("Slot " + slot + " not in validate range - [0, " + getSlots() + ")");
  }
}
